/*******************************************************************************
 * oltpbenchmark.com
 *  
 *  Project Info:  http://oltpbenchmark.com
 *  Project Members:  	Carlo Curino <carlo.curino@gmail.com>
 * 				Evan Jones <ej@evanjones.ca>
 * 				DIFALLAH Djellel Eddine <djelleleddine.difallah@unifr.ch>
 * 				Andy Pavlo <pavlo@cs.brown.edu>
 * 				CUDRE-MAUROUX Philippe <philippe.cudre-mauroux@unifr.ch>  
 *  				Yang Zhang <yaaang@gmail.com> 
 * 
 *  This library is free software; you can redistribute it and/or modify it under the terms
 *  of the GNU General Public License as published by the Free Software Foundation;
 *  either version 3.0 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU Lesser General Public License for more details.
 ******************************************************************************/
package com.oltpbenchmark.benchmarks.wikipedia;

import java.net.UnknownHostException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.Procedure.UserAbortException;
import com.oltpbenchmark.api.TransactionGenerator;
import com.oltpbenchmark.api.TransactionType;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.benchmarks.wikipedia.procedures.AddWatchList;
import com.oltpbenchmark.benchmarks.wikipedia.procedures.GetPageAnonymous;
import com.oltpbenchmark.benchmarks.wikipedia.procedures.GetPageAuthenticated;
import com.oltpbenchmark.benchmarks.wikipedia.procedures.RemoveWatchList;
import com.oltpbenchmark.benchmarks.wikipedia.procedures.UpdatePage;
import com.oltpbenchmark.benchmarks.wikipedia.util.Article;
import com.oltpbenchmark.benchmarks.wikipedia.util.WikipediaOperation;
import com.oltpbenchmark.types.TransactionStatus;
import com.oltpbenchmark.util.RandomDistribution.Flat;
import com.oltpbenchmark.util.TextGenerator;

public class WikipediaWorker extends Worker {
	private static final Logger LOG = Logger.getLogger(WikipediaWorker.class);
	private final TransactionGenerator<WikipediaOperation> generator;

	final Flat usersRng;
	final int num_users;

	public WikipediaWorker(int id, WikipediaBenchmark benchmarkModule,
			TransactionGenerator<WikipediaOperation> generator) {
		super(benchmarkModule, id);
		this.generator = generator;
		this.num_users = (int) Math.round(WikipediaConstants.USERS
				* this.getWorkloadConfiguration().getScaleFactor());
		this.usersRng = new Flat(rng(), 1, this.num_users);
	}

	private String generateUserIP() {
		return String.format("%d.%d.%d.%d", rng().nextInt(255) + 1, rng()
				.nextInt(256), rng().nextInt(256), rng().nextInt(256));
	}

	@Override
	protected TransactionStatus executeWork(TransactionType nextTransaction)
			throws UserAbortException, SQLException {
		WikipediaOperation t = null;

		Class<? extends Procedure> procClass = nextTransaction
				.getProcedureClass();
		boolean needUser = (procClass.equals(AddWatchList.class)
				|| procClass.equals(RemoveWatchList.class) || procClass
				.equals(GetPageAuthenticated.class));
		while (t == null) {
			t = this.generator.nextTransaction();
			if (needUser && t.userId == 0) {
				t = null;
			}
		} // WHILE
		assert (t != null);
		if (t.userId != 0)
			t.userId = this.usersRng.nextInt();

		// AddWatchList
		if (procClass.equals(AddWatchList.class)) {
			assert (t.userId > 0);
			addToWatchlist(t.userId, t.nameSpace, t.pageTitle);
		}
		// RemoveWatchList
		else if (procClass.equals(RemoveWatchList.class)) {
			assert (t.userId > 0);
			removeFromWatchlist(t.userId, t.nameSpace, t.pageTitle);
		}
		// UpdatePage
		else if (procClass.equals(UpdatePage.class)) {
			updatePage(this.generateUserIP(), t.userId, t.nameSpace,
					t.pageTitle);
		}
		// GetPageAnonymous
		else if (procClass.equals(GetPageAnonymous.class)) {
			getPageAnonymous(true, this.generateUserIP(), t.nameSpace,
					t.pageTitle);
		}
		// GetPageAuthenticated
		else if (procClass.equals(GetPageAuthenticated.class)) {
			assert (t.userId > 0);
			getPageAuthenticated(true, this.generateUserIP(), t.userId,
					t.nameSpace, t.pageTitle);
		}

		// conn.commit();
		return (TransactionStatus.SUCCESS);
	}

	/**
	 * Implements wikipedia selection of last version of an article (with and
	 * without the user being logged in)
	 * 
	 * @parama userIp contains the user's IP address in dotted quad form for
	 *         IP-based access control
	 * @param userId
	 *            the logged in user's identifer. If negative, it is an
	 *            anonymous access.
	 * @param nameSpace
	 * @param pageTitle
	 * @return article (return a Class containing the information we extracted,
	 *         useful for the updatePage transaction)
	 * @throws SQLException
	 * @throws UnknownHostException
	 */
	public Article getPageAnonymous(boolean forSelect, String userIp,
			int nameSpace, String pageTitle) throws SQLException {
		GetPageAnonymous proc = this.getProcedure(GetPageAnonymous.class);
		assert (proc != null);
		return proc.run(conn, forSelect, userIp, nameSpace, pageTitle);
	}

	public Article getPageAuthenticated(boolean forSelect, String userIp,
			int userId, int nameSpace, String pageTitle) throws SQLException {
		GetPageAuthenticated proc = this
				.getProcedure(GetPageAuthenticated.class);
		assert (proc != null);
		return proc.run(conn, forSelect, userIp, userId, nameSpace, pageTitle);
	}

	public void addToWatchlist(int userId, int nameSpace, String pageTitle)
			throws SQLException {
		AddWatchList proc = this.getProcedure(AddWatchList.class);
		assert (proc != null);
		proc.run(conn, userId, nameSpace, pageTitle);
	}

	public void removeFromWatchlist(int userId, int nameSpace, String pageTitle)
			throws SQLException {
		RemoveWatchList proc = this.getProcedure(RemoveWatchList.class);
		assert (proc != null);
		proc.run(conn, userId, nameSpace, pageTitle);
	}

	public void updatePage(String userIp, int userId, int nameSpace,
			String pageTitle) throws SQLException {
		Article a = getPageAnonymous(false, userIp, nameSpace, pageTitle);
		conn.commit();

		// TODO: If the Article is null, then we want to insert a new page.
		// But we don't support that right now.
		if (a == null)
			return;

		WikipediaBenchmark b = this.getBenchmarkModule();
		int revCommentLen = b.commentLength.nextValue().intValue();
		String revComment = TextGenerator.randomStr(rng(), revCommentLen);
		int revMinorEdit = b.minorEdit.nextValue().intValue();

		// Permute the original text of the article
		// Important: We have to make sure that we fill in the entire array
		char newText[] = b.generateRevisionText(a.oldText.toCharArray());

		if (LOG.isTraceEnabled())
			LOG.trace("UPDATING: Page: id:" + a.pageId + " ns:" + nameSpace
					+ " title" + pageTitle);
		UpdatePage proc = this.getProcedure(UpdatePage.class);
		assert (proc != null);
		proc.run(conn, a.textId, a.pageId, pageTitle, new String(newText),
				nameSpace, userId, userIp, a.userText, a.revisionId,
				revComment, revMinorEdit);
	}

}

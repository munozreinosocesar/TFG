package com.oltpbenchmark.benchmarks.chbenchmark.queries;

import com.oltpbenchmark.api.SQLStmt;

public class Q16 extends GenericQuery {

	protected static SQLStmt query_stmt;

	static {
		final String queryFile = "query16.sql";

		query_stmt = initSQLStmt(queryFile);
	}

	protected SQLStmt get_query() {
		return query_stmt;
	}
}

/*******************************************************************************
 * oltpbenchmark.com
 *  
 *  Project Info:  http://oltpbenchmark.com
 *  Project Members:    Carlo Curino <carlo.curino@gmail.com>
 *              Evan Jones <ej@evanjones.ca>
 *              DIFALLAH Djellel Eddine <djelleleddine.difallah@unifr.ch>
 *              Andy Pavlo <pavlo@cs.brown.edu>
 *              CUDRE-MAUROUX Philippe <philippe.cudre-mauroux@unifr.ch>  
 *                  Yang Zhang <yaaang@gmail.com> 
 * 
 *  This library is free software; you can redistribute it and/or modify it under the terms
 *  of the GNU General Public License as published by the Free Software Foundation;
 *  either version 3.0 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  See the GNU Lesser General Public License for more details.
 ******************************************************************************/

 package com.oltpbenchmark.benchmarks.ecommerce.procedures;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.SQLException;
 
 import com.oltpbenchmark.api.Procedure;
 import com.oltpbenchmark.api.SQLStmt;
 import com.oltpbenchmark.benchmarks.ecommerce.EcommerceConstants;
 
public class AddToCart extends Procedure {
 
	 // -----------------------------------------------------------------
	 // SQL STATEMENTS
	 // -----------------------------------------------------------------
 
	 public SQLStmt insertCartItem = new SQLStmt("INSERT INTO "
			 + EcommerceConstants.TABLENAME_CARRITO_PRODUCTOS + " (" 
			 + "carrito_id, producto_id) VALUES (?, ?) ON CONFLICT (carrito_id, producto_id) DO NOTHING");

	 // -----------------------------------------------------------------
	 // RUN METHOD
	 // -----------------------------------------------------------------
 
	 public void run(Connection conn, int carritoId, int productoId)
			 throws SQLException {
		 if (carritoId > 0) {
			 PreparedStatement ps = this.getPreparedStatement(conn, insertCartItem);
			 ps.setInt(1, carritoId);
			 ps.setInt(2, productoId);
			 ps.executeUpdate();
		 }
	 }
}

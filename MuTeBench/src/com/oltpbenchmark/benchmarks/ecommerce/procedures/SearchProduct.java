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
 import java.sql.ResultSet;
 import java.sql.SQLException;
 
 import com.oltpbenchmark.api.Procedure;
 import com.oltpbenchmark.api.SQLStmt;
 import com.oltpbenchmark.benchmarks.ecommerce.EcommerceConstants;
 
 public class SearchProduct extends Procedure {
 
	 // -----------------------------------------------------------------
	 // SQL STATEMENTS
	 // -----------------------------------------------------------------
 
	 public SQLStmt selectProduct = new SQLStmt("SELECT * FROM "
			 + EcommerceConstants.TABLENAME_PRODUCTO
			 + " WHERE id = ? LIMIT 1");
 
	 // -----------------------------------------------------------------
	 // RUN METHOD
	 // -----------------------------------------------------------------
 
	 public void run(Connection conn, int productId) throws SQLException {
		 PreparedStatement st = this.getPreparedStatement(conn, selectProduct);
		 st.setInt(1, productId);
		 ResultSet rs = st.executeQuery();
 
		 if (!rs.next()) {
			 throw new SQLException("Producto no encontrado: ID " + productId);
		 }
 
		 // Recuperar datos del producto
		 int id = rs.getInt("id");
		 String name = rs.getString("name");
		 double price = rs.getDouble("price");
 
		 rs.close();
 
		 // Log de búsqueda (opcional)
		 //System.out.println("Producto encontrado - ID: " + id + ", Nombre: " + name + ", Precio: $" + price);
	 }
 }
 
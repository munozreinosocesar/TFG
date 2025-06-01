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
 
 public class ViewOrderHistory extends Procedure {
 
	 // -----------------------------------------------------------------
	 // SQL STATEMENTS
	 // -----------------------------------------------------------------
 
	 public SQLStmt selectOrders = new SQLStmt(
		 "SELECT p.id AS pedido_id, p.fecha, pp.producto_id " +
		 "FROM " + EcommerceConstants.TABLENAME_PEDIDO + " p " +
		 "JOIN " + EcommerceConstants.TABLENAME_PEDIDO_PRODUCTOS + " pp " +
		 "ON p.id = pp.pedido_id " +
		 "WHERE p.usuario_id = ? " +
		 "ORDER BY p.fecha DESC"
	 );
 
	 // -----------------------------------------------------------------
	 // RUN METHOD
	 // -----------------------------------------------------------------
 
	public void run(Connection conn, int userId) throws SQLException {
		PreparedStatement st = this.getPreparedStatement(conn, selectOrders);
		st.setInt(1, userId);
		ResultSet rs = st.executeQuery();

		//System.out.println("Historial de pedidos para usuario ID: " + userId);

		while (rs.next()) {
			int pedidoId = rs.getInt("pedido_id");
			String fecha = rs.getString("fecha");
			int productoId = rs.getInt("producto_id");

			//System.out.println("Pedido ID: " + pedidoId + ", Fecha: " + fecha + ", Producto ID: " + productoId);
		}

		rs.close();
	}
 }
 
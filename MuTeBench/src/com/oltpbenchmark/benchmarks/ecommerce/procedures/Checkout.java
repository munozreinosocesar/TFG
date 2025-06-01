package com.oltpbenchmark.benchmarks.ecommerce.procedures;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;

public class Checkout extends Procedure {

    public final SQLStmt createOrder = new SQLStmt(
        "INSERT INTO pedido (usuario_id, fecha) VALUES (?, CURRENT_TIMESTAMP) RETURNING id;"
    );

    public final SQLStmt moveItemsToOrder = new SQLStmt(
        "INSERT INTO pedido_productos (pedido_id, producto_id) " +
        "SELECT ?, producto_id FROM carrito_productos " +
        "WHERE carrito_id = (SELECT id FROM carrito WHERE usuario_id = ?);"
    );

    public final SQLStmt clearCart = new SQLStmt(
        "DELETE FROM carrito_productos WHERE carrito_id = (SELECT id FROM carrito WHERE usuario_id = ?);"
    );

    public void run(Connection conn, int userId) throws SQLException {
        int orderId = -1;

        // 1️⃣ Crear un nuevo pedido y obtener su ID
        PreparedStatement psCreateOrder = this.getPreparedStatement(conn, createOrder);
        psCreateOrder.setInt(1, userId);
        ResultSet rs = psCreateOrder.executeQuery();
        if (rs.next()) {
            orderId = rs.getInt(1);
        }
        rs.close(); // solo cerramos el ResultSet, no el PreparedStatement

        if (orderId == -1) {
            throw new SQLException("Error: No se pudo crear el pedido para el usuario " + userId);
        }

        // 2️⃣ Mover productos del carrito al pedido
        PreparedStatement psMoveItems = this.getPreparedStatement(conn, moveItemsToOrder);
        psMoveItems.setInt(1, orderId);
        psMoveItems.setInt(2, userId);
        psMoveItems.executeUpdate();

        // 3️⃣ Vaciar el carrito del usuario
        PreparedStatement psClearCart = this.getPreparedStatement(conn, clearCart);
        psClearCart.setInt(1, userId);
        psClearCart.executeUpdate();
    }
}

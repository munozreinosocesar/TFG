package com.oltpbenchmark.benchmarks.ecommerce.procedures;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.benchmarks.ecommerce.EcommerceConstants;

public class RemoveFromCart extends Procedure {

    // -----------------------------------------------------------------
    // SQL STATEMENTS
    // -----------------------------------------------------------------

    public SQLStmt removeCartItem = new SQLStmt("DELETE FROM "
            + EcommerceConstants.TABLENAME_CARRITO_PRODUCTOS
            + " WHERE carrito_id = ? AND producto_id = ?");

    // -----------------------------------------------------------------
    // RUN METHOD
    // -----------------------------------------------------------------

    public void run(Connection conn, int carritoId, int productoId) throws SQLException {
        if (carritoId > 0) {
            PreparedStatement ps = this.getPreparedStatement(conn, removeCartItem);
            ps.setInt(1, carritoId);
            ps.setInt(2, productoId);
            ps.executeUpdate();
        }
    }
}

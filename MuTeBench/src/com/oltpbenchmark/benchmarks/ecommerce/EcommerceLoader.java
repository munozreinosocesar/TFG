package com.oltpbenchmark.benchmarks.ecommerce;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import com.oltpbenchmark.api.Loader;
import com.oltpbenchmark.catalog.Table;
import com.oltpbenchmark.util.SQLUtil;

/**
 * Carga de datos de prueba para eCommerce
 */
public class EcommerceLoader extends Loader {
    private static final Logger LOG = Logger.getLogger(EcommerceLoader.class);

    private final int num_usuarios;
    private final int num_productos;

    /**
     * Constructor
     */
    public EcommerceLoader(EcommerceBenchmark benchmark, Connection c) {
        super(benchmark, c);
        this.num_usuarios = (int) Math.round(EcommerceConstants.USUARIOS * this.scaleFactor);
        this.num_productos = (int) Math.round(EcommerceConstants.PRODUCTOS * this.scaleFactor);

        if (LOG.isDebugEnabled()) {
            LOG.debug("# de USUARIOS: " + this.num_usuarios);
            LOG.debug("# de PRODUCTOS: " + this.num_productos);
        }
    }

    @Override
    public void load() {
        try {
            this.loadUsuarios();
            this.loadDirecciones();
            this.loadProductos();
            this.loadCarritos();
            this.loadCarritosProductos();
            this.loadPedidos();
            this.loadPedidosProductos();
            this.loadPagos();
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getNextException() != null)
                e = e.getNextException();
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Carga de Usuarios
     */
    private void loadUsuarios() throws SQLException {
        Table catalog_tbl = this.getTableCatalog("usuario");
        assert (catalog_tbl != null);

        String sql = SQLUtil.getInsertSQL(catalog_tbl);
        PreparedStatement stmt = this.conn.prepareStatement(sql);

        for (int i = 1; i <= this.num_usuarios; i++) {
            stmt.setInt(1, i);
            stmt.setString(2, "Usuario_" + i);
            stmt.setString(3, "Apellido_" + i);
            stmt.setString(4, "usuario" + i + "@example.com");
            stmt.setString(5, "password" + i); // Nuevo valor para la contraseña
            stmt.setInt(6, i % 14);
            stmt.addBatch();
        }
        stmt.executeBatch();
        this.conn.commit();
        stmt.close();
    }

    /**
     * Carga de Direcciones
     */
    private void loadDirecciones() throws SQLException {
        Table catalog_tbl = this.getTableCatalog("direccion");
        assert (catalog_tbl != null);

        String sql = SQLUtil.getInsertSQL(catalog_tbl);
        PreparedStatement stmt = this.conn.prepareStatement(sql);

        for (int i = 1; i <= this.num_usuarios; i++) {
            stmt.setInt(1, i);
            stmt.setInt(2, i);
            stmt.setString(3, "Calle " + i);
            stmt.setString(4, "Ciudad " + (i % 10));
            stmt.setString(5, "Pais " + (i % 5));
            stmt.setString(6, "28080");
            stmt.addBatch();
        }
        stmt.executeBatch();
        this.conn.commit();
        stmt.close();
    }

    /**
     * Carga de Productos
     */
    private void loadProductos() throws SQLException {
        Table catalog_tbl = this.getTableCatalog("producto");
        assert (catalog_tbl != null);

        String sql = SQLUtil.getInsertSQL(catalog_tbl);
        PreparedStatement stmt = this.conn.prepareStatement(sql);

        for (int i = 1; i <= this.num_productos; i++) {
            stmt.setInt(1, i);
            stmt.setString(2, "Producto_" + i);
            stmt.setDouble(3, 10 + (Math.random() * 90));
            stmt.addBatch();
        }
        stmt.executeBatch();
        this.conn.commit();
        stmt.close();
    }

    /**
     * Carga de Carritos
     */
    private void loadCarritos() throws SQLException {
        Table catalog_tbl = this.getTableCatalog("carrito");
        assert (catalog_tbl != null);

        String sql = SQLUtil.getInsertSQL(catalog_tbl);
        PreparedStatement stmt = this.conn.prepareStatement(sql);

        for (int i = 1; i <= this.num_usuarios; i++) {
            stmt.setInt(1, i);
            stmt.addBatch();
        }
        stmt.executeBatch();
        this.conn.commit();
        stmt.close();
    }

    /**
     * Carga de Carritos con Productos (relación muchos a muchos)
     */
    private void loadCarritosProductos() throws SQLException {
        Table catalog_tbl = this.getTableCatalog("carrito_productos");
        assert (catalog_tbl != null);

        String sql = SQLUtil.getInsertSQL(catalog_tbl);
        PreparedStatement stmt = this.conn.prepareStatement(sql);

        for (int i = 1; i <= this.num_usuarios; i++) {
            stmt.setInt(1, i);
            stmt.setInt(2, (i % this.num_productos) + 1);
            stmt.addBatch();
        }
        stmt.executeBatch();
        this.conn.commit();
        stmt.close();
    }

    /**
     * Carga de Pedidos
     */
    private void loadPedidos() throws SQLException {
        Table catalog_tbl = this.getTableCatalog("pedido");
        assert (catalog_tbl != null);

        String sql = SQLUtil.getInsertSQL(catalog_tbl);
        PreparedStatement stmt = this.conn.prepareStatement(sql);

        for (int i = 1; i <= this.num_usuarios; i++) {
            stmt.setInt(1, i);
            stmt.setInt(2, i);
            stmt.setString(3, "20240305120000");
            stmt.addBatch();
        }
        stmt.executeBatch();
        this.conn.commit();
        stmt.close();
    }

    /**
     * Carga de Pedido_Productos
     */
    private void loadPedidosProductos() throws SQLException {
        Table catalog_tbl = this.getTableCatalog("pedido_productos");
        assert (catalog_tbl != null);

        String sql = SQLUtil.getInsertSQL(catalog_tbl);
        PreparedStatement stmt = this.conn.prepareStatement(sql);

        for (int i = 1; i <= this.num_usuarios; i++) {
            stmt.setInt(1, i);
            stmt.setInt(2, (i % this.num_productos) + 1);
            stmt.addBatch();
        }
        stmt.executeBatch();
        this.conn.commit();
        stmt.close();
    }

    /**
     * Carga de Pagos
     */
    private void loadPagos() throws SQLException {
        Table catalog_tbl = this.getTableCatalog("pagos");
        assert (catalog_tbl != null);

        String sql = SQLUtil.getInsertSQL(catalog_tbl);
        PreparedStatement stmt = this.conn.prepareStatement(sql);

        for (int i = 1; i <= this.num_usuarios; i++) {
            stmt.setInt(1, i);
            stmt.setInt(2, i);
            stmt.setInt(3, i);
            stmt.setDouble(4, 99.99);
            stmt.setString(5, "Tarjeta");
            stmt.setString(6, "20240305121000");
            stmt.addBatch();
        }
        stmt.executeBatch();
        this.conn.commit();
        stmt.close();
    }
}

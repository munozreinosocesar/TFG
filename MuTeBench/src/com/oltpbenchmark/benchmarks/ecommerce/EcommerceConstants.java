package com.oltpbenchmark.benchmarks.ecommerce;

public abstract class EcommerceConstants {

    /**
     * Número base de productos en la tienda
     */
    public static final int PRODUCTOS = 1000;

    /**
     * Número base de usuarios
     */
    public static final int USUARIOS = 2000;

    // ----------------------------------------------------------------
    // DATA SET INFORMATION
    // ----------------------------------------------------------------

    /**
     * Nombres de las tablas
     */
    public static final String TABLENAME_USUARIO = "usuario";
    public static final String TABLENAME_DIRECCION = "direccion";
    public static final String TABLENAME_PRODUCTO = "producto";
    public static final String TABLENAME_CARRITO = "carrito";
    public static final String TABLENAME_CARRITO_PRODUCTOS = "carrito_productos";
    public static final String TABLENAME_PEDIDO = "pedido";
    public static final String TABLENAME_PEDIDO_PRODUCTOS = "pedido_productos";
    public static final String TABLENAME_PAGOS = "pagos";

    public static final int BATCH_SIZE = 1000;
}

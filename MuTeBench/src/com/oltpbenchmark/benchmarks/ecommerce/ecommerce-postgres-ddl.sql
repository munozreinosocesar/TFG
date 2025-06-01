-- 🚀 Eliminar tablas si existen (en orden para evitar errores por dependencias)
DROP TABLE IF EXISTS carrito_productos CASCADE;
DROP TABLE IF EXISTS pedido_productos CASCADE;
DROP TABLE IF EXISTS pagos CASCADE;
DROP TABLE IF EXISTS pedido CASCADE;
DROP TABLE IF EXISTS carrito CASCADE;
DROP TABLE IF EXISTS producto CASCADE;
DROP TABLE IF EXISTS direccion CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;

-- 🚀 Eliminar secuencias si existen
DROP SEQUENCE IF EXISTS producto_id_seq;
DROP SEQUENCE IF EXISTS carrito_id_seq;
DROP SEQUENCE IF EXISTS pedido_id_seq;
DROP SEQUENCE IF EXISTS usuario_id_seq;
DROP SEQUENCE IF EXISTS direccion_id_seq;
DROP SEQUENCE IF EXISTS pagos_id_seq;

-- 🚀 Crear las secuencias necesarias
CREATE SEQUENCE producto_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE carrito_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE pedido_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE usuario_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE direccion_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE pagos_id_seq START WITH 1 INCREMENT BY 1;

-- 🚀 Crear la tabla Usuario
CREATE TABLE usuario (
    id BIGINT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    tenant_id INT NOT NULL
);
CREATE INDEX IDX_USUARIO_EMAIL ON usuario(email);
CREATE INDEX IDX_USUARIO_TENANT ON usuario(tenant_id);

-- 🚀 Crear la tabla Direccion con relación uno a uno con Usuario
CREATE TABLE direccion (
    id BIGINT PRIMARY KEY,
    usuario_id BIGINT UNIQUE REFERENCES usuario(id) ON DELETE CASCADE,
    calle VARCHAR(255),
    ciudad VARCHAR(100),
    pais VARCHAR(50),
    codigo_postal VARCHAR(20)
);

-- 🚀 Crear la tabla Producto
CREATE TABLE producto (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE PRECISION NOT NULL
);
CREATE INDEX IDX_PRODUCTO_PRECIO ON producto(price);

-- 🚀 Crear la tabla Carrito
CREATE TABLE carrito (
    id BIGINT PRIMARY KEY
);

-- 🚀 Crear la tabla intermedia carrito_productos
CREATE TABLE carrito_productos (
    carrito_id BIGINT REFERENCES carrito(id) ON DELETE CASCADE,
    producto_id BIGINT REFERENCES producto(id) ON DELETE CASCADE,
    PRIMARY KEY (carrito_id, producto_id)
);

-- 🚀 Crear la tabla Pedido con relación a Usuario
CREATE TABLE pedido (
    id BIGINT PRIMARY KEY,
    usuario_id BIGINT REFERENCES usuario(id) ON DELETE CASCADE,
    fecha VARCHAR(14) NOT NULL
);
CREATE INDEX IDX_PEDIDO_FECHA ON pedido(fecha);
CREATE INDEX IDX_PEDIDO_USUARIO ON pedido(usuario_id);

-- 🚀 Crear la tabla intermedia pedido_productos
CREATE TABLE pedido_productos (
    pedido_id BIGINT REFERENCES pedido(id) ON DELETE CASCADE,
    producto_id BIGINT REFERENCES producto(id) ON DELETE CASCADE,
    PRIMARY KEY (pedido_id, producto_id)
);

-- 🚀 Crear la tabla Pagos con relación a Pedido y Usuario
CREATE TABLE pagos (
    id BIGINT PRIMARY KEY,
    usuario_id BIGINT REFERENCES usuario(id) ON DELETE CASCADE,
    pedido_id BIGINT UNIQUE REFERENCES pedido(id) ON DELETE CASCADE,
    monto DECIMAL(10,2),
    metodo_pago VARCHAR(50),
    fecha VARCHAR(14) NOT NULL
);
CREATE INDEX IDX_PAGOS_USUARIO ON pagos(usuario_id);


-- 1. Eliminar las secuencias si existen
DROP SEQUENCE IF EXISTS categoria_seq CASCADE;
DROP SEQUENCE IF EXISTS ciudad_seq CASCADE;
DROP SEQUENCE IF EXISTS cliente_seq CASCADE;
DROP SEQUENCE IF EXISTS detallepedido_seq CASCADE;
DROP SEQUENCE IF EXISTS empleado_seq CASCADE;
DROP SEQUENCE IF EXISTS estadopedido_seq CASCADE;
DROP SEQUENCE IF EXISTS inventario_seq CASCADE;
DROP SEQUENCE IF EXISTS pedido_seq CASCADE;
DROP SEQUENCE IF EXISTS producto_seq CASCADE;
DROP SEQUENCE IF EXISTS rol_seq CASCADE;
DROP SEQUENCE IF EXISTS sucursal_seq CASCADE;
DROP SEQUENCE IF EXISTS transaccion_seq CASCADE;
DROP SEQUENCE IF EXISTS usuario_seq CASCADE;
DROP SEQUENCE IF EXISTS metodo_pago_seq CASCADE;
DROP SEQUENCE IF EXISTS transferencia_seq CASCADE;
DROP SEQUENCE IF EXISTS estado_trasnfe_seq CASCADE;

-- 2. Crear las secuencias
CREATE SEQUENCE categoria_seq START 1;
CREATE SEQUENCE ciudad_seq START 1;
CREATE SEQUENCE cliente_seq START 1;
CREATE SEQUENCE detallepedido_seq START 1;
CREATE SEQUENCE empleado_seq START 1;
CREATE SEQUENCE estadopedido_seq START 1;
CREATE SEQUENCE inventario_seq START 1;
CREATE SEQUENCE pedido_seq START 1;
CREATE SEQUENCE producto_seq START 1;
CREATE SEQUENCE rol_seq START 1;
CREATE SEQUENCE sucursal_seq START 1;
CREATE SEQUENCE transaccion_seq START 1;
CREATE SEQUENCE usuario_seq START 1;
CREATE SEQUENCE metodo_pago_seq START 1;
CREATE SEQUENCE transferencia_seq START 1;
CREATE SEQUENCE estado_trasnfe_seq START 1;


-- 3. Eliminar las tablas si existen
DROP TABLE IF EXISTS detallepedido CASCADE;
DROP TABLE IF EXISTS inventario CASCADE;
DROP TABLE IF EXISTS transaccion CASCADE;
DROP TABLE IF EXISTS pedido CASCADE;
DROP TABLE IF EXISTS empleado CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;
DROP TABLE IF EXISTS cliente CASCADE;
DROP TABLE IF EXISTS producto CASCADE;
DROP TABLE IF EXISTS estadopedido CASCADE;
DROP TABLE IF EXISTS sucursal CASCADE;
DROP TABLE IF EXISTS categoria CASCADE;
DROP TABLE IF EXISTS rol CASCADE;
DROP TABLE IF EXISTS ciudad CASCADE;
DROP TABLE IF EXISTS metodo_pago CASCADE;
DROP TABLE IF EXISTS Transferencia CASCADE;
DROP TABLE IF EXISTS EstadoTransferencia CASCADE;
DROP TABLE IF EXISTS ClienteInvitado CASCADE;
DROP TABLE IF EXISTS RolUsuario CASCADE;
DROP TABLE IF EXISTS MetodoPago CASCADE;


CREATE TABLE Categoria (
    id_categoria INTEGER PRIMARY KEY DEFAULT nextval('categoria_seq'),
    nombre VARCHAR(50),
    descripcion VARCHAR(120)
);

CREATE TABLE Ciudad (
    id_ciudad INTEGER PRIMARY KEY DEFAULT nextval('ciudad_seq'),
    nombre VARCHAR(50)
);

CREATE TABLE USUARIO (
    rut_usuario VARCHAR(15) PRIMARY KEY,
    nombreUsuario VARCHAR(50) NOT NULL,
    contrasena VARCHAR(128) NOT NULL,
    correo VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE CLIENTE (
    rut_usuario VARCHAR(15) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    telefono VARCHAR(15) NOT NULL,
    direccion VARCHAR(120),
    FOREIGN KEY (rut_usuario) REFERENCES USUARIO (rut_usuario)
);

CREATE TABLE ClienteInvitado (
    rutCliente VARCHAR(15) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    correo VARCHAR(50) NOT NULL,
    telefono VARCHAR(15) NOT NULL
);

CREATE TABLE EstadoPedido (
    id_estadoPedido INTEGER PRIMARY KEY DEFAULT nextval('estadopedido_seq'),
    nombre VARCHAR(50),
    descripcion VARCHAR(120)
);

CREATE TABLE EstadoTransferencia (
    idEstadoTrnsf INTEGER PRIMARY KEY DEFAULT nextval('estado_trasnfe_seq'),
    nombre VARCHAR(55) NOT NULL,
    descripcion VARCHAR(120) NOT NULL
);

CREATE TABLE MetodoPago (
    id_metodo_pago INTEGER PRIMARY KEY DEFAULT nextval('metodo_pago_seq'),
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(120) NOT NULL
);

CREATE TABLE SUCURSAL (
    id_sucursal INTEGER PRIMARY KEY DEFAULT nextval('sucursal_seq'),
    nombre VARCHAR(50),
    direccion VARCHAR(120),
    telefono VARCHAR(15),
    id_ciudad INTEGER NOT NULL,
    FOREIGN KEY (id_ciudad) REFERENCES Ciudad (id_ciudad)
);

CREATE TABLE Empleado (
    rut_usuario VARCHAR(15) PRIMARY KEY,
    id_sucursal INTEGER NOT NULL,
    FOREIGN KEY (rut_usuario) REFERENCES USUARIO (rut_usuario),
    FOREIGN KEY (id_sucursal) REFERENCES SUCURSAL (id_sucursal)
);

CREATE TABLE PRODUCTO (
    id_producto INTEGER PRIMARY KEY DEFAULT nextval('producto_seq'),
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT NOT NULL,
    precio INTEGER NOT NULL,
    marca VARCHAR(255) NOT NULL,
	imagen VARCHAR(255),
    id_categoria INTEGER NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES Categoria (id_categoria)
);

CREATE TABLE PEDIDO (
    id_pedido INTEGER PRIMARY KEY DEFAULT nextval('pedido_seq'),
    fecha DATE NOT NULL,
    estado VARCHAR(50) NOT NULL,
    total INTEGER NOT NULL,
    rutCliente VARCHAR(15) NOT NULL,
    id_estadoPedido INTEGER NOT NULL,
    id_sucursal INTEGER NOT NULL,
    FOREIGN KEY (id_estadoPedido) REFERENCES EstadoPedido (id_estadoPedido),
    FOREIGN KEY (id_sucursal) REFERENCES SUCURSAL (id_sucursal)
);

CREATE TABLE DetallePedido (
    id_detallePedido INTEGER PRIMARY KEY DEFAULT nextval('detallepedido_seq'),
    cantidad INTEGER,
    id_producto INTEGER NOT NULL,
    id_pedido INTEGER NOT NULL,
    FOREIGN KEY (id_producto) REFERENCES PRODUCTO (id_producto),
    FOREIGN KEY (id_pedido) REFERENCES PEDIDO (id_pedido)
);

CREATE TABLE Inventario (
    id_producto INTEGER NOT NULL,
    id_sucursal INTEGER NOT NULL,
    stock INTEGER NOT NULL,
    PRIMARY KEY (id_producto, id_sucursal),
    FOREIGN KEY (id_producto) REFERENCES PRODUCTO (id_producto),
    FOREIGN KEY (id_sucursal) REFERENCES SUCURSAL (id_sucursal)
);

CREATE TABLE TRANSACCION (
    id_transaccion INTEGER PRIMARY KEY DEFAULT nextval('transaccion_seq'),
    fecha DATE NOT NULL,
    monto INTEGER NOT NULL,
    id_pedido INTEGER NOT NULL,
    id_metodo_pago INTEGER NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES PEDIDO (id_pedido),
    FOREIGN KEY (id_metodo_pago) REFERENCES MetodoPago (id_metodo_pago)
);

CREATE TABLE Transferencia (
    idTransferencia INTEGER PRIMARY KEY DEFAULT nextval('transferencia_seq'),
    monto INTEGER NOT NULL,
    fecha DATE NOT NULL,
    id_pedido INTEGER NOT NULL,
    comprobante VARCHAR(255) NOT NULL,
    comentario VARCHAR(255),
    idEstadoTrnsf INTEGER NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES PEDIDO (id_pedido),
    FOREIGN KEY (idEstadoTrnsf) REFERENCES EstadoTransferencia (idEstadoTrnsf)
);

CREATE TABLE Rol (
    id_rol INTEGER PRIMARY KEY DEFAULT nextval('rol_seq'),
    nombre VARCHAR(50),
    descripcion VARCHAR(120)
);

CREATE TABLE RolUsuario (
    id_rol INTEGER NOT NULL,
    rut_usuario VARCHAR(15) NOT NULL,
    PRIMARY KEY (rut_usuario, id_rol),
    FOREIGN KEY (id_rol) REFERENCES Rol (id_rol),
    FOREIGN KEY (rut_usuario) REFERENCES USUARIO (rut_usuario)
);

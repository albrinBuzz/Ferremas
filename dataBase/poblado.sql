-- Insertar en EstadoTransferencia
INSERT INTO EstadoTransferencia (nombre, descripcion)
VALUES
('Pendiente', 'La transferencia está pendiente'),
('Rechazada', 'La transferencia está rechazada'),
('Completada', 'La transferencia fue completada'),
('Cancelada', 'La transferencia fue cancelada');

-- Insertar categorías de productos
INSERT INTO Categoria (nombre, descripcion) VALUES
('Herramientas Manuales', 'Herramientas que no requieren energía eléctrica para su funcionamiento.'),
('Herramientas Eléctricas', 'Herramientas que funcionan con energía eléctrica, como taladros, sierras, etc.'),
('Materiales de Construcción', 'Materiales utilizados en la construcción como cemento, ladrillos, etc.'),
('Pinturas y Acabados', 'Pinturas, esmaltes y otros productos para el acabado de superficies.'),
('Materiales Eléctricos', 'Cables, interruptores, enchufes, y otros productos eléctricos.'),
('Accesorios de Seguridad', 'Elementos de protección personal como cascos, guantes, y lentes de seguridad.'),
('Sistemas de Iluminación', 'Lámparas, focos, bombillas y otros productos de iluminación.'),
('Ferretería General', 'Accesorios y productos diversos para construcción y mantenimiento general.'),
('Herramientas de Jardinería', 'Herramientas para el cuidado y mantenimiento de jardines y exteriores.');

-- Insertar ciudades
INSERT INTO Ciudad (nombre) VALUES
('Santiago'),
('Valparaíso'),
('Concepción'),
('La Serena'),
('Antofagasta'),
('Temuco'),
('Punta Arenas'),
('Iquique'),
('Rancagua');

-- Insertar roles
INSERT INTO rol (nombre, descripcion) VALUES
('Administrador', 'Encargado de la gestión de la tienda, informes y estrategias.'),
('Cliente', 'Rol asignado a los clientes que compran productos.'),
('Vendedor', 'Asesora a los clientes, gestiona los pedidos y facturación.'),
('Bodeguero', 'Organiza y prepara los productos para el despacho.'),
('Contador', 'Controla y registra las transacciones y pagos.');

-- Insertar estados de pedido
INSERT INTO estadopedido (nombre, descripcion) VALUES
('Pendiente', 'Pedido creado, pendiente de validación.'),
('Pagado', 'El pago ha sido confirmado.'),
('En Preparación', 'El pedido está siendo preparado por bodega.'),
('Listo para Retiro', 'El pedido está listo para ser retirado en tienda.'),
('Retirado', 'El cliente retiró el pedido.'),
('No Retirado', 'El cliente no retiró el pedido en el plazo estipulado.'),
('Cancelado', 'El pedido fue cancelado por el cliente o por la tienda.'),
('Solicitado', 'El cliente está en tienda esperando el pedido.'),
('Devuelto', 'El pedido fue devuelto después de la entrega.'),
('En Revisión', 'El pedido requiere revisión manual por problemas o inconsistencias.');

-- Insertar métodos de pago
INSERT INTO MetodoPago (nombre, descripcion) VALUES
('Tarjeta de Crédito', 'Pago mediante tarjeta de crédito'),
('Tarjeta debito', 'Pago mediante transferencia desde cuenta bancaria'),
('Transferencia Bancaria', 'Pago mediante transferencia desde cuenta bancaria'),
('Pago en Efectivo', 'Pago en efectivo en tienda o punto de venta');

-- Insertar sucursales
INSERT INTO sucursal (nombre, direccion, telefono, id_ciudad) VALUES
('FERREMAS Santiago', 'Av. Providencia 1234, Santiago', '229876543', 1),
('FERREMAS Santiago Centro', 'Calle Santa Rosa 1234, Santiago Centro', '222345678', 1),
('FERREMAS Santiago Norte', 'Calle Vicuña Mackenna 5678, Santiago Norte', '229876543', 1),
('FERREMAS Valparaíso', 'Calle Blanco 4321, Valparaíso', '324567890', 2);

-- Insertar usuarios (con RUT corregidos)
INSERT INTO Usuario (nombreUsuario, contrasena, correo, rut_usuario) VALUES
('adminFERREMAS', '$2a$10$hsjnyiws1X0PpAZYrNNbYuFacX53JUO9jfasNhL.WhHa6JpkO4O/m', 'admin@ferremas.cl', '12345678-9'),

('vendedor1', '$2a$10$hsjnyiws1X0PpAZYrNNbYuFacX53JUO9jfasNhL.WhHa6JpkO4O/m', 'vendedor1@ferremas.cl', '23456789-0'),
('bodeguero1', '$2a$10$hsjnyiws1X0PpAZYrNNbYuFacX53JUO9jfasNhL.WhHa6JpkO4O/m', 'bodeguero1@ferremas.cl', '34567890-1'),
('contador1', '$2a$10$hsjnyiws1X0PpAZYrNNbYuFacX53JUO9jfasNhL.WhHa6JpkO4O/m', 'contador1@ferremas.cl', '45678901-2'),

('vendedor2', '$2a$10$hsjnyiws1X0PpAZYrNNbYuFacX53JUO9jfasNhL.WhHa6JpkO4O/m', 'vendedor2@ferremas.cl', '13256729-0'),
('bodeguero2', '$2a$10$hsjnyiws1X0PpAZYrNNbYuFacX53JUO9jfasNhL.WhHa6JpkO4O/m', 'bodeguero2@ferremas.cl', '89012345-6'),
('contador2', '$2a$10$hsjnyiws1X0PpAZYrNNbYuFacX53JUO9jfasNhL.WhHa6JpkO4O/m', 'contador2@ferremas.cl', '12623902-2'),

('vendedor3', '$2a$10$hsjnyiws1X0PpAZYrNNbYuFacX53JUO9jfasNhL.WhHa6JpkO4O/m', 'vendedor3@ferremas.cl', '22446789-1'),
('bodeguero3', '$2a$10$hsjnyiws1X0PpAZYrNNbYuFacX53JUO9jfasNhL.WhHa6JpkO4O/m', 'bodeguero3@ferremas.cl', '14364890-7'),
('contador3', '$2a$10$hsjnyiws1X0PpAZYrNNbYuFacX53JUO9jfasNhL.WhHa6JpkO4O/m', 'contador3@ferremas.cl', '13478901-4'),

('cliente1', '$2a$10$hsjnyiws1X0PpAZYrNNbYuFacX53JUO9jfasNhL.WhHa6JpkO4O/m', 'cliente1@ferremas.cl', '56789012-3'),
('cliente2', '$2a$10$hsjnyiws1X0PpAZYrNNbYuFacX53JUO9jfasNhL.WhHa6JpkO4O/m', 'cliente2@ferremas.cl', '90123456-7'),
('cliente3', '$2a$10$hsjnyiws1X0PpAZYrNNbYuFacX53JUO9jfasNhL.WhHa6JpkO4O/m', 'cliente3@ferremas.cl', '01234567-8');

-- Insertar clientes
INSERT INTO Cliente (nombre, telefono, direccion, rut_usuario) VALUES
('Juan Pérez', '987654321', 'Calle Falsa 123, Santiago', '56789012-3'),
('Carlos Gómez', '987654322', 'Calle Los Andes 2345, Santiago', '89012345-6'),
('Gómez Carlos ', '98224311', 'Calle Los Andes 2345, Santiago', '01234567-8'),
('Ana María Pérez', '987654323', 'Calle del Sol 5678, Rancagua', '90123456-7');

-- Asignar el rol 'Usuario' a los clientes utilizando su rut_usuario
INSERT INTO RolUsuario (rut_usuario, id_rol)
SELECT rut_usuario, (SELECT id_rol FROM Rol WHERE nombre = 'Cliente')
FROM USUARIO
WHERE rut_usuario IN ('56789012-3', '89012345-6', '90123456-7','01234567-8');

-- Insertar empleados
INSERT INTO Empleado (rut_usuario, id_sucursal)
VALUES
('23456789-0', 1),  -- vendedor1 en la sucursal 2 (Valparaíso)
('34567890-1', 1),  -- bodeguero1 en la sucursal 3 (Concepción)
('45678901-2', 1),  -- contador1 en la sucursal 4 (La Serena)
('13256729-0', 2),  -- vendedor2 en la sucursal 2 (Valparaíso)
('89012345-6', 2),  -- vendedor3 en la sucursal 1 (Santiago)
('12623902-2', 2),  -- bodeguero2 en la sucursal 3 (Concepción)
('22446789-1', 3),  -- bodeguero3 en la sucursal 1 (Santiago)
('14364890-7', 3),  -- bodeguero3 en la sucursal 1 (Santiago)
('13478901-4', 3);  -- contador2 en la sucursal 4 (La Serena)

-- Asignar roles a los usuarios con un INSERT tradicional
INSERT INTO RolUsuario (rut_usuario, id_rol)
VALUES
('12345678-9', (SELECT id_rol FROM Rol WHERE nombre = 'Administrador')),  -- Admin
('23456789-0', (SELECT id_rol FROM Rol WHERE nombre = 'Vendedor')),      -- Vendedor1
('13256729-0', (SELECT id_rol FROM Rol WHERE nombre = 'Vendedor')),      -- Vendedor2
('22446789-1', (SELECT id_rol FROM Rol WHERE nombre = 'Vendedor')),      -- Vendedor3

('34567890-1', (SELECT id_rol FROM Rol WHERE nombre = 'Bodeguero')),     -- Bodeguero1
('89012345-6', (SELECT id_rol FROM Rol WHERE nombre = 'Bodeguero')),     -- Bodeguero2
('14364890-7', (SELECT id_rol FROM Rol WHERE nombre = 'Bodeguero')),     -- Bodeguero3

('45678901-2', (SELECT id_rol FROM Rol WHERE nombre = 'Contador')),      -- Contador1
('12623902-2', (SELECT id_rol FROM Rol WHERE nombre = 'Contador')),      -- Contador2
('13478901-4', (SELECT id_rol FROM Rol WHERE nombre = 'Contador'));      -- Contador3

-- Insertar productos
-- Insertar los productos en la tabla PRODUCTO
INSERT INTO PRODUCTO (nombre, descripcion, precio, marca, id_categoria, imagen)
VALUES
    ('Cinta Métrica Stanley 30-615', 'Cinta métrica de 5 metros y 25mm de ancho, con carcasa resistente y gancho para mediciones precisas', 8000, 'Stanley', 1, 'Cinta_Métrica_Stanley_30-615.jpeg'),
    ('Juego de Alicates Ingco HKPS28318', 'Conjunto de 3 alicates: de corte, punta y combinación, con mangos antideslizantes', 8600, 'Ingco', 2, 'Juego_de_Alicates_Ingco_HKPS28318.jpeg'),
    ('Llave Inglesa Bahco 9031', 'Llave ajustable fabricada en acero al cromo-vanadio, con diseño ergonómico', 12300, 'Bahco', 3, 'Llave_Inglesa_Bahco_9031.jpeg'),
    ('Nivel de Burbuja Truper NT-9', 'Nivel magnético de 9 pulgadas con dos burbujas horizontales y una vertical, fabricado en ABS', 4200, 'Truper', 4, 'Nivel_de_Burbuja_Truper_NT-9.jpeg'),
    ('Sierra de Mano Lidl Parkside PSBS 85 A1', 'Sierra de mano 3 en 1 para cortar madera, metal y plástico', 42000, 'Lidl Parkside', 2, 'Sierra_de_Mano_Lidl_Parkside_PSBS_85_A1.jpeg'),
    ('Esmeril Angular Total Tools 750W', 'Esmeril angular de 4.1/2 pulgadas con 750W de potencia, ideal para cortar y pulir materiales', 55000, 'Total Tools', 6, 'Esmeril_Angular_Total_Tools_750W.jpeg'),
    ('Lijadora DeWalt DWE6411-B3', 'Lijadora de acabado fino de 225W para superficies de madera y metal', 27899, 'DeWalt', 7, 'Lijadora_DeWalt_DWE6411-B3.jpeg'),
    ('Martillo Stanley 51-162', 'Martillo de carpintero con mango de fibra de vidrio y cabeza de acero forjado', 18880, 'Stanley', 8, 'MartilloStanley51-162.jpeg'),
    ('Pistola de Calor Parkside PHG 2000 A1', 'Pistola de calor con dos niveles de temperatura y diseño ergonómico', 23200, 'Parkside', 9, 'Pistola_de_Calor_Parkside_PHG_2000_A1.jpeg'),
    ('Taladro Eléctrico Black & Decker KR504CRES', 'Taladro de percusión de 500W con dos velocidades y diseño compacto',43600, 'Black & Decker', 2, 'taladroMakinaPercutor.jpeg');


-- Insertar inventarios para productos en sucursales
INSERT INTO inventario (id_producto, id_sucursal, stock) VALUES
(1, 1, 100),(1, 2, 17),(1, 3, 7),
(2, 2, 150), (3, 3, 120), (4, 3, 80), (5, 1, 200),
(6, 3, 90), (7, 1, 50), (8, 2, 30), (9, 2, 160), (10, 3, 140);



package com.ferremas.util;

import com.github.javafaker.Faker;

import java.sql.*;
import java.util.Random;

public class InsertBulk{
private static final String URL = "jdbc:postgresql://localhost:5432/ferremasDB";
private static final String USER = "ferremas";
private static final String PASSWORD = "ferremas";

private static final int NUM_PEDIDOS = 100;
private static final int NUM_PRODUCTOS = 5000;
private static final int NUM_INVENTARIO = 5000;
private static final Faker faker = new Faker();
private static final Random random = new Random();

public static void main(String[] args) {
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        connection.setAutoCommit(false);

        insertProductoData(connection,NUM_PRODUCTOS);
        insertInventarioData(connection,NUM_INVENTARIO);
        /*insertPedidosData(connection, NUM_PEDIDOS);
        insertDetallesPedidoData(connection, NUM_PEDIDOS);
        insertTransaccionesData(connection, NUM_PEDIDOS);
        insertTransferenciasData(connection, NUM_PEDIDOS);*/

        //connection.commit();

        System.out.println("Datos insertados exitosamente.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public static void insertPedidosData(Connection conn, int cantidad) throws SQLException {
    String sql = "INSERT INTO pedido (fecha, estado, total, rutcliente, id_estadopedido, id_sucursal) VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        for (int i = 0; i < cantidad; i++) {
            ps.setDate(1, new Date(System.currentTimeMillis() - random.nextInt(20) * 86400000L));
            ps.setString(2, "En Proceso");
            int total = (random.nextInt(10) + 1) * 5000;
            ps.setInt(3, total);
            ps.setString(4, getRandomRut());
            ps.setInt(5, 1); // id_estadopedido fijo
            ps.setInt(6, random.nextInt(5) + 1); // sucursales 1-5
            ps.executeUpdate();
        }
    }
}

    public static void insertProductoData(Connection connection, int numProductos) throws SQLException {
        String sql = "INSERT INTO Producto (nombre, descripcion, precio, marca, id_categoria) VALUES (?, ?, ?, ?,?)";
        Random random = new Random();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 1; i <= numProductos; i++) {
                ps.setString(1, faker.commerce().productName());
                ps.setString(2, faker.lorem().characters()); // Nombre del producto
                ps.setInt(3, random.nextInt() * 1000); // Descripción aleatoria
                ps.setString(4, faker.company().name());
                ps.setInt(5, random.nextInt(1,9));
                ps.executeUpdate();
            }
        }
        Logger.logInfo("Productos Insertados");
    }

    public static void insertInventarioData(Connection connection, int numInventarios) throws SQLException {
        String sqlCheck = "SELECT COUNT(*) FROM Inventario WHERE id_producto = ? AND id_sucursal = ?";
        String sqlInsert = "INSERT INTO Inventario (id_producto, id_sucursal, stock) VALUES (?, ?, ?)";

        Random random = new Random();
        PreparedStatement psCheck = connection.prepareStatement(sqlCheck);
        PreparedStatement psInsert = connection.prepareStatement(sqlInsert);

        connection.setAutoCommit(false); // Desactivamos auto-commit

        for (int i = 1; i <= numInventarios; i++) {
            int idProducto = random.nextInt(1, NUM_PRODUCTOS);
            int idSucursal = random.nextInt(1, 3);
            int stock = random.nextInt(100) + 1;

            // Verificar si ya existe la combinación (id_producto, id_sucursal)
            psCheck.setInt(1, idProducto);
            psCheck.setInt(2, idSucursal);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {  // Si no existe
                psInsert.setInt(1, idProducto);
                psInsert.setInt(2, idSucursal);
                psInsert.setInt(3, stock);
                psInsert.executeUpdate();
            } else {
                Logger.logInfo("Registro duplicado detectado: " + idProducto + ", " + idSucursal);
            }
        }

        connection.commit();
        connection.setAutoCommit(true); // Restauramos auto-commit

        Logger.logInfo("Inventarios Insertados");
    }



    public static void insertDetallesPedidoData(Connection conn, int cantidadPedidos) throws SQLException {
    String sql = "INSERT INTO detallepedido (cantidad, id_producto, id_pedido) VALUES (?, ?, ?)";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        for (int pedidoId = 1; pedidoId <= cantidadPedidos; pedidoId++) {
            int numProductos = random.nextInt(3) + 1;
            for (int j = 0; j < numProductos; j++) {
                ps.setInt(1, random.nextInt(3) + 1);
                ps.setInt(2, random.nextInt(10) + 1); // productos 1–10
                ps.setInt(3, pedidoId);
                ps.addBatch();
            }
        }
        ps.executeBatch();
    }
}

public static void insertTransaccionesData(Connection conn, int cantidadPedidos) throws SQLException {
    String sql = "INSERT INTO transaccion (fecha, monto, id_pedido, id_metodo_pago) VALUES (?, ?, ?, ?)";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        for (int i = 1; i <= cantidadPedidos; i++) {
            ps.setDate(1, new Date(System.currentTimeMillis()));
            ps.setInt(2, (random.nextInt(10) + 1) * 5000);
            ps.setInt(3, i);
            int metodoPago = random.nextInt(3) + 1;
            ps.setInt(4, metodoPago);
            ps.executeUpdate();
        }
    }
}

public static void insertTransferenciasData(Connection conn, int cantidadPedidos) throws SQLException {
    String sql = "INSERT INTO Transferencia (monto, fecha, id_pedido, comprobante, comentario, idEstadoTrnsf) VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        for (int i = 1; i <= cantidadPedidos; i++) {
            int metodoPago = random.nextInt(3) + 1;
            if (metodoPago == 2) { // Transferencia bancaria
                ps.setInt(1, (random.nextInt(10) + 1) * 5000);
                ps.setDate(2, new Date(System.currentTimeMillis()));
                ps.setInt(3, i);
                ps.setString(4, "comprobante_" + i + ".pdf");
                ps.setString(5, "Pago mediante transferencia");
                ps.setInt(6, 1); // estado pendiente
                ps.addBatch();
            }
        }
        ps.executeBatch();
    }
}

private static String getRandomRut() {
    return faker.number().digits(8) + "-" + faker.number().numberBetween(0, 9);
}

}

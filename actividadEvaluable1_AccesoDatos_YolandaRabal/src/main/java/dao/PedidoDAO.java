package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PedidoDAO {
    private final Connection connection;

    public PedidoDAO(Connection connection) {
        this.connection = connection;
    }

    public void agregarPedido(int idProducto, String descripcion, double precioTotal) {
        String query = "INSERT INTO Pedidos (id_producto, descripcion, precio_total) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idProducto);
            preparedStatement.setString(2, descripcion);
            preparedStatement.setDouble(3, precioTotal);
            preparedStatement.executeUpdate();
            System.out.println("Pedido agregado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al agregar el pedido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void mostrarTodosLosPedidos() {
        String query = "SELECT * FROM Pedidos";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("\nListado de todos los pedidos:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int idProducto = resultSet.getInt("id_producto");
                String descripcion = resultSet.getString("descripcion");
                double precioTotal = resultSet.getDouble("precio_total");

                System.out.printf("ID: %d, ID Producto: %d, Descripción: %s, Precio Total: %.2f%n",
                        id, idProducto, descripcion, precioTotal);
            }
        } catch (SQLException e) {
            System.err.println("Error al mostrar los pedidos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

package dao;

import model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProductoDAO {
    private final Connection connection;

    public ProductoDAO(Connection connection) {
        this.connection = connection;
    }

    public void agregarProductos(List<Producto> productos) {
        String query = "INSERT INTO Productos (nombre, descripcion, cantidad, precio) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (Producto producto : productos) {
                preparedStatement.setString(1, producto.getNombre());
                preparedStatement.setString(2, producto.getDescripcion());
                preparedStatement.setInt(3, producto.getCantidad());
                preparedStatement.setDouble(4, producto.getPrecio());
                preparedStatement.executeUpdate();
                System.out.println("Producto agregado: " + producto.getNombre());
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar los productos desde el JSON.");
            e.printStackTrace();
        }
    }

    public Producto obtenerProductoPorId(int idProducto) {
        String query = "SELECT * FROM Productos WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idProducto);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                String descripcion = resultSet.getString("descripcion");
                int cantidad = resultSet.getInt("cantidad");
                double precio = resultSet.getDouble("precio");

                return new Producto(idProducto, nombre, descripcion, cantidad, precio);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el producto por ID.");
            e.printStackTrace();
        }
        return null;
    }

    public void agregarProductosFav() {
        String selectQuery = "SELECT id FROM Productos WHERE precio > 1000";
        String insertQuery = "INSERT INTO Productos_Fav (id_producto) VALUES (?)";

        try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             ResultSet resultSet = selectStatement.executeQuery()) {

            while (resultSet.next()) {
                int idProducto = resultSet.getInt("id");

                try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                    insertStatement.setInt(1, idProducto);
                    insertStatement.executeUpdate();
                    System.out.println("Producto favorito agregado con ID: " + idProducto);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar los productos favoritos con precio mayor a 1000.");
            e.printStackTrace();
        }
    }

    public void mostrarTodosLosProductos() {
        String query = "SELECT * FROM Productos";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("\nListado de todos los productos:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                String descripcion = resultSet.getString("descripcion");
                int cantidad = resultSet.getInt("cantidad");
                double precio = resultSet.getDouble("precio");
                System.out.printf("ID: %d, Nombre: %s, Descripción: %s, Cantidad: %d, Precio: %.2f%n", id, nombre, descripcion, cantidad, precio);
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar todos los productos.");
            e.printStackTrace();
        }
    }

    public void mostrarProductosConPrecioInferiorA(double precioLimite) {
        String query = "SELECT * FROM Productos WHERE precio < ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, precioLimite);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("\nListado de productos con precio inferior a " + precioLimite + "€:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                String descripcion = resultSet.getString("descripcion");
                int cantidad = resultSet.getInt("cantidad");
                double precio = resultSet.getDouble("precio");
                System.out.printf("ID: %d, Nombre: %s, Descripción: %s, Cantidad: %d, Precio: %.2f%n", id, nombre, descripcion, cantidad, precio);
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar los productos con precio inferior a " + precioLimite + "€.");
            e.printStackTrace();
        }
    }
}

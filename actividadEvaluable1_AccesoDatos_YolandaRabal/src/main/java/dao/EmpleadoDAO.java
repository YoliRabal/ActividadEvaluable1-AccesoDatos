package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EmpleadoDAO {
    private final Connection connection;

    public EmpleadoDAO(Connection connection) {
        this.connection = connection;
    }

    public void agregarEmpleado(String nombre, String apellidos, String correo) {
        String query = String.format("INSERT INTO Empleados (nombre, apellidos, correo) VALUES ('%s', '%s', '%s')",
                nombre, apellidos, correo);
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
            System.out.println("Empleado agregado exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al agregar el empleado: " + e.getMessage());
            System.err.println("Consulta fallida: " + query);
            e.printStackTrace();
        }
    }

    public void mostrarTodosLosEmpleados() {
        String query = "SELECT * FROM Empleados";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("\nListado de todos los empleados:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                String apellidos = resultSet.getString("apellidos");
                String correo = resultSet.getString("correo");
                System.out.printf("ID: %d, Nombre: %s, Apellidos: %s, Correo: %s%n", id, nombre, apellidos, correo);
            }
        } catch (SQLException e) {
            System.err.println("Error al mostrar los empleados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

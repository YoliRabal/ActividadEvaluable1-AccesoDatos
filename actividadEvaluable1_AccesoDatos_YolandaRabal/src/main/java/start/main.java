package start;

import dao.EmpleadoDAO;
import dao.PedidoDAO;
import dao.ProductoDAO;
import database.DBConnection;
import json.JsonReader;
import model.Producto;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        try (Connection connection = DBConnection.getInstance()) {
            if (connection == null) {
                System.out.println("No se pudo establecer la conexión con la base de datos.");
                return;
            }

            Scanner scanner = new Scanner(System.in);
            EmpleadoDAO empleadoDAO = new EmpleadoDAO(connection);
            PedidoDAO pedidoDAO = new PedidoDAO(connection);
            ProductoDAO productoDAO = new ProductoDAO(connection);

            boolean ejecutar = true;

            while (ejecutar) {
                System.out.println("\n--- Menú Principal ---");
                System.out.println("1. Gestionar Empleados");
                System.out.println("2. Gestionar Pedidos");
                System.out.println("3. Gestionar Productos");
                System.out.println("4. Consultar Datos");
                System.out.println("5. Salir");

                int opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir la nueva línea

                switch (opcion) {
                    case 1:
                        gestionarEmpleados(empleadoDAO, scanner);
                        break;
                    case 2:
                        gestionarPedidos(pedidoDAO, productoDAO, scanner);
                        break;
                    case 3:
                        gestionarProductos(productoDAO, scanner);
                        break;
                    case 4:
                        consultarDatos(empleadoDAO, pedidoDAO, productoDAO, scanner);
                        break;
                    case 5:
                        ejecutar = false;
                        break;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                        break;
                }
            }

            System.out.println("Gracias por usar el sistema.");
        } catch (Exception e) {
            System.out.println("Error en la conexión principal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void gestionarEmpleados(EmpleadoDAO empleadoDAO, Scanner scanner) {
        System.out.println("\n--- Gestión de Empleados ---");
        boolean continuar = true;

        while (continuar) {
            System.out.println("1. Agregar Empleado");
            System.out.println("2. Regresar al Menú Principal");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.println("Nombre:");
                    String nombre = scanner.nextLine();
                    System.out.println("Apellidos:");
                    String apellidos = scanner.nextLine();
                    System.out.println("Correo:");
                    String correo = scanner.nextLine();

                    empleadoDAO.agregarEmpleado(nombre, apellidos, correo);
                    break;
                case 2:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private static void gestionarPedidos(PedidoDAO pedidoDAO, ProductoDAO productoDAO, Scanner scanner) {
        System.out.println("\n--- Gestión de Pedidos ---");
        boolean continuar = true;

        while (continuar) {
            System.out.println("1. Agregar Pedido");
            System.out.println("2. Regresar al Menú Principal");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    productoDAO.mostrarTodosLosProductos();
                    System.out.println("ID del Producto:");
                    int idProducto = scanner.nextInt();
                    scanner.nextLine();

                    Producto producto = productoDAO.obtenerProductoPorId(idProducto);
                    if (producto != null) {
                        pedidoDAO.agregarPedido(idProducto, "Pedido de: " + producto.getNombre(), producto.getPrecio());
                        System.out.println("Pedido agregado exitosamente.");
                    } else {
                        System.out.println("Producto no encontrado.");
                    }
                    break;
                case 2:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private static void gestionarProductos(ProductoDAO productoDAO, Scanner scanner) {
        System.out.println("\n--- Gestión de Productos ---");
        boolean continuar = true;

        while (continuar) {
            System.out.println("1. Agregar Productos desde JSON");
            System.out.println("2. Agregar Productos Favoritos (>1000 EUR)");
            System.out.println("3. Regresar al Menú Principal");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    agregarProductosDesdeJson(productoDAO);
                    break;
                case 2:
                    productoDAO.agregarProductosFav();
                    break;
                case 3:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private static void agregarProductosDesdeJson(ProductoDAO productoDAO) {
        System.out.println("Obteniendo productos desde el JSON...");
        List<Producto> productos = JsonReader.obtenerProductosDesdeJson();

        if (productos.isEmpty()) {
            System.out.println("No se encontraron productos en el JSON.");
            return;
        }

        productoDAO.agregarProductos(productos);
        System.out.println("Todos los productos del JSON han sido agregados a la base de datos.");
    }

    private static void consultarDatos(EmpleadoDAO empleadoDAO, PedidoDAO pedidoDAO, ProductoDAO productoDAO, Scanner scanner) {
        System.out.println("\n--- Consultas Disponibles ---");
        boolean continuar = true;

        while (continuar) {
            System.out.println("1. Mostrar todos los empleados");
            System.out.println("2. Mostrar todos los productos");
            System.out.println("3. Mostrar todos los pedidos");
            System.out.println("4. Mostrar productos con precio inferior a 600 EUR");
            System.out.println("5. Regresar al Menú Principal");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    empleadoDAO.mostrarTodosLosEmpleados();
                    break;
                case 2:
                    productoDAO.mostrarTodosLosProductos();
                    break;
                case 3:
                    pedidoDAO.mostrarTodosLosPedidos();
                    break;
                case 4:
                    productoDAO.mostrarProductosConPrecioInferiorA(600);
                    break;
                case 5:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }
}

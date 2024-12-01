package json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Producto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JsonReader {

    public static List<Producto> obtenerProductosDesdeJson() {
        List<Producto> productos = new ArrayList<>();
        try {
            URL url = new URL("https://dummyjson.com/products");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            connection.disconnect();

            String jsonString = content.toString();
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            JsonArray productsJsonArray = jsonObject.getAsJsonArray("products");

            Gson gson = new Gson();
            for (JsonElement productElement : productsJsonArray) {
                JsonObject productObject = productElement.getAsJsonObject();

                String nombre = productObject.get("title").getAsString();
                String descripcion = productObject.get("description").getAsString();
                int cantidad = productObject.get("stock").getAsInt();
                double precio = productObject.get("price").getAsDouble();

                Producto producto = new Producto(nombre, descripcion, cantidad, precio);
                productos.add(producto);
            }

        } catch (Exception e) {
            System.out.println("Error al obtener los productos desde JSON: " + e.getMessage());
            e.printStackTrace();
        }

        return productos;
    }
}

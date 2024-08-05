package org.jlz;

import com.google.gson.Gson;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

import io.github.cdimascio.dotenv.Dotenv;

public class GatosService {

  private static Dotenv dotenv = Dotenv.load();
  private static String apiKey = dotenv.get("API_KEY");


  public static void verGatos() throws IOException {
    OkHttpClient client = new OkHttpClient();
    String urlApi = "https://api.thecatapi.com/v1/images/search";

    Request request = new Request.Builder().url(urlApi).build();

    try (Response response = client.newCall(request).execute()) {
      if (response.isSuccessful()) {
        assert response.body() != null;
        String responseBody = response.body().string();
        // Eliminar los corchetes de array de la respuesta para poder pasar de [{}] a {}
        responseBody = responseBody.substring(1, responseBody.length());
        responseBody = responseBody.substring(0, responseBody.length()-1);

        // Usamos Gson para convertir la respuesta de Json a un objeto Gatos
        Gson gson = new Gson();
        Gatos gatos = gson.fromJson(responseBody, Gatos.class);

        // Redimensionar la imagen a otro tamaño
        Image image = null;
        try{
          // Leer url como clase URL
          URL urlImage = new URL(gatos.getUrl());
          // Cargar una imagen desde la URL
          image = ImageIO.read(urlImage);
          //
          ImageIcon fondoGato = new ImageIcon(image);
          if(fondoGato.getIconWidth() > 800){
            // Redimencionamos
            Image fondo = fondoGato.getImage();
            // Cambiar el tamaño de la imagen ancho, alto, metodo de escalado
            Image modificada = fondo.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
            fondoGato = new ImageIcon(modificada);
          }
          String menu = "Opciones: \n"
                        + "1. Ver otra imagen \n"
                        + "2. Favorito \n"
                        + "3. Ver Favoritos \n"
                        + "4. Volver \n";
          String[] botones = {"Ver otra imagen", "Favorito", "Ver Favoritos", "Volver"};
          String id_gato = gatos.getId();
          String opcion = (String) (String) JOptionPane.showInputDialog(
              null,
              menu,
              id_gato,
              JOptionPane.INFORMATION_MESSAGE,
              fondoGato,
              botones,
              botones[0]
          );

          int seleccion = -1;

          for(int i=0; i<botones.length; i++){
            if(opcion.equals(botones[i])){
              seleccion = i;
            }
          }

          switch (seleccion){
            case 0:
              verGatos();
              break;
            case 1:
              favoritoGato(gatos);
              break;
            case 2:
              listarFavorito();
              break;
            default:
              break;
          }
        }
        catch (IOException e){
          System.out.println("Error en la petición "+ e);
        }
        //System.out.println("Respuesta de la API:\n" + responseBody);
      } else {
        System.out.println("Error en la solicitud: " + response.code());
      }
    }
  }

  public static void favoritoGato(@NotNull Gatos gato) throws IOException {
    System.out.println("Agregar a favoritos");
    String urlApi = "https://api.thecatapi.com/v1/favourites";

    System.out.println(gato.toString());

    OkHttpClient client = new OkHttpClient();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, "{\r\n    \"image_id\":\"" + gato.getId() + "\"\r\n}");

    Request request = new Request.Builder()
        .url(urlApi)
        .post(body)
        .addHeader("Content-Type", "application/json")
        .addHeader("x-api-key", gato.getApiKey())
        .build();

    try(Response response = client.newCall(request).execute();) {
      // Procesar la respuesta
      assert response.body() != null;
      String responseBody = response.body().string();
      System.out.println(responseBody);
    }
    catch (IOException e){
      System.err.println(e);
    }
  }

  public static void listarFavorito() throws IOException {
    System.out.println("Lista de favoritos");
    String urlApi = "https://api.thecatapi.com/v1/favourites";
    OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder()
        .url(urlApi)
        .addHeader("Content-Type", "application/json")
        .addHeader("x-api-key", apiKey)
        .build();

    try(Response response = client.newCall(request).execute()){
      if(response.isSuccessful()){
        assert response.body() != null;
        String responseBody = response.body().string();
        System.out.println(responseBody);
        // Creamos la lista de clases GatosFavoritos
        Gson gson = new Gson();
        // Desde la respuesta creamos los objetos
        GatosFavoritos[] gatosFavs = gson.fromJson(responseBody, GatosFavoritos[].class);

        // Seleccionar una de las imagenes de favoritos para ver
        if(gatosFavs.length > 0){
          int min = 1;
          int max = gatosFavs.length;
          int aleatorio = (int) (Math.random() * (max-min)-1) + min;
          int indice = aleatorio - 1;
          GatosFavoritos gatoFav = gatosFavs[indice];

          // Redimensionar la imagen a otro tamaño
          Image image = null;
          try{
            // Leer url como clase URL
            URL urlImage = new URL(gatoFav.getImage().getUrl());
            // Cargar una imagen desde la URL
            image = ImageIO.read(urlImage);
            //
            ImageIcon fondoGato = new ImageIcon(image);
            if(fondoGato.getIconWidth() > 800){
              // Redimencionamos
              Image fondo = fondoGato.getImage();
              // Cambiar el tamaño de la imagen ancho, alto, metodo de escalado
              Image modificada = fondo.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
              fondoGato = new ImageIcon(modificada);
            }
            String menu = "Opciones: \n"
                + "1. Ver otra imagen \n"
                + "2. Eliminar de Favorito \n"
                + "4. Volver \n";
            String[] botones = {"Ver Favorito", "Eliminar Favoritos", "Volver"};
            String id_gato = gatoFav.getImage().getId();
            String opcion = (String) (String) JOptionPane.showInputDialog(
                null,
                menu,
                id_gato,
                JOptionPane.INFORMATION_MESSAGE,
                fondoGato,
                botones,
                botones[0]
            );

            int seleccion = -1;

            for(int i=0; i<botones.length; i++){
              if(opcion.equals(botones[i])){
                seleccion = i;
              }
            }

            switch (seleccion){
              case 0:
                listarFavorito();
                break;
              case 1:
                eliminarDeFavoritos(gatoFav);
                break;
              default:
                break;
            }
          }
          catch (IOException e){
            System.out.println("Error en la petición "+ e);
          }
        }
        else{
          System.out.println("No hay favoritos aún");
        }
      }
    }
    catch (IOException e){ System.out.println(e);}
  }

  public static void eliminarDeFavoritos(GatosFavoritos gatoFav){
    System.out.println("Lista de favoritos");
    String urlApi = "https://api.thecatapi.com/v1/favourites"+"/"+gatoFav.getId();
    System.out.println("URL: "+ urlApi);
    OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder()
        .url(urlApi)
        .addHeader("Content-Type", "application/json")
        .addHeader("x-api-key", apiKey)
        .delete()
        .build();

    try(Response response = client.newCall(request).execute()){
      if(response.isSuccessful()) {
        assert response.body() != null;
        String responseBody = response.body().string();
        System.out.println(responseBody);
        System.out.println("Se ha eliminado: "+ gatoFav.getId());
        listarFavorito();
      }
    }
    catch (IOException e){
      System.err.println(e);
    }
  }
}

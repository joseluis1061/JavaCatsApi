package org.jlz;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;


public class GatosService {

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
                        + "3. Volver d\n";
          String[] botones = {"Ver otra imagen", "Favorito", "Volver"};
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
              favoritoGato();
              break;
            default:

              break;
          }
        }
        catch (IOException e){
          System.out.println("Error en la petición "+ e);
        }
        System.out.println("Respuesta de la API:\n" + responseBody);
      } else {
        System.out.println("Error en la solicitud: " + response.code());
      }
    }
  }

  public static void favoritoGato(){
    System.out.println("Gatos favoritos");
  }
}

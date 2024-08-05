package org.jlz;
import io.github.cdimascio.dotenv.Dotenv;

public class GatosFavoritos {
  private final Dotenv dotenv = Dotenv.load();

  String id;
  String image_id;
  String apiKey = dotenv.get("API_KEY");
  ImageGatos image;

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ImageGatos getImage() {
    return image;
  }

  public void setImage(ImageGatos image) {
    this.image = image;
  }

  public String getImage_id() {
    return image_id;
  }

  public void setImage_id(String image_id) {
    this.image_id = image_id;
  }
}

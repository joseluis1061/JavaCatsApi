package org.jlz;
import io.github.cdimascio.dotenv.Dotenv;
public class Gatos {
  Dotenv dotenv = Dotenv.load();

  private String id;
  private String url;
  private String apiKey = dotenv.get("API_KEY");
  private String image;


  public String getId() {
    return id;
  }

  public void setId(int id) {
    this.id = String.valueOf(id);
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return "Gatos{" +
        "id='" + id + '\'' +
        ", image='" + image + '\'' +
        ", url='" + url + '\'' +
        ", apiKey='" + apiKey + '\'' +
        '}';
  }
}

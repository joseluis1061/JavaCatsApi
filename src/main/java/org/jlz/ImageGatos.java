package org.jlz;

public class ImageGatos {
  String id;
  String url;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return "ImageGatos{" +
        "id='" + id + '\'' +
        ", url='" + url + '\'' +
        '}';
  }
}

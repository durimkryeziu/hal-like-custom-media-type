package com.example.hallikecustommediatype.image;

import java.util.UUID;

public class Image {

  private final UUID id;
  private final String url;

  public Image(String url) {
    this.id = UUID.randomUUID();
    this.url = url;
  }

  public UUID id() {
    return id;
  }

  public String url() {
    return url;
  }
}

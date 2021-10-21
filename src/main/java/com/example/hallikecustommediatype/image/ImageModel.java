package com.example.hallikecustommediatype.image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = IanaLinkRelations.ITEM_VALUE)
final class ImageModel extends RepresentationModel<ImageModel> {

  @JsonIgnore
  private final UUID id;

  @JsonProperty
  private final String url;

  ImageModel(UUID id, String url) {
    this.id = id;
    this.url = url;
  }

  public static ImageModel fromImage(Image image) {
    return new ImageModel(image.id(), image.url());
  }

  public UUID id() {
    return id;
  }
}

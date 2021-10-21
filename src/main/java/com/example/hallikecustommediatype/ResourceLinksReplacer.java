package com.example.hallikecustommediatype;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;

public final class ResourceLinksReplacer {

  private ResourceLinksReplacer() {
  }

  public static <R extends RepresentationModel<? extends R>> void replaceResourceLinks(
      RepresentationModel<R> resource, Link... links) {
    Links updatedLinks = resource.getLinks().merge(Links.MergeMode.REPLACE_BY_REL, links);
    resource.removeLinks().add(updatedLinks);
  }
}

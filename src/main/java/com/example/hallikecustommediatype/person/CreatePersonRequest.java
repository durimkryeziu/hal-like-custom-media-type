package com.example.hallikecustommediatype.person;

import com.example.hallikecustommediatype.image.ImageController;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriTemplate;

public class CreatePersonRequest extends RepresentationModel<CreatePersonRequest> {

  @JsonProperty
  private String firstName;

  @JsonProperty
  private String lastName;

  public CreatePersonRequest() {
  }

  public CreatePersonRequest(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public String firstName() {
    return firstName;
  }

  public String lastName() {
    return lastName;
  }

  public UUID imageId() {
    return getLinkedResourceId("image", WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ImageController.class).getImage(null, null, null)));
  }

  private UUID getLinkedResourceId(String rel, WebMvcLinkBuilder linkBuilder) {
    Link link = getLink(rel).orElse(null);
    if (link == null) {
      return null;
    }
    List<Link> links = stripQueryParameters(List.of(link));
    if (CollectionUtils.isEmpty(links) || links.get(0) == null) {
      return null;
    }
    link = links.get(0);
    UriTemplate uriTemplate = new UriTemplate(linkBuilder.withSelfRel().getHref());
    String id = uriTemplate.match(link.getHref()).get("id");
    return UUID.fromString(id);
  }

  private List<Link> stripQueryParameters(List<Link> links) {
    List<Link> strippedLinks = new ArrayList<>();
    if (links == null || links.isEmpty()) {
      return strippedLinks;
    }
    for (Link link : links) {
      URI uri = link.toUri();
      URI strippedUri;
      try {
        strippedUri = new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), null, uri.getFragment());
        strippedLinks.add(Link.of(strippedUri.toString(), link.getRel()));
      } catch (URISyntaxException e) {
        System.out.printf("Error while stripping queryParams from %s%n", link);
      }
    }
    return strippedLinks;
  }
}

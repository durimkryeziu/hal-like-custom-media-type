package com.example.hallikecustommediatype.image;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
class ImageModelAssembler extends RepresentationModelAssemblerSupport<Image, ImageModel> {

  public ImageModelAssembler() {
    super(ImageController.class, ImageModel.class);
  }

  @Override
  public ImageModel toModel(Image entity) {
    var model = createModelWithId(entity.id(), entity);
    model.add(linkTo(methodOn(ImageController.class).listAll()).withRel("images"));
    return model;
  }

  @Override
  protected ImageModel instantiateModel(Image entity) {
    return ImageModel.fromImage(entity);
  }
}

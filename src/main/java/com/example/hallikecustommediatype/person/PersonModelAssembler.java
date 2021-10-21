package com.example.hallikecustommediatype.person;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.hallikecustommediatype.image.ImageController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
class PersonModelAssembler extends RepresentationModelAssemblerSupport<Person, PersonModel> {

  public PersonModelAssembler() {
    super(PersonController.class, PersonModel.class);
  }

  @Override
  public PersonModel toModel(Person entity) {
    var model = createModelWithId(entity.id(), entity);
    var image = entity.image();
    model.add(linkTo(methodOn(PersonController.class).listAll()).withRel("people"));
    if (image != null) {
      model.add(linkTo(methodOn(ImageController.class).getImage(image.id(), null, null)).withRel("image"));
    }
    return model;
  }

  @Override
  protected PersonModel instantiateModel(Person entity) {
    return PersonModel.fromPerson(entity);
  }
}

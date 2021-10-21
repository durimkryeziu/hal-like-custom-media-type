package com.example.hallikecustommediatype.person;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.hallikecustommediatype.image.ImageRepository;
import java.util.UUID;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/people")
public class PersonController {

  private final PersonRepository personRepository;
  private final PersonModelAssembler personModelAssembler;
  private final ImageRepository imageRepository;

  public PersonController(PersonRepository personRepository, PersonModelAssembler personModelAssembler,
                          ImageRepository imageRepository) {
    this.personRepository = personRepository;
    this.personModelAssembler = personModelAssembler;
    this.imageRepository = imageRepository;
  }

  @PostMapping
  public ResponseEntity<PersonModel> create(@RequestBody CreatePersonRequest request) {
    var image = this.imageRepository.findById(request.imageId()).orElseThrow();
    var person = this.personRepository.save(new Person(request.firstName(), request.lastName()).setImage(image));
    var personModel = this.personModelAssembler.toModel(person);
    var location = personModel.getRequiredLink(IanaLinkRelations.SELF).toUri();
    return ResponseEntity.created(location).body(personModel);
  }

  @GetMapping
  public ResponseEntity<CollectionModel<PersonModel>> listAll() {
    var personModels = this.personRepository.findAll()
        .stream()
        .map(personModelAssembler::toModel)
        .collect(toList());
    return ResponseEntity.ok(CollectionModel.of(personModels, linkTo(methodOn(PersonController.class).listAll()).withSelfRel()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<PersonModel> getPerson(@PathVariable UUID id) {
    var personModel = this.personRepository.findById(id)
        .map(this.personModelAssembler::toModel);
    return ResponseEntity.of(personModel);
  }
}

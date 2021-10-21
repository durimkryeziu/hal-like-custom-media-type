package com.example.hallikecustommediatype.person;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = IanaLinkRelations.ITEM_VALUE)
final class PersonModel extends RepresentationModel<PersonModel> {

  @JsonProperty
  private final String firstName;

  @JsonProperty
  private final String lastName;

  PersonModel(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public static PersonModel fromPerson(Person person) {
    return new PersonModel(person.firstName(), person.lastName());
  }
}

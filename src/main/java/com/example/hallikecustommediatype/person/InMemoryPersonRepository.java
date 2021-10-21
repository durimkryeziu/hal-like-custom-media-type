package com.example.hallikecustommediatype.person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryPersonRepository implements PersonRepository {

  private static final Map<UUID, Person> people = new HashMap<>();

  static {
    var person = new Person("John", "Doe");
    people.put(person.id(), person);
  }

  @Override
  public Person save(Person person) {
    people.put(person.id(), person);
    return person;
  }

  @Override
  public List<Person> findAll() {
    return new ArrayList<>(people.values());
  }

  @Override
  public Optional<Person> findById(UUID id) {
    return Optional.ofNullable(people.get(id));
  }
}

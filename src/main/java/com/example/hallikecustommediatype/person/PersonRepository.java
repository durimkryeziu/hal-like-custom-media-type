package com.example.hallikecustommediatype.person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonRepository {

  Person save(Person person);

  List<Person> findAll();

  Optional<Person> findById(UUID id);
}

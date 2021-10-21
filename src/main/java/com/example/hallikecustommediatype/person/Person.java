package com.example.hallikecustommediatype.person;

import com.example.hallikecustommediatype.image.Image;
import java.util.UUID;

public class Person {

  private final UUID id;
  private final String firstName;
  private final String lastName;
  private Image image;

  public Person(String firstName, String lastName) {
    this.id = UUID.randomUUID();
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public Person setImage(Image image) {
    this.image = image;
    return this;
  }

  public UUID id() {
    return id;
  }

  public String firstName() {
    return firstName;
  }

  public String lastName() {
    return lastName;
  }

  public Image image() {
    return image;
  }
}

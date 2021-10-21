package com.example.hallikecustommediatype.image;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ImageRepository {

  List<Image> findAll();

  Optional<Image> findById(UUID id);
}

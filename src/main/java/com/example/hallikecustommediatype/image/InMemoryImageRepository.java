package com.example.hallikecustommediatype.image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryImageRepository implements ImageRepository {

  private static final Map<UUID, Image> images = new HashMap<>();

  static {
    var image = new Image("https://picsum.photos/id/180/600/300");
    images.put(image.id(), image);
  }

  @Override
  public List<Image> findAll() {
    return new ArrayList<>(images.values());
  }

  @Override
  public Optional<Image> findById(UUID id) {
    return Optional.ofNullable(images.get(id));
  }
}

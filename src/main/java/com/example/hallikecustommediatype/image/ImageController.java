package com.example.hallikecustommediatype.image;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.hallikecustommediatype.ResourceLinksReplacer;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/images")
public class ImageController {

  private static final Logger log = LoggerFactory.getLogger(ImageController.class);

  private final ImageRepository imageRepository;
  private final ImageModelAssembler imageModelAssembler;

  public ImageController(ImageRepository imageRepository, ImageModelAssembler imageModelAssembler) {
    this.imageRepository = imageRepository;
    this.imageModelAssembler = imageModelAssembler;
  }

  @GetMapping
  public ResponseEntity<CollectionModel<ImageModel>> listAll() {
    var imageModels = this.imageRepository.findAll()
        .stream()
        .map(this.imageModelAssembler::toModel)
        .collect(toList());
    imageModels.forEach(model -> ResourceLinksReplacer.replaceResourceLinks(model,
        linkTo(methodOn(ImageController.class).getImage(model.id(), null, "HIGH")).withRel(IanaLinkRelations.SELF)));
    return ResponseEntity.ok(CollectionModel.of(imageModels, linkTo(methodOn(ImageController.class).listAll()).withSelfRel()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ImageModel> getImage(@PathVariable UUID id, @RequestParam(name = "category", required = false) String category,
                                             @RequestParam(name = "quality", required = false, defaultValue = "HIGH") String quality) {
    log.debug("Chosen category : {}; Quality: {}", category, quality);
    var imageModel = this.imageRepository.findById(id)
        .map(this.imageModelAssembler::toModel);
    return ResponseEntity.of(imageModel);
  }
}

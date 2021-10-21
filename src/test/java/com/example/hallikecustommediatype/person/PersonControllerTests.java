package com.example.hallikecustommediatype.person;

import static com.example.hallikecustommediatype.MockUriBuilder.mockTemplatedUri;
import static com.example.hallikecustommediatype.MockUriBuilder.mockUri;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.hallikecustommediatype.image.Image;
import com.example.hallikecustommediatype.image.ImageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@WebMvcTest(PersonController.class)
class PersonControllerTests implements ApplicationContextAware {

  @MockBean
  private ImageRepository imageRepository;

  @MockBean
  private PersonRepository personRepository;

  @Autowired
  private MockMvc mockMvc;

  private ObjectMapper hateoasObjectMapper;

  @Test
  void createPerson_GivenValidRequest_ShouldCreatePerson() throws Exception {
    var image = new Image("https://img.example.com");
    given(this.imageRepository.findById(any())).willReturn(Optional.of(image));
    var person = new Person("John", "Doe").setImage(image);
    given(this.personRepository.save(any())).willReturn(person);

    var request = new CreatePersonRequest("John", "Doe")
        .add(Link.of(mockTemplatedUri("/images/{id}?quality=HIGH{&category}", image.id()), "image"));

    this.mockMvc
        .perform(post("/api/people")
            .accept(MediaTypes.HAL_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(serialize(request)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(header().string(HttpHeaders.LOCATION, equalTo(mockUri("/people/{id}", person.id()))))
        .andExpect(jsonPath("$.firstName", equalTo(person.firstName())))
        .andExpect(jsonPath("$.lastName", equalTo(person.lastName())))
        .andExpect(jsonPath("$._links.length()", equalTo(3)))
        .andExpect(jsonPath("$._links.self.href", equalTo(mockUri("/people/{id}", person.id()))))
        .andExpect(jsonPath("$._links.image.href", equalTo(mockTemplatedUri("/images/{id}{?category,quality}", image.id()))))
        .andExpect(jsonPath("$._links.people.href", equalTo(mockUri("/people"))));
    verify(this.imageRepository).findById(image.id());
    var personCaptor = ArgumentCaptor.forClass(Person.class);
    verify(this.personRepository).save(personCaptor.capture());
    assertThat(personCaptor.getValue()).satisfies(it -> {
      assertThat(it.firstName()).isEqualTo(person.firstName());
      assertThat(it.lastName()).isEqualTo(person.lastName());
      assertThat(it.image()).isEqualTo(image);
    });
  }

  private String serialize(CreatePersonRequest value) throws JsonProcessingException {
    return this.hateoasObjectMapper.writeValueAsString(value);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    RequestMappingHandlerAdapter handlerAdapter = applicationContext.getBean(RequestMappingHandlerAdapter.class);
    this.hateoasObjectMapper = handlerAdapter.getMessageConverters().stream()
        .filter(it -> MappingJackson2HttpMessageConverter.class.equals(it.getClass()))
        .filter(it -> it.getSupportedMediaTypes(RepresentationModel.class).contains(MediaTypes.HAL_JSON))
        .map(MappingJackson2HttpMessageConverter.class::cast)
        .findFirst()
        .map(it -> it.getObjectMappersForType(RepresentationModel.class).get(MediaTypes.HAL_JSON))
        .orElse(null);
  }

  @TestConfiguration
  static class Config {

    @Bean
    PersonModelAssembler personModelAssembler() {
      return new PersonModelAssembler();
    }
  }
}
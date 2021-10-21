package com.example.hallikecustommediatype;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public final class MockUriBuilder {

  private static final String BASE_PATH = "http://localhost/api";

  private MockUriBuilder() {
  }

  public static String mockUri(String path, Object... vars) {
    return buildUriComponents(path, vars).encode().toUriString();
  }

  public static String mockTemplatedUri(String path, Object... vars) {
    return buildUriComponents(path, vars).toUriString();
  }

  private static UriComponents buildUriComponents(String path, Object... vars) {
    return UriComponentsBuilder.fromUriString(BASE_PATH + path).buildAndExpand(vars);
  }
}

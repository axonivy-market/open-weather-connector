package com.axonivy.connector.openweather.test;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.map.HashedMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

public class BaseAPITest {
  private static final String DEFAULT_WEATHER_DATA_URL = "https://api.openweathermap.org/data/2.5/forecast";
  private static final String DEFAULT_WEATHER_GEO_URL = "https://api.openweathermap.org/data/2.5/forecast";
  private static final String APP_ID_QUERRY_PARAM_KEY = "appId";
  protected static String appId;
  protected APIRequestContext request;
  private Playwright playwright;

  @BeforeAll
  void beforeAll() {
    createPlaywright();
    appId = System.getProperty("apiToken");
  }

  @AfterAll
  void afterAll() {
    disposeAPIRequestContext();
    closePlaywright();
  }

  void createPlaywright() {
    playwright = Playwright.create();
    createSimpleRequestContext();
  }

  void createSimpleRequestContext() {
    request = playwright.request().newContext();
  }

  private APIResponse getResponseFromUrlWithQueryParams(String url, RequestOptions queryOptions) {
    return request.get(url, queryOptions);
  }

  protected APIResponse getResponseFromDataUrlWithQueryParams(Map<String, String> queryParams) {
    return getResponseFromUrlWithQueryParams(DEFAULT_WEATHER_DATA_URL, createRequestOptionFromQueryParam(queryParams));
  }

  protected APIResponse getResponseFromGeoUrlWithQueryParams(Map<String, String> queryParams) {
    return getResponseFromUrlWithQueryParams(DEFAULT_WEATHER_GEO_URL, createRequestOptionFromQueryParam(queryParams));
  }

  private RequestOptions createRequestOptionFromQueryParam(Map<String, String> queryParams) {
    Objects.requireNonNullElse(queryParams, new HashedMap<>());
    RequestOptions requestOptions = RequestOptions.create();
    for (Map.Entry<String, String> entry : queryParams.entrySet()) {
      requestOptions.setQueryParam(entry.getKey(), entry.getValue());
    }
    return requestOptions;
  }

  protected Map<String, String> updateAppIdQueryParam(Map<String, String> params) {
    Objects.requireNonNullElse(params, new HashedMap<>());
    params.put(APP_ID_QUERRY_PARAM_KEY, appId);
    return params;
  }

  void disposeAPIRequestContext() {
    if (request != null) {
      request.dispose();
      request = null;
    }
  }

  void closePlaywright() {
    if (playwright != null) {
      playwright.close();
      playwright = null;
    }
  }
}
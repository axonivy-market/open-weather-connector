package com.axonivy.connector.openweather.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.microsoft.playwright.APIResponse;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ForecastWeatherApiTest extends BaseAPITest {
  private static final String NEW_YORK_LON_VALUE = "-74.0060152";
  private static final String NEW_YORK_LAT_VALUE = "40.7127281";
  private static final String LON_QUERRY_PARAM_KEY = "lon";
  private static final String LAT_QUERRY_PARAM_KEY = "lat";
  private static final String UNITS_QUERRY_PARAM_KEY = "units";
  private static final String METRIC_UNIT_VALUE = "metric";
  private static final String LOCATION_QUERRY_PARAM_KEY = "q";
  private static final String LIMIT_QUERRY_PARAM_KEY = "limit";
  private static final String NEW_YORK_LOCATION_VALUE = "New York";

  @Test
  void shouldReturnOkStatusWhenCallGetNewYorkData() {
    APIResponse repsonse = getResponseFromDataUrlWithQueryParams(createNewYorkDataParam());
    assertTrue(repsonse.ok());
  }
  
  @Test
  void shouldReturnNewYorkInResponseWhenCallGetNewYorkGeo() {
    APIResponse repsonse = getResponseFromGeoUrlWithQueryParams(createNewYorkLocationParam());
    assertTrue(repsonse.ok());
    // This test case should be failed
    assertEquals("New York County", repsonse.toString());
  }

  private Map<String, String> createNewYorkDataParam() {
    Map<String, String> params = new HashMap<String, String>();
    params.put(LON_QUERRY_PARAM_KEY, NEW_YORK_LON_VALUE);
    params.put(LAT_QUERRY_PARAM_KEY, NEW_YORK_LAT_VALUE);
    params.put(UNITS_QUERRY_PARAM_KEY, METRIC_UNIT_VALUE);
    updateAppIdQueryParam(params);
    return params;
  }
  
  private Map<String, String> createNewYorkLocationParam() {
    Map<String, String> params = new HashMap<String, String>();
    params.put(LOCATION_QUERRY_PARAM_KEY, NEW_YORK_LOCATION_VALUE);
    params.put(LIMIT_QUERRY_PARAM_KEY, "1");
    updateAppIdQueryParam(params);
    return params;
  }
}

package com.axonivy.connector.openweather.test.utils;

import java.io.IOException;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import com.axonivy.connector.openweather.test.constant.OpenWeatherCommonConstants;
import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.bpm.engine.client.BpmClient;
import ch.ivyteam.ivy.bpm.engine.client.ExecContext;
import ch.ivyteam.ivy.bpm.engine.client.element.BpmProcess;
import ch.ivyteam.ivy.bpm.engine.client.sub.SubRequestBuilder;
import ch.ivyteam.ivy.environment.AppFixture;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.rest.client.RestClient;
import ch.ivyteam.ivy.rest.client.RestClients;
import ch.ivyteam.ivy.server.internal.test.AppFixtureJu5Context;

public class OpenWeatherUtils {
  private static final String APP_ID_KEY = "appId";
  private static final String FEATURE_SUFFIX = ".Features";
  private static final String REST_CLIENT_PREFIX = "RestClients.";
  private static final String WEATHER_GEO_URL_KEY = "weatherGeoUrl";
  private static final String APP_ID_PROP_KEY = "AUTH." + APP_ID_KEY;
  private static final String WEATHER_DATA_URL_KEY = "weatherDataUrl";
  private static final String LOCAL_CREDENTIALS_FILE_PATH = "credentials.properties";
  private static final String OPEN_WEATHER_CONNECTOR_PREFIX = "openWeatherConnector.";
  private static final String JSON_FEATURES = "ch.ivyteam.ivy.rest.client.mapper.JsonFeature";
  private static final String WEATHER_DATA_REST_CLIENT_NAME = "WeatherData (Openweathermap weather API)";
  private static final String GEO_DATA_REST_CLIENT_NAME = "GeocodingCoordinates (Openweathermap geocoding API)";
  private static final String WEATHER_DATA_MOCK_ENDPOINT = "{ivy.app.baseurl}/api/weatherDataMock";
  private static final String WEATHER_GEO_MOCK_ENDPOINT = "{ivy.app.baseurl}/api/weatherGeoMock";
  private static BpmClient testClient = new BpmClient(new ExecContext(IApplication.current()));

  public static BpmClient getTestBpmClient() {
    return testClient;
  }

  @SuppressWarnings("restriction")
  public static void setUpConfigForMockServer(ExtensionContext context) {
    AppFixture fixture = AppFixtureJu5Context.get(context).getFixture();
    // Disable OAuth feature for mock rest service
    fixture.config(REST_CLIENT_PREFIX + WEATHER_DATA_REST_CLIENT_NAME + FEATURE_SUFFIX, JSON_FEATURES);
    fixture.config(REST_CLIENT_PREFIX + GEO_DATA_REST_CLIENT_NAME + FEATURE_SUFFIX, JSON_FEATURES);
    fixture.var(OPEN_WEATHER_CONNECTOR_PREFIX + WEATHER_DATA_URL_KEY, WEATHER_DATA_MOCK_ENDPOINT);
    fixture.var(OPEN_WEATHER_CONNECTOR_PREFIX + WEATHER_GEO_URL_KEY, WEATHER_GEO_MOCK_ENDPOINT);
  }

  public static void setUpConfigForContext(ExtensionContext context) {
    switch (context.getDisplayName()) {
    case OpenWeatherCommonConstants.REAL_CALL_CONTEXT_DISPLAY_NAME:
      setUpConfigForRestCallTest();
      break;
    case OpenWeatherCommonConstants.MOCK_SERVER_CONTEXT_DISPLAY_NAME:
      setUpConfigForMockServer(context);
      break;
    default:
      break;
    }
  }

  private static void setupClientWithNameAndUrl(RestClients clients, String clientName, String clientDefaultUri,
      String appIdValue) {
    RestClient client = clients.find(clientName);
    Ivy.log().warn("client == null: "+client == null);
    client = client.toBuilder().uri(clientDefaultUri).property(APP_ID_PROP_KEY, appIdValue).toRestClient();
    clients.set(client);
  }

  public static void setUpConfigForRestCallTest() {
    String appId = System.getProperty(APP_ID_KEY);
    String weatherDataUrl = System.getProperty(WEATHER_DATA_URL_KEY);
    String weatherGeoUrl = System.getProperty(WEATHER_GEO_URL_KEY);


    // Local setup for testing
    if (StringUtils.isAnyBlank(new String[] { appId, weatherDataUrl, weatherGeoUrl })) {
      try (var in = OpenWeatherUtils.class.getResourceAsStream(LOCAL_CREDENTIALS_FILE_PATH)) {
        if (in != null) {
          Properties props = new Properties();
          props.load(in);
          appId = (String) props.get(APP_ID_KEY);
          weatherDataUrl = (String) props.get(WEATHER_DATA_URL_KEY);
          weatherGeoUrl = (String) props.get(WEATHER_GEO_URL_KEY);
        }
      } catch (IOException e) {
        Ivy.log().warn("Can't get credential from local file with path: {0}", LOCAL_CREDENTIALS_FILE_PATH);
      }
    }

    RestClients clients = RestClients.of(IApplication.current());
    setupClientWithNameAndUrl(clients, WEATHER_DATA_REST_CLIENT_NAME, weatherDataUrl, appId);
    setupClientWithNameAndUrl(clients, GEO_DATA_REST_CLIENT_NAME, weatherGeoUrl, appId);
  }

  public static SubRequestBuilder getSubProcessWithNameAndPath(String subProcessPath, String subProcessName) {
    return testClient.start().subProcess(BpmProcess.path(subProcessPath).elementName(subProcessName));
  }
}

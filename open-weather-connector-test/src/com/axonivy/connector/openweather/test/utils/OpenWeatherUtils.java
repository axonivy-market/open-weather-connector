package com.axonivy.connector.openweather.test.utils;

import java.io.IOException;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.bpm.engine.client.BpmClient;
import ch.ivyteam.ivy.bpm.engine.client.element.BpmProcess;
import ch.ivyteam.ivy.bpm.engine.client.sub.SubRequestBuilder;
import ch.ivyteam.ivy.environment.AppFixture;
import ch.ivyteam.ivy.rest.client.RestClient;
import ch.ivyteam.ivy.rest.client.RestClients;

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

	public static void setUpConfigForMockServer(AppFixture fixture) {
		// Disable OAuth feature for mock rest service
		fixture.config(REST_CLIENT_PREFIX + WEATHER_DATA_REST_CLIENT_NAME + FEATURE_SUFFIX, JSON_FEATURES);
		fixture.config(REST_CLIENT_PREFIX + GEO_DATA_REST_CLIENT_NAME + FEATURE_SUFFIX, JSON_FEATURES);
		fixture.var(OPEN_WEATHER_CONNECTOR_PREFIX + WEATHER_DATA_URL_KEY, "{ivy.app.baseurl}/api/weatherDataMock");
		fixture.var(OPEN_WEATHER_CONNECTOR_PREFIX + WEATHER_GEO_URL_KEY, "{ivy.app.baseurl}/api/weatherGeoMock");
	}

	private static void setupClientWithNameAndUrl(RestClients clients, String clientName, String clientDefaultUri,
			String appIdValue) {
		RestClient client = clients.find(clientName);
		client = client.toBuilder().uri(clientDefaultUri).property(APP_ID_PROP_KEY, appIdValue).toRestClient();
		clients.set(client);
	}

	public static void setUpConfigForRestCallTest() throws IOException {
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
			}
		}

		RestClients clients = RestClients.of(IApplication.current());
		setupClientWithNameAndUrl(clients, WEATHER_DATA_REST_CLIENT_NAME, weatherDataUrl, appId);
		setupClientWithNameAndUrl(clients, GEO_DATA_REST_CLIENT_NAME, weatherGeoUrl, appId);
	}

	public static SubRequestBuilder getSubProcessWithNameAndPath(BpmClient bpmClient, String subProcessPath,
			String subProcessName) {
		return bpmClient.start().subProcess(BpmProcess.path(subProcessPath).elementName(subProcessName));
	}
}

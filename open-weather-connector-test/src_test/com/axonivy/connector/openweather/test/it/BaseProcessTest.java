package com.axonivy.connector.openweather.test.it;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.bpm.engine.client.BpmClient;
import ch.ivyteam.ivy.bpm.engine.client.element.BpmProcess;
import ch.ivyteam.ivy.bpm.engine.client.sub.SubRequestBuilder;
import ch.ivyteam.ivy.bpm.exec.client.IvyProcessTest;
import ch.ivyteam.ivy.environment.AppFixture;
import ch.ivyteam.ivy.rest.client.RestClient;
import ch.ivyteam.ivy.rest.client.RestClients;

@IvyProcessTest(enableWebServer = true)
public class BaseProcessTest {
	private static final String APP_ID_KEY = "appId";
	private static final String APP_ID_PROP_KEY = "AUTH." + APP_ID_KEY;
	private static final String WEATHER_DATA_URL_KEY = "weatherDataUrl";
	private static final String WEATHER_GEO_URL_KEY = "weatherGeoUrl";
	private static final String LOCAL_CREDENTIALS_FILE_PATH = "credentials.properties";
	private static final String WEATHER_DATA_REST_CLIENT_NAME = "WeatherData (Openweathermap weather API)";
	private static final String GEO_DATA_REST_CLIENT_NAME = "GeocodingCoordinates (Openweathermap geocoding API)";
	protected static final String RESULT_KEY = "result";
	protected static final String RESULTS_KEY = "results";
	protected static final Double TEST_LON_VALUE = 40.7484;
	protected static final Double TEST_LAT_VALUE = -73.9967;
	protected static final String TEST_ZIPCODE_VALUE = "10001";
	private static String appId;
	private static String weatherDataUrl;
	private static String weatherGeoUrl;

	@BeforeEach
	void beforeAll(AppFixture fixture, IApplication app) throws IOException {
		setupConfig(fixture, app);
	}

	private static void setupClientWithNameAndUrl(RestClients clients, String clientName, String clientDefaultUri) {
		RestClient client = clients.find(clientName);
		client = client.toBuilder().uri(clientDefaultUri).property(APP_ID_PROP_KEY, appId).toRestClient();
		clients.set(client);
	}

	protected SubRequestBuilder getSubProcessWithNameAndPath(BpmClient bpmClient, String subProcessPath,
			String subProcessName) {
		return bpmClient.start().subProcess(BpmProcess.path(subProcessPath).elementName(subProcessName));
	}

	static void setupConfig(AppFixture fixture, IApplication app) throws IOException {
		appId = System.getProperty(APP_ID_KEY);
		weatherDataUrl = System.getProperty(WEATHER_DATA_URL_KEY);
		weatherGeoUrl = System.getProperty(WEATHER_GEO_URL_KEY);

		// Local setup for testing
		if (StringUtils.isAnyBlank(new String[] { appId, weatherDataUrl, weatherGeoUrl })) {
			try (var in = BaseProcessTest.class.getResourceAsStream(LOCAL_CREDENTIALS_FILE_PATH)) {
				if (in != null) {
					Properties props = new Properties();
					props.load(in);
					appId = (String) props.get(APP_ID_KEY);
					weatherDataUrl = (String) props.get(WEATHER_DATA_URL_KEY);
					weatherGeoUrl = (String) props.get(WEATHER_GEO_URL_KEY);
				}
			}
		}

		RestClients clients = RestClients.of(app);
		setupClientWithNameAndUrl(clients, WEATHER_DATA_REST_CLIENT_NAME, weatherDataUrl);
		setupClientWithNameAndUrl(clients, GEO_DATA_REST_CLIENT_NAME, weatherGeoUrl);
	}
}
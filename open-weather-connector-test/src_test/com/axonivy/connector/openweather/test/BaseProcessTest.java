package com.axonivy.connector.openweather.test;

import org.junit.jupiter.api.BeforeEach;

import ch.ivyteam.ivy.bpm.exec.client.IvyProcessTest;
import ch.ivyteam.ivy.environment.AppFixture;

@IvyProcessTest
public class BaseProcessTest {

	@BeforeEach
	void beforeEach(AppFixture fixture) {
		// Disable OAuth feature for mock rest service
		fixture.config("RestClients.WeatherData (Openweathermap weather API).Features",
				"ch.ivyteam.ivy.rest.client.mapper.JsonFeature");
		fixture.config("RestClients.GeocodingCoordinates (Openweathermap geocoding API).Features",
				"ch.ivyteam.ivy.rest.client.mapper.JsonFeature");
		fixture.var("openWeatherConnector.weatherDataUrl", "http://localhost:8081/designer/api/weatherDataMock");
		fixture.var("openWeatherConnector.weatherGeoUrl", "http://localhost:8081/designer/api/weatherGeoMock");
	}
}
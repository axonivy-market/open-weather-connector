package com.axonivy.connector.openweather.test.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.Duration;
import java.time.OffsetDateTime;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openweathermap.api.data2_5.client.AirPollution;

import com.axonivy.connector.openweather.test.utils.OpenWeatherUtils;

import ch.ivyteam.ivy.bpm.engine.client.BpmClient;
import ch.ivyteam.ivy.bpm.engine.client.ExecutionResult;
import ch.ivyteam.ivy.bpm.error.BpmError;
import ch.ivyteam.ivy.bpm.exec.client.IvyProcessTest;

@IvyProcessTest
public class AirPollutionProcessTest {
	private final String GET_AIR_POLLUTION_PROCESS_PATH = "connector/AirPollution";
	private final String GET_AIR_POLLUTION_BY_GEOCODE_SIGNATURE = "getCurrentAirPollution(Double,Double)";
	private final String GET_FORECAST_AIR_POLLUTION_BY_GEOCODE_SIGNATURE = "getForecastAirPollution(Double,Double)";
	private final String GET_HISTORICAL_AIR_POLLUTION_BY_GEOCODE_SIGNATURE = "getHistoricalAirPollution(Double,Double,OffsetDateTime,OffsetDateTime)";
	private final String RESULT_KEY = "result";
	private final Double TEST_LON_VALUE = 40.7484;
	private final Double TEST_LAT_VALUE = -73.9967;

	@BeforeEach
	void beforeEach() throws IOException {
		OpenWeatherUtils.setUpConfigForRestCallTest();
	}

	@Test
	public void testGetAirPollutionByGeoCode_ReturnsAirPollution(BpmClient bpmClient) throws NoSuchFieldException {
		ExecutionResult result = OpenWeatherUtils.getSubProcessWithNameAndPath(bpmClient,
				GET_AIR_POLLUTION_PROCESS_PATH, GET_AIR_POLLUTION_BY_GEOCODE_SIGNATURE)
				.execute(TEST_LON_VALUE, TEST_LAT_VALUE);
		var object = result.data().last().get(RESULT_KEY);
		assertThat(object).isInstanceOf(AirPollution.class);
	}

	@Test()
	public void testGetAirPollutionByGeoCode_ThrowsBpmException(BpmClient bpmClient) throws NoSuchFieldException {
		try {
			OpenWeatherUtils.getSubProcessWithNameAndPath(bpmClient, GET_AIR_POLLUTION_PROCESS_PATH,
					GET_AIR_POLLUTION_BY_GEOCODE_SIGNATURE).execute(null, null);
		} catch (BpmError e) {
			assertThat(e.getHttpStatusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
		}
	}

	@Test
	public void testGetForecastAirPollutionByGeoCode_ReturnsAirPollution(BpmClient bpmClient)
			throws NoSuchFieldException {
		ExecutionResult result = OpenWeatherUtils.getSubProcessWithNameAndPath(bpmClient,
				GET_AIR_POLLUTION_PROCESS_PATH, GET_FORECAST_AIR_POLLUTION_BY_GEOCODE_SIGNATURE)
				.execute(TEST_LON_VALUE, TEST_LAT_VALUE);
		var object = result.data().last().get(RESULT_KEY);
		assertThat(object).isInstanceOf(AirPollution.class);
	}

	@Test()
	public void testGetForecastAirPollutionByGeoCode_ThrowsBpmException(BpmClient bpmClient)
			throws NoSuchFieldException {
		try {
			OpenWeatherUtils.getSubProcessWithNameAndPath(bpmClient, GET_AIR_POLLUTION_PROCESS_PATH,
					GET_FORECAST_AIR_POLLUTION_BY_GEOCODE_SIGNATURE).execute(null, null);
		} catch (BpmError e) {
			assertThat(e.getHttpStatusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
		}
	}

	@Test
	public void testGetHistoricalAirPollutionByGeoCode_ReturnsAirPollution(BpmClient bpmClient)
			throws NoSuchFieldException {
		OffsetDateTime now = OffsetDateTime.now();
		OffsetDateTime twoDaysLater = now.plus(Duration.ofDays(2));
		ExecutionResult result = OpenWeatherUtils
				.getSubProcessWithNameAndPath(bpmClient, GET_AIR_POLLUTION_PROCESS_PATH,
						GET_HISTORICAL_AIR_POLLUTION_BY_GEOCODE_SIGNATURE)
				.execute(TEST_LON_VALUE, TEST_LAT_VALUE, now, twoDaysLater);
		var object = result.data().last().get(RESULT_KEY);
		assertThat(object).isInstanceOf(AirPollution.class);
	}

	@Test()
	public void testGetHistoricalAirPollutionByGeoCode_ThrowsBpmExceptionCanNotGeo(BpmClient bpmClient)
			throws NoSuchFieldException {
		OffsetDateTime now = OffsetDateTime.now();
		OffsetDateTime twoDaysLater = now.plus(Duration.ofDays(2));
		try {
			OpenWeatherUtils.getSubProcessWithNameAndPath(bpmClient, GET_AIR_POLLUTION_PROCESS_PATH,
					GET_HISTORICAL_AIR_POLLUTION_BY_GEOCODE_SIGNATURE).execute(null, null, now, twoDaysLater);
		} catch (BpmError e) {
			assertThat(e.getHttpStatusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
		}
	}

	@Test()
	public void testGetHistoricalAirPollutionByGeoCode_ThrowsBpmExceptionStartMoreThanEnd(BpmClient bpmClient)
			throws NoSuchFieldException {
		OffsetDateTime now = OffsetDateTime.now();
		OffsetDateTime twoDaysLater = now.plus(Duration.ofDays(2));
		try {
			OpenWeatherUtils
					.getSubProcessWithNameAndPath(bpmClient, GET_AIR_POLLUTION_PROCESS_PATH,
							GET_HISTORICAL_AIR_POLLUTION_BY_GEOCODE_SIGNATURE)
					.execute(TEST_LON_VALUE, TEST_LAT_VALUE, twoDaysLater, now);
		} catch (BpmError e) {
			assertThat(e.getHttpStatusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
		}
	}
}

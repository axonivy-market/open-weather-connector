package com.axonivy.connector.openweather.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.OffsetDateTime;

import org.junit.jupiter.api.Test;
import org.openweathermap.api.data2_5.client.AirPollution;

import ch.ivyteam.ivy.bpm.engine.client.BpmClient;
import ch.ivyteam.ivy.bpm.engine.client.ExecutionResult;
import ch.ivyteam.ivy.bpm.error.BpmError;

public class AirPollutionProcessTest extends BaseProcessTest {
	private static final String GET_AIR_POLLUTION_PROCESS_PATH = "connector/AirPollution";
	private static final String GET_AIR_POLLUTION_BY_GEOCODE_SIGNATURE = "getCurrentAirPollution(Double,Double)";
	private static final String GET_FORECAST_AIR_POLLUTION_BY_GEOCODE_SIGNATURE = "getForecastAirPollution(Double,Double)";
	private static final String GET_HISTORICAL_AIR_POLLUTION_BY_GEOCODE_SIGNATURE = "getHistoricalAirPollution(Double,Double,OffsetDateTime,OffsetDateTime)";

	@Test
	public void testGetAirPollutionByGeoCode_ReturnsAirPollution(BpmClient bpmClient) throws NoSuchFieldException {
		ExecutionResult result = getSubProcessWithNameAndPath(bpmClient, GET_AIR_POLLUTION_PROCESS_PATH,
				GET_AIR_POLLUTION_BY_GEOCODE_SIGNATURE).execute(40.7127281, -74.0060152);
		var object = result.data().last().get("result");
		assertThat(object).isInstanceOf(AirPollution.class);
	}

	@Test()
	public void testGetAirPollutionByGeoCode_ThrowsBpmException(BpmClient bpmClient) throws NoSuchFieldException {
		try {
			getSubProcessWithNameAndPath(bpmClient, GET_AIR_POLLUTION_PROCESS_PATH,
					GET_AIR_POLLUTION_BY_GEOCODE_SIGNATURE).execute(null, null);
		} catch (BpmError e) {
			assertThat(e.getHttpStatusCode()).isEqualTo(400);
		}
	}

	@Test
	public void testGetForecastAirPollutionByGeoCode_ReturnsAirPollution(BpmClient bpmClient)
			throws NoSuchFieldException {
		ExecutionResult result = getSubProcessWithNameAndPath(bpmClient, GET_AIR_POLLUTION_PROCESS_PATH,
				GET_FORECAST_AIR_POLLUTION_BY_GEOCODE_SIGNATURE).execute(40.7127281, -74.0060152);
		var object = result.data().last().get("result");
		assertThat(object).isInstanceOf(AirPollution.class);
	}

	@Test()
	public void testGetForecastAirPollutionByGeoCode_ThrowsBpmException(BpmClient bpmClient)
			throws NoSuchFieldException {
		try {
			getSubProcessWithNameAndPath(bpmClient, GET_AIR_POLLUTION_PROCESS_PATH,
					GET_FORECAST_AIR_POLLUTION_BY_GEOCODE_SIGNATURE).execute(null, null);
		} catch (BpmError e) {
			assertThat(e.getHttpStatusCode()).isEqualTo(400);
		}
	}

	@Test
	public void testGetHistoricalAirPollutionByGeoCode_ReturnsAirPollution(BpmClient bpmClient)
			throws NoSuchFieldException {
		OffsetDateTime now = OffsetDateTime.now();
		OffsetDateTime twoDaysLater = now.plus(Duration.ofDays(2));
		ExecutionResult result = getSubProcessWithNameAndPath(bpmClient, GET_AIR_POLLUTION_PROCESS_PATH,
				GET_HISTORICAL_AIR_POLLUTION_BY_GEOCODE_SIGNATURE).execute(40.7127281, -74.0060152, now, twoDaysLater);
		var object = result.data().last().get("result");
		assertThat(object).isInstanceOf(AirPollution.class);
	}

	@Test()
	public void testGetHistoricalAirPollutionByGeoCode_ThrowsBpmExceptionCanNotGeo(BpmClient bpmClient)
			throws NoSuchFieldException {
		OffsetDateTime now = OffsetDateTime.now();
		OffsetDateTime twoDaysLater = now.plus(Duration.ofDays(2));
		try {
			getSubProcessWithNameAndPath(bpmClient, GET_AIR_POLLUTION_PROCESS_PATH,
					GET_HISTORICAL_AIR_POLLUTION_BY_GEOCODE_SIGNATURE).execute(null, null, now, twoDaysLater);
		} catch (BpmError e) {
			assertThat(e.getHttpStatusCode()).isEqualTo(400);
		}
	}

	@Test()
	public void testGetHistoricalAirPollutionByGeoCode_ThrowsBpmExceptionStartMoreThanEnd(BpmClient bpmClient)
			throws NoSuchFieldException {
		OffsetDateTime now = OffsetDateTime.now();
		OffsetDateTime twoDaysLater = now.plus(Duration.ofDays(2));
		try {
			getSubProcessWithNameAndPath(bpmClient, GET_AIR_POLLUTION_PROCESS_PATH,
					GET_HISTORICAL_AIR_POLLUTION_BY_GEOCODE_SIGNATURE)
					.execute(40.7127281, -74.0060152, twoDaysLater, now);
		} catch (BpmError e) {
			assertThat(e.getHttpStatusCode()).isEqualTo(400);
		}
	}
}

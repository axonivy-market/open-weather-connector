package com.axonivy.connector.openweather.test.it;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.openweathermap.api.data2_5.client.Forecast;

import ch.ivyteam.ivy.bpm.engine.client.BpmClient;
import ch.ivyteam.ivy.bpm.engine.client.ExecutionResult;
import ch.ivyteam.ivy.bpm.error.BpmError;

public class ForecastWeatherProcessTest extends BaseProcessTest {

	private static final String GET_FORECAST_PROCESS_PATH = "connector/ForecastWeather";
	private static final String GET_FORECAST_BY_GEOCODE_SIGNATURE = "getForecastWeather(Double,Double,Integer,String,String)";

	@Test
	public void testGetForecastWeatherByGeoCode_ReturnsForecast(BpmClient bpmClient) throws NoSuchFieldException {
		ExecutionResult result = getSubProcessWithNameAndPath(bpmClient, GET_FORECAST_PROCESS_PATH,
				GET_FORECAST_BY_GEOCODE_SIGNATURE)
				.execute(TEST_LON_VALUE, TEST_LAT_VALUE, 1, StringUtils.EMPTY, StringUtils.EMPTY);
		var object = result.data().last().get(RESULT_KEY);
		assertThat(object).isInstanceOf(Forecast.class);
	}

	@Test()
	public void testGetForecastByGeoCode_ThrowsBpmException(BpmClient bpmClient) throws NoSuchFieldException {
		try {
			getSubProcessWithNameAndPath(bpmClient, GET_FORECAST_PROCESS_PATH, GET_FORECAST_BY_GEOCODE_SIGNATURE)
					.execute(null, null, 1, StringUtils.EMPTY, StringUtils.EMPTY);
		} catch (BpmError e) {
			assertThat(e.getHttpStatusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
		}
	}
}

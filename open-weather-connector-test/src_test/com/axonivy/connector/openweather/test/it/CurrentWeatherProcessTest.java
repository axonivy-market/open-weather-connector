package com.axonivy.connector.openweather.test.it;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.openweathermap.api.data2_5.client.Current;

import ch.ivyteam.ivy.bpm.engine.client.BpmClient;
import ch.ivyteam.ivy.bpm.engine.client.ExecutionResult;
import ch.ivyteam.ivy.bpm.error.BpmError;

public class CurrentWeatherProcessTest extends BaseProcessTest {
	private static final String GET_CURRENT_WEATHER_PROCESS_PATH = "connector/CurrentWeather";
	private static final String GET_CURRENT_WEATHER_BY_GEOCODE_SIGNATURE = "getCurrentWeather(Double,Double,String,String)";

	@Test
	public void testGetCurrentWeatherByGeoCode_ReturnsCurrentWeather(BpmClient bpmClient) throws NoSuchFieldException {
		ExecutionResult result = getSubProcessWithNameAndPath(bpmClient, GET_CURRENT_WEATHER_PROCESS_PATH,
				GET_CURRENT_WEATHER_BY_GEOCODE_SIGNATURE)
				.execute(40.7127281, -74.0060152, StringUtils.EMPTY, StringUtils.EMPTY);
		var object = result.data().last().get("result");
		assertThat(object).isInstanceOf(Current.class);
	}

	@Test()
	public void testGetCurrentWeatherByGeoCode_ThrowsBpmException(BpmClient bpmClient) throws NoSuchFieldException {
		try {
			getSubProcessWithNameAndPath(bpmClient, GET_CURRENT_WEATHER_PROCESS_PATH,
					GET_CURRENT_WEATHER_BY_GEOCODE_SIGNATURE).execute(null, null, StringUtils.EMPTY, StringUtils.EMPTY);
		} catch (BpmError e) {
			assertThat(e.getHttpStatusCode()).isEqualTo(400);
		}
	}
}

package com.axonivy.connector.openweather.test.it;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openweathermap.api.data2_5.client.Current;
import com.axonivy.connector.openweather.test.utils.OpenWeatherUtils;
import ch.ivyteam.ivy.bpm.engine.client.BpmClient;
import ch.ivyteam.ivy.bpm.engine.client.ExecutionResult;
import ch.ivyteam.ivy.bpm.error.BpmError;
import ch.ivyteam.ivy.bpm.exec.client.IvyProcessTest;

@IvyProcessTest
public class CurrentWeatherProcessTest {
	private final String GET_CURRENT_WEATHER_PROCESS_PATH = "connector/CurrentWeather";
	private final String GET_CURRENT_WEATHER_BY_GEOCODE_SIGNATURE = "getCurrentWeather(Double,Double,String,String)";
	private final String RESULT_KEY = "result";
	private final Double TEST_LON_VALUE = 40.7484;
	private final Double TEST_LAT_VALUE = -73.9967;

	@BeforeEach
	void beforeEach() throws IOException {
		OpenWeatherUtils.setUpConfigForRestCallTest();
	}

	@Test
	public void testGetCurrentWeatherByGeoCode_ReturnsCurrentWeather(BpmClient bpmClient) throws NoSuchFieldException {
		ExecutionResult result = OpenWeatherUtils
				.getSubProcessWithNameAndPath(bpmClient, GET_CURRENT_WEATHER_PROCESS_PATH,
						GET_CURRENT_WEATHER_BY_GEOCODE_SIGNATURE)
				.execute(TEST_LON_VALUE, TEST_LAT_VALUE, StringUtils.EMPTY, StringUtils.EMPTY);
		var object = result.data().last().get(RESULT_KEY);
		assertThat(object).isInstanceOf(Current.class);
	}

	@Test()
	public void testGetCurrentWeatherByGeoCode_ThrowsBpmException(BpmClient bpmClient) throws NoSuchFieldException {
		try {
			OpenWeatherUtils
					.getSubProcessWithNameAndPath(bpmClient, GET_CURRENT_WEATHER_PROCESS_PATH,
							GET_CURRENT_WEATHER_BY_GEOCODE_SIGNATURE)
					.execute(null, null, StringUtils.EMPTY, StringUtils.EMPTY);
		} catch (BpmError e) {
			assertThat(e.getHttpStatusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
		}
	}
}

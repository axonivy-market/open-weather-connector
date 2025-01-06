package com.axonivy.connector.openweather.test;

import static org.assertj.core.api.Assertions.assertThat;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openweathermap.api.data2_5.client.Current;
import com.axonivy.connector.openweather.test.context.CustomInvocationContextProvider;
import com.axonivy.connector.openweather.test.utils.OpenWeatherUtils;
import ch.ivyteam.ivy.bpm.engine.client.ExecutionResult;
import ch.ivyteam.ivy.bpm.error.BpmError;
import ch.ivyteam.ivy.bpm.exec.client.IvyProcessTest;
import static com.axonivy.connector.openweather.test.constant.OpenWeatherCommonConstants.GET_CURRENT_WEATHER_PROCESS_PATH;
import static com.axonivy.connector.openweather.test.constant.OpenWeatherCommonConstants.RESULT_KEY;
import static com.axonivy.connector.openweather.test.constant.OpenWeatherCommonConstants.GET_CURRENT_WEATHER_BY_GEOCODE_SIGNATURE;

@IvyProcessTest(enableWebServer = true)
@ExtendWith(CustomInvocationContextProvider.class)
public class CurrentWeatherProcessTest {

  private final Double TEST_LON_VALUE = 40.7484;
  private final Double TEST_LAT_VALUE = -73.9967;

  @BeforeEach
  void beforeEach(ExtensionContext context) {
    OpenWeatherUtils.setUpConfigForContext(context);
  }

  @TestTemplate
  public void testGetCurrentWeatherByGeoCode_ReturnsCurrentWeather() throws NoSuchFieldException {
    ExecutionResult result = OpenWeatherUtils
        .getSubProcessWithNameAndPath(GET_CURRENT_WEATHER_PROCESS_PATH, GET_CURRENT_WEATHER_BY_GEOCODE_SIGNATURE)
        .execute(TEST_LON_VALUE, TEST_LAT_VALUE, StringUtils.EMPTY, StringUtils.EMPTY);
    var object = result.data().last().get(RESULT_KEY);
    assertThat(object).isInstanceOf(Current.class);
  }

  @TestTemplate
  public void testGetCurrentWeatherByGeoCode_ThrowsBpmException() throws NoSuchFieldException {
    try {
      OpenWeatherUtils
          .getSubProcessWithNameAndPath(GET_CURRENT_WEATHER_PROCESS_PATH, GET_CURRENT_WEATHER_BY_GEOCODE_SIGNATURE)
          .execute(null, null, StringUtils.EMPTY, StringUtils.EMPTY);
    } catch (BpmError e) {
      assertThat(e.getHttpStatusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
    }
  }
}

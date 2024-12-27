package com.axonivy.connector.openweather.test.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.openweathermap.api.geo1_0.client.GeoLocation;

import ch.ivyteam.ivy.bpm.engine.client.BpmClient;
import ch.ivyteam.ivy.bpm.engine.client.ExecutionResult;
import ch.ivyteam.ivy.bpm.error.BpmError;

public class GeocodingLocationProcessTest extends BaseProcessTest {

	private static final String GEOCODING_LOCATION_PROCESS_PATH = "connector/GeocodingLocation";
	private static final String GEOCODING_LOCATION_BY_NAME_SIGNATURE = "getCoordinatesByLocationName(String,String,String,Integer)";
	private static final String GEOCODING_LOCATION_BY_ZIP_CODE_SIGNATURE = "getCoordinatesByZipCode(String,String)";
	private static final String GEOCODING_LOCATION_REVERSE_SIGNATURE = "reverse(Double,Double,Integer)";

	@Test
	public void testGeocodingByName_ReturnsListOfGeoLocations(BpmClient bpmClient) throws NoSuchFieldException {
		ExecutionResult result = getSubProcessWithNameAndPath(bpmClient, GEOCODING_LOCATION_PROCESS_PATH,
				GEOCODING_LOCATION_BY_NAME_SIGNATURE).execute("London", StringUtils.EMPTY, StringUtils.EMPTY, 1);
		var object = result.data().last().get(RESULTS_KEY);
		assertThat(object).isInstanceOf(List.class);
		var objects = (ArrayList<?>) object;
		assertThat(objects).isNotEmpty();
		assertThat(objects.get(0)).isInstanceOf(GeoLocation.class);
	}

	@Test()
	public void testGeocodingByName_ThrowsBpmException(BpmClient bpmClient) throws NoSuchFieldException {
		try {
			getSubProcessWithNameAndPath(bpmClient, GEOCODING_LOCATION_PROCESS_PATH,
					GEOCODING_LOCATION_BY_NAME_SIGNATURE)
					.execute(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, 1);
		} catch (BpmError e) {
			assertThat(e.getHttpStatusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
		}
	}

	@Test
	public void testGeocodingByZip_ReturnsGeoLocation(BpmClient bpmClient) throws NoSuchFieldException {
		ExecutionResult result = getSubProcessWithNameAndPath(bpmClient, GEOCODING_LOCATION_PROCESS_PATH,
				GEOCODING_LOCATION_BY_ZIP_CODE_SIGNATURE).execute(TEST_ZIPCODE_VALUE, StringUtils.EMPTY);
		var object = result.data().last().get(RESULT_KEY);
		assertThat(object).isInstanceOf(GeoLocation.class);
	}

	@Test()
	public void testGeocodingByZip_ThrowsBpmException(BpmClient bpmClient) throws NoSuchFieldException {
		try {
			getSubProcessWithNameAndPath(bpmClient, GEOCODING_LOCATION_PROCESS_PATH,
					GEOCODING_LOCATION_BY_ZIP_CODE_SIGNATURE).execute(StringUtils.EMPTY, StringUtils.EMPTY);
		} catch (BpmError e) {
			assertThat(e.getHttpStatusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
		}
	}

	@Test
	public void testReverse_ReturnsListOfGeoLocations(BpmClient bpmClient) throws NoSuchFieldException {
		ExecutionResult result = getSubProcessWithNameAndPath(bpmClient, GEOCODING_LOCATION_PROCESS_PATH,
				GEOCODING_LOCATION_REVERSE_SIGNATURE).execute(TEST_LON_VALUE, TEST_LAT_VALUE, 1);
		var object = result.data().last().get(RESULTS_KEY);
		assertThat(object).isInstanceOf(List.class);
		var objects = (ArrayList<?>) object;
		assertThat(objects).isNotEmpty();
		assertThat(objects.get(0)).isInstanceOf(GeoLocation.class);
	}

	@Test()
	public void testReverse_ThrowsBpmException(BpmClient bpmClient) throws NoSuchFieldException {
		try {
			getSubProcessWithNameAndPath(bpmClient, GEOCODING_LOCATION_PROCESS_PATH,
					GEOCODING_LOCATION_REVERSE_SIGNATURE).execute(null, null, 1);
		} catch (BpmError e) {
			assertThat(e.getHttpStatusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
		}
	}
}

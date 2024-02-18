package com.axonivy.connector.openweather.managedbean;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.openweathermap.api.data2_5.client.Forecast;
import org.openweathermap.api.data2_5.client.WeatherRecord;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;

import com.axonivy.connector.openweather.dto.DailyForecast;
import com.axonivy.connector.openweather.dto.DailyForecastDisplayInfo;
import com.axonivy.connector.openweather.service.ForecastService;
import com.axonivy.connector.openweather.util.Constants;
import com.axonivy.connector.openweather.util.DateTimeFormatterUtilities;

@ManagedBean
@ViewScoped
public class ForecastWeatherBean implements Serializable {

	private static final long serialVersionUID = 4700043015312367231L;

	private String typeOfDegree;
	private LocalDate selectedDate;
	private LocalTime selectedTime;
	private int selectedDateIndex;
	private int selectedTimeIndex;
	private String cityName;
	private String formattedTime12Hour;
	private String formattedDate;
	private ZoneId zoneId;
	private String speedUnit;
	private String units;

	private int currentTemperature;
	private int currentHumidity;
	private float currentWindSpeed;
	private String currentWeatherIconCode;
	private String currentWeatherDetail;

	private List<WeatherRecord> threeHourlyFiveDayForecasts;
	private List<DailyForecast> dailyForecasts;
	private List<DailyForecastDisplayInfo> dailyForecastDisplayInfos;

	private String searchCityName;
	private String searchCountryCode;
	private String searchStateCode;

	private LineChartModel temperatureModel;
	private BarChartModel precipitationModel;
	private int chartWindowSize;

	@PostConstruct
	public void init() {
		searchCityName = Constants.DEFAULT_SEARCH_CITY_NAME;
		units = Constants.DEFAULT_UNITS;
		typeOfDegree = Constants.DEFAULT_TYPE_OF_DEGREE;
		speedUnit = Constants.DEFAULT_SPEED_UNIT;
		chartWindowSize = Constants.DEFAULT_CHART_WINDOW_SIZE;
		search();
	}

	public LocalDate getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(LocalDate selectedDate) {
		this.selectedDate = selectedDate;
		updateFormattedDate();
	}

	public LocalTime getSelectedTime() {
		return selectedTime;
	}

	public void setSelectedTimeIndexFromUI() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		setSelectedTimeIndex(Integer.parseInt(params.get("selectedTimeIndex")));
	}

	public int getSelectedTimeIndex() {
		return selectedTimeIndex;
	}

	public void setSelectedTimeIndex(int selectedTimeIndex) {
		if (selectedTimeIndex < 0 || selectedTimeIndex >= threeHourlyFiveDayForecasts.size()) {
			return;
		}
		this.selectedTimeIndex = selectedTimeIndex;
		WeatherRecord selectedRecord = threeHourlyFiveDayForecasts.get(selectedTimeIndex);
		LocalDateTime dateTime = Instant.ofEpochSecond(selectedRecord.getDt()).atZone(zoneId).toLocalDateTime();

		setSelectedTime(dateTime.toLocalTime());

		if (selectedDate.isAfter(dateTime.toLocalDate())) {
			setSelectedDate(dateTime.toLocalDate());
			selectedDateIndex--;
		} else if (selectedDate.isBefore(dateTime.toLocalDate())) {
			setSelectedDate(dateTime.toLocalDate());
			selectedDateIndex++;
		}

		currentTemperature = selectedRecord.getMain().getTemp().intValue();
		currentHumidity = selectedRecord.getMain().getHumidity();
		currentWindSpeed = selectedRecord.getWind().getSpeed();
		currentWeatherIconCode = selectedRecord.getWeather().get(0).getIcon();
		currentWeatherDetail = StringUtils.capitalize(selectedRecord.getWeather().get(0).getDescription());
	}

	public void setSelectedTime(LocalTime selectedTime) {
		this.selectedTime = selectedTime;
		updateFormattedTime12Hour();
	}

	public int getSelectedDateIndex() {
		return selectedDateIndex;
	}

	public void setSelectedDateIndex(int selectedDateIndex) {
		if (selectedDateIndex < 0 || selectedDateIndex >= dailyForecastDisplayInfos.size()) {
			return;
		}

		this.selectedDateIndex = selectedDateIndex;
		DailyForecast selectedForecast = dailyForecastDisplayInfos.get(selectedDateIndex).getDailyForecast();
		setSelectedDate(selectedForecast.getDate());

		currentTemperature = selectedForecast.getTemperature();
		currentHumidity = selectedForecast.getHumidity();
		currentWindSpeed = selectedForecast.getWindSpeed();
		currentWeatherIconCode = selectedForecast.getWeatherIcon();
		currentWeatherDetail = StringUtils.capitalize(selectedForecast.getWeatherDescription());

		setSelectedTime(null);
	}

	public String getTypeOfDegree() {
		return typeOfDegree;
	}

	public void setTypeOfDegree(String typeOfDegree) {
		if (!Objects.equals(this.typeOfDegree, typeOfDegree)) {
			this.typeOfDegree = typeOfDegree;
			if (typeOfDegree.equals(Constants.CELSIUS_TYPE_OF_DEGREE)) {
				speedUnit = Constants.SPEED_METER_UNIT;
				units = Constants.METRIC_UNITS;
			} else if (typeOfDegree.equals(Constants.FAHRENHEIT_TYPE_OF_DEGREE)) {
				speedUnit = Constants.SPEED_MILE_UNIT;
				units = Constants.IMPERIAL_UNITS;
			} else {
				typeOfDegree = StringUtils.EMPTY;
				speedUnit = StringUtils.EMPTY;
				units = StringUtils.EMPTY;
			}
			processAndGroupForecastData();
			if (selectedTime == null) {
				setSelectedDateIndex(selectedDateIndex);
			} else {
				setSelectedTimeIndex(selectedTimeIndex);
			}
			updateTemperatureModelByData();
		}
	}

	public int getCurrentWeatherDegree() {
		return currentTemperature;
	}

	public int getCurrentHumidity() {
		return currentHumidity;
	}

	public float getCurrentWindSpeed() {
		return currentWindSpeed;
	}

	public String getCurrentWeatherIconCode() {
		return currentWeatherIconCode;
	}

	public String getCurrentWeatherDetail() {
		return currentWeatherDetail;
	}

	public String getFormattedTime12Hour() {
		return formattedTime12Hour;
	}

	public String getFormattedDate() {
		return formattedDate;
	}

	public String getSearchCityName() {
		return searchCityName;
	}

	public void setSearchCityName(String searchCityName) {
		this.searchCityName = searchCityName;
	}

	public String getSearchCountryCode() {
		return searchCountryCode;
	}

	public void setSearchCountryCode(String searchCountryCode) {
		this.searchCountryCode = searchCountryCode;
	}

	public String getSearchStateCode() {
		return searchStateCode;
	}

	public void setSearchStateCode(String searchStateCode) {
		this.searchStateCode = searchStateCode;
	}

	private void updateFormattedDate() {
		formattedDate = DateTimeFormatterUtilities.formatDate(selectedDate);
	}

	private void updateFormattedTime12Hour() {
		formattedTime12Hour = DateTimeFormatterUtilities.formatTime12Hour(selectedTime);
	}

	public String getCityName() {
		return cityName;
	}

	public List<DailyForecastDisplayInfo> getDailyForecastDisplayInfos() {
		return dailyForecastDisplayInfos;
	}

	public LineChartModel getTemperatureModel() {
		return temperatureModel;
	}

	public String getUnits() {
		return units;
	}

	public String getSpeedUnit() {
		return speedUnit;
	}

	public BarChartModel getPrecipitationModel() {
		return precipitationModel;
	}

	public int getChartWindowSize() {
		return chartWindowSize;
	}
	
	public int getCurrentChartWindowStartX() {
		return dailyForecastDisplayInfos.get(selectedDateIndex).getChartWindowStartX();
	}
	
	public int getCurrentChartWindowEndX() {
		return dailyForecastDisplayInfos.get(selectedDateIndex).getChartWindowEndX();
	}

	public void search() {
		processAndGroupForecastData();
		setSelectedDateIndex(0);
		createTemperatureModel();
		createPrecipitationModel();
	}

	private void processAndGroupForecastData() {
		Optional<Forecast> optionalForecast = ForecastService.getInstance()
				.fetchForecastThreeHourlyFiveDay(searchCityName, searchCountryCode, searchStateCode, units);

		if (optionalForecast.isEmpty()) {
			return;
		}
		Forecast forecast = optionalForecast.get();

		forecast.getList().forEach(record -> {
			record.getWeather().forEach(weather -> {
				String icon = weather.getIcon();
				if (icon != null) {
					icon = icon.replace('n', 'd');
					weather.setIcon(icon);
				}
			});
		});

		zoneId = ZoneId.ofOffset("UTC", ZoneOffset.ofTotalSeconds(forecast.getCity().getTimezone()));
		cityName = forecast.getCity().getName();
		threeHourlyFiveDayForecasts = forecast.getList();
		groupForecastData();
	}

	private void groupForecastData() {
		Map<LocalDate, List<WeatherRecord>> forecastsByDate = groupForecastsByDate();

		dailyForecasts = generateDailyForecasts(forecastsByDate);

		dailyForecastDisplayInfos = IntStream.range(0, dailyForecasts.size())
				.mapToObj(index -> createDailyForecastDisplayInfo(index)).collect(Collectors.toList());
	}

	private Map<LocalDate, List<WeatherRecord>> groupForecastsByDate() {
		return threeHourlyFiveDayForecasts.stream().collect(
				Collectors.groupingBy(record -> Instant.ofEpochSecond(record.getDt()).atZone(zoneId).toLocalDate()));
	}

	private List<DailyForecast> generateDailyForecasts(Map<LocalDate, List<WeatherRecord>> forecastsByDate) {
		return forecastsByDate.entrySet().stream().map(entry -> new DailyForecast(entry.getKey(), entry.getValue()))
				.sorted(Comparator.comparing(DailyForecast::getDate)).limit(5).collect(Collectors.toList());
	}

	private DailyForecastDisplayInfo createDailyForecastDisplayInfo(int index) {
		int startX = calculateModelStartX(index);
		int endX = startX + chartWindowSize - 1;
		return new DailyForecastDisplayInfo(dailyForecasts.get(index), startX, endX);
	}

	private int calculateModelStartX(int index) {
		return IntStream.range(0, index).map(i -> dailyForecasts.get(i).getDailyRecords().size()).sum();
	}

	private void createTemperatureModel() {
		temperatureModel = new LineChartModel();
		ChartData data = new ChartData();
		LineChartDataSet dataSet = new LineChartDataSet();
		LineChartOptions options = new LineChartOptions();

		List<Object> values = prepareTemperatureData();
		List<String> labels = prepareTimeLabels();

		dataSet.setData(values);
		dataSet.setLabel("Temperature");
		dataSet.setFill(false);
		dataSet.setTension(0.4);
		data.addChartDataSet(dataSet);

		data.setLabels(labels);

		temperatureModel.setOptions(options);
		temperatureModel.setData(data);
		temperatureModel.setExtender("temperatureChartExtender");
	}

	private void updateTemperatureModelByData() {
		LineChartDataSet dataSet = (LineChartDataSet) temperatureModel.getData().getDataSet().get(0);
		List<Object> values = prepareTemperatureData();
		dataSet.setData(values);
	}

	private List<Object> prepareTemperatureData() {
		return dailyForecastDisplayInfos
				.stream().map(DailyForecastDisplayInfo::getDailyForecast).flatMap(dailyForecast -> dailyForecast
						.getDailyRecords().stream().map(record -> record.getMain().getTempMax().intValue()))
				.collect(Collectors.toList());
	}

	private List<String> prepareTimeLabels() {
		return dailyForecastDisplayInfos.stream().map(DailyForecastDisplayInfo::getDailyForecast)
				.flatMap(dailyForecast -> dailyForecast.getDailyRecords().stream()
						.map(record -> Instant.ofEpochSecond(record.getDt()).atZone(zoneId).toLocalTime()))
				.map(DateTimeFormatterUtilities::formatTime24Hour).collect(Collectors.toList());
	}

	public void createPrecipitationModel() {
		precipitationModel = new BarChartModel();
		ChartData data = new ChartData();
		BarChartDataSet dataSet = new BarChartDataSet();
		BarChartOptions options = new BarChartOptions();

		List<Number> values = preparePrecipitationData();
		List<String> labels = prepareTimeLabels();

		dataSet.setData(values);
		dataSet.setLabel("Precipitation");
		data.addChartDataSet(dataSet);
		data.setLabels(labels);

		precipitationModel.setOptions(options);
		precipitationModel.setData(data);
		precipitationModel.setExtender("precipitationChartExtender");
	}

	private List<Number> preparePrecipitationData() {
		return dailyForecastDisplayInfos.stream().map(DailyForecastDisplayInfo::getDailyForecast).flatMap(
				dailyForecast -> dailyForecast.getDailyRecords().stream().map(record -> record.getPop().intValue()))
				.collect(Collectors.toList());
	}
}
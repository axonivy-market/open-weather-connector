# OpenWeather Connector

Harness the power of freely available weather data for your business processes with OpenWeather! This Axon Ivy Connector implements access to OpenWeatherMap data sets and creates various weather maps within the free plan:

* **Current Weather:** Get the current weather for any location worldwide.
* **Advanced forecasts:** Plan ahead with multi-day forecasts.
* **Precipitation:** Forecast the probability of precipitation for the next few days.
* **Wind:** Wind strength and prevailing direction can also be predicted.

This connector supports you with a demo implementation to reduce your integration effort

## Demo

Within this demo, users can access comprehensive **5-day weather forecasts** for any location worldwide. Follow these simple steps:

1. **Precise Location Specification:** Accurately identify the desired location by entering the city name and its corresponding country code. For locations within the United States, the inclusion of the state code further refines the forecast.
2. **Initiate Search:** Simply click the **Search** button to activate the retrieval process. The connector will efficiently retrieve and display a detailed **5-day forecast** specific to your chosen location.

![Demo](images/forecast-weather-demo.png)
![Demo](images/precipitation-chart.png)
![Demo](images/wind-chart.png)

## Setup

### Application ID
The OpenWeatherMap weather API is not free to use. However, there is a free version with minimal API calls for development purposes. To use the connector, select a suitable API package via the [OpenWeatherMap API Developer](https://openweathermap.org/api) and generate an **API key**.

##### How to get an API key
1. Login and go to your [OpenWeatherMap API keys page](https://home.openweathermap.org/api_keys)
2. Add your API key name and generate it:
![Register key](images/register-api-key.png)
3. API key now is available:
![Register key](images/register-successful.png)

After a **API key** is available, you can store it in your project in the variables.yaml as the variable "openWeatherConnector.appId":

```
@variables.yaml@ 
```

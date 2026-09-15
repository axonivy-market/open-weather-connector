package com.axonivy.connector.openweather.auth;

import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;

public class OpenWeatherAuthFeature implements Feature {
	
	@Override
	public boolean configure(FeatureContext context) {
		context.register(new AppIdAuthorizationFilter(), Priorities.AUTHENTICATION);
		return true;
	}
}

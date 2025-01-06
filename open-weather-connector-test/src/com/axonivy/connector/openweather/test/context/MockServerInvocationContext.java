package com.axonivy.connector.openweather.test.context;

import static com.axonivy.connector.openweather.test.constant.OpenWeatherCommonConstants.MOCK_SERVER_CONTEXT_DISPLAY_NAME;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;;

public class MockServerInvocationContext implements TestTemplateInvocationContext {
  @Override
  public String getDisplayName(int invocationIndex) {
    return MOCK_SERVER_CONTEXT_DISPLAY_NAME;
  }

  @Override
  public List<Extension> getAdditionalExtensions() {
    return Collections.singletonList(
    		new ParameterResolver() {

      @Override
      public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
          throws ParameterResolutionException {
        return extensionContext;
      }

      @Override
      public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
          throws ParameterResolutionException {
    	 return ExtensionContext.class == parameterContext.getParameter().getType();
      }
    });
  }
}

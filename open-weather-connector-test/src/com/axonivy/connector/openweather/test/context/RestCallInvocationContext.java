package com.axonivy.connector.openweather.test.context;

import static com.axonivy.connector.openweather.test.constant.OpenWeatherCommonConstants.REAL_CALL_CONTEXT_DISPLAY_NAME;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;

import com.axonivy.connector.openweather.test.utils.OpenWeatherUtils;

public class RestCallInvocationContext implements TestTemplateInvocationContext {

  @Override
  public String getDisplayName(int invocationIndex) {
    return REAL_CALL_CONTEXT_DISPLAY_NAME;
  }

  public void setUp() throws IOException {
    OpenWeatherUtils.setUpConfigForRestCallTest();
  }

  @Override
  public List<Extension> getAdditionalExtensions() {
    return Collections.singletonList(new ParameterResolver() {

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

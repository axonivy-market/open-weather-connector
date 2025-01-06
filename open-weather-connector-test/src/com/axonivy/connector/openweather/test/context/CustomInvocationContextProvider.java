package com.axonivy.connector.openweather.test.context;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

public class CustomInvocationContextProvider implements TestTemplateInvocationContextProvider {

  @Override
  public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
    return Stream.of(new MockServerInvocationContext(), new RestCallInvocationContext());
  }

  @Override
  public boolean supportsTestTemplate(ExtensionContext context) {
    return true;
  }
}

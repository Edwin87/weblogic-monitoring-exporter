package io.prometheus.wls.rest.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import javax.servlet.http.HttpServletResponse;

public class ResponseHeaderMatcher extends TypeSafeDiagnosingMatcher<HttpServletResponse> {
    private String expectedHeaderName;
    private String expectedHeaderValue;

    private ResponseHeaderMatcher(String expectedHeaderName, String expectedHeaderValue) {
        this.expectedHeaderName = expectedHeaderName;
        this.expectedHeaderValue = expectedHeaderValue;
    }

    public static ResponseHeaderMatcher containsHeader(String expectedHeaderName, String expectedHeaderValue) {
        return new ResponseHeaderMatcher(expectedHeaderName, expectedHeaderValue);
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    protected boolean matchesSafely(HttpServletResponse response, Description description) {
        if (!response.containsHeader(expectedHeaderName))
            return reportNoSuchHeader(response, description);
        else if (!response.getHeaders(expectedHeaderName).contains(expectedHeaderValue))
            return reportNoMatchingHeaderValue(response, description);
        else
            return true;
    }

    private boolean reportNoSuchHeader(HttpServletResponse response, Description description) {
        description.appendValueList("found header names: {", ", ", "}", response.getHeaderNames());
        return false;
    }

    private boolean reportNoMatchingHeaderValue(HttpServletResponse response, Description description) {
        description.appendText("found ").appendText(expectedHeaderName)
                .appendValueList(" with value(s) ", ", ", "", response.getHeaders(expectedHeaderName));
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("response containing header ")
                .appendValue(expectedHeaderName)
                .appendText(" with value ")
                .appendValue(expectedHeaderValue);
    }
}
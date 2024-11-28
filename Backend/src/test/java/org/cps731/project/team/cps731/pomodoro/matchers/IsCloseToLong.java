package org.cps731.project.team.cps731.pomodoro.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class IsCloseToLong extends TypeSafeMatcher<Long> {

    private final long expectedValue;
    private final long margin;

    public IsCloseToLong(long expectedValue, long margin) {
        this.expectedValue = expectedValue;
        this.margin = margin;
    }

    @Override
    protected boolean matchesSafely(Long actualValue) {
        return Math.abs(actualValue - expectedValue) <= margin;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a long value within ")
                   .appendValue(margin)
                   .appendText(" of ")
                   .appendValue(expectedValue);
    }

    public static IsCloseToLong closeTo(long expectedValue, long margin) {
        return new IsCloseToLong(expectedValue, margin);
    }
}
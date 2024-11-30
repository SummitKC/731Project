package org.cps731.project.team.cps731.pomodoro.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class IsCloseToInt extends TypeSafeMatcher<Integer> {

    private final int expectedValue;
    private final int margin;

    public IsCloseToInt(int expectedValue, int margin) {
        this.expectedValue = expectedValue;
        this.margin = margin;
    }

    @Override
    protected boolean matchesSafely(Integer actualValue) {
        return Math.abs(actualValue - expectedValue) <= margin;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a int value within ")
                .appendValue(margin)
                .appendText(" of ")
                .appendValue(expectedValue);
    }

    public static IsCloseToInt closeTo(int expectedValue, int margin) {
        return new IsCloseToInt(expectedValue, margin);
    }
}

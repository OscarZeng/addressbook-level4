package seedu.address.model.record;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Record's day parameter of any date in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDayParam(String)}
 */
public class DayParam {


    public static final String MESSAGE_DAYPARAM_CONSTRAINTS =
            "Day parameter should only contain numbers, and it should be at least 3 digits long";
    public static final String DAYPARAM_VALIDATION_REGEX = "\\d{3,}";
    public final String value;

    /**
     * Constructs a {@code DayParam}.
     *
     * @param dayParam A valid day parameter.
     */
    public DayParam(String dayParam) {
        requireNonNull(dayParam);
        checkArgument(isValidDayParam(dayParam), MESSAGE_DAYPARAM_CONSTRAINTS);
        value = dayParam;
    }

    /**
     * Returns true if a given string is a valid day parameter of any date.
     */
    public static boolean isValidDayParam(String test) {
        return test.matches(DAYPARAM_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DayParam // instanceof handles nulls
                && value.equals(((DayParam) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}

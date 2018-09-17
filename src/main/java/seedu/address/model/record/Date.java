package seedu.address.model.record;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Record's day parameter of any date in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class Date {


    public static final String MESSAGE_DATE_CONSTRAINTS =
            "Day parameter should only contain numbers, and it should be at least 3 digits long";
    public static final String DATE_VALIDATION_REGEX = "\\d{3,}";
    public final String value;

    /**
     * Constructs a {@code Date}.
     *
     * @param date A valid day parameter.
     */
    public Date(String date) {
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_DATE_CONSTRAINTS);
        value = date;
    }

    /**
     * Returns true if a given string is a valid day parameter of any date.
     */
    public static boolean isValidDate(String test) {
        return test.matches(DATE_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Date // instanceof handles nulls
                && value.equals(((Date) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}

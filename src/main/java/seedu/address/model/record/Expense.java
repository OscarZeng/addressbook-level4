package seedu.address.model.record;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Record's expense in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidExpense(String)}
 */
public class Expense {

    public static final String MESSAGE_EXPENSE_CONSTRAINTS =
            "Expenses can take any values, and it should not be blank";


    /*
     * The first character of the expense must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String EXPENSE_VALIDATION_REGEX = "[^\\s].*";

    public final String value;

    /**
     * Constructs an {@code Expense}.
     *
     * @param expense A valid expense.
     */
    public Expense(String expense) {
        requireNonNull(expense);
        checkArgument(isValidExpense(expense), MESSAGE_EXPENSE_CONSTRAINTS);
        value = expense;
    }

    /**
     * Returns true if a given string is a valid email.
     */
    public static boolean isValidExpense(String test) {
        return test.matches(EXPENSE_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Expense // instanceof handles nulls
                && value.equals(((Expense) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}

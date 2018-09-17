package seedu.address.model.record;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class ExpenseTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Expense(null));
    }

    @Test
    public void constructor_invalidExpenseArgumentException() {
        String invalidExpense = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Expense(invalidExpense));
    }

    @Test
    public void isValidExpense() {
        // null expense
        Assert.assertThrows(NullPointerException.class, () -> Expense.isValidExpense(null));

        // invalid expense
        assertFalse(Expense.isValidExpense("")); // empty string
        assertFalse(Expense.isValidExpense(" ")); // spaces only

        // valid expense
        assertTrue(Expense.isValidExpense("Blk 456, Den Road, #01-355"));
        assertTrue(Expense.isValidExpense("-")); // one character
        assertTrue(Expense.isValidExpense("Leng Inc; 1234 Market St; San Francisco CA 2349879; USA")); // long expense
    }
}

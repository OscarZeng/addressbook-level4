package seedu.address.model.record;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class DayParamTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new DayParam(null));
    }

    @Test
    public void constructor_invalidDayParam_throwsIllegalArgumentException() {
        String invalidDayParam = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new DayParam(invalidDayParam));
    }

    @Test
    public void isValidDayParam() {
        // null day parameter
        Assert.assertThrows(NullPointerException.class, () -> DayParam.isValidDayParam(null));

        // invalid day parameters
        assertFalse(DayParam.isValidDayParam("")); // empty string
        assertFalse(DayParam.isValidDayParam(" ")); // spaces only
        assertFalse(DayParam.isValidDayParam("91")); // less than 3 numbers
        assertFalse(DayParam.isValidDayParam("dayparams")); // non-numeric
        assertFalse(DayParam.isValidDayParam("9011p041")); // alphabets within digits
        assertFalse(DayParam.isValidDayParam("9312 1534")); // spaces within digits

        // valid day parameters
        assertTrue(DayParam.isValidDayParam("911")); // exactly 3 numbers
        assertTrue(DayParam.isValidDayParam("93121534"));
        assertTrue(DayParam.isValidDayParam("124293842033123")); // long day parameters
    }
}

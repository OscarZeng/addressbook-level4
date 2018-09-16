package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.record.Address;
import seedu.address.model.record.Email;
import seedu.address.model.record.Name;
import seedu.address.model.record.Record;
import seedu.address.model.record.DayParam;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Record objects.
 */
public class RecordBuilder {

    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_DAYPARAM = "85355255";
    public static final String DEFAULT_EMAIL = "alice@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    private Name name;
    private DayParam dayParam;
    private Email email;
    private Address address;
    private Set<Tag> tags;

    public RecordBuilder() {
        name = new Name(DEFAULT_NAME);
        dayParam = new DayParam(DEFAULT_DAYPARAM);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
    }

    /**
     * Initializes the RecordBuilder with the data of {@code recordToCopy}.
     */
    public RecordBuilder(Record recordToCopy) {
        name = recordToCopy.getName();
        dayParam = recordToCopy.getDayParam();
        email = recordToCopy.getEmail();
        address = recordToCopy.getAddress();
        tags = new HashSet<>(recordToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Record} that we are building.
     */
    public RecordBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Record} that we are building.
     */
    public RecordBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Record} that we are building.
     */
    public RecordBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code dayParam} of the {@code Record} that we are building.
     */
    public RecordBuilder withDayParam(String dayParam) {
        this.dayParam = new DayParam(dayParam);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Record} that we are building.
     */
    public RecordBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    public Record build() {
        return new Record(name, dayParam, email, address, tags);
    }

}

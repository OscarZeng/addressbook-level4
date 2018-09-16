package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.record.Address;
import seedu.address.model.record.Email;
import seedu.address.model.record.Name;
import seedu.address.model.record.Record;
import seedu.address.model.record.DayParam;
import seedu.address.model.tag.Tag;

/**
 * JAXB-friendly version of the Record.
 */
public class XmlAdaptedRecord {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Record's %s field is missing!";

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String dayParam;
    @XmlElement(required = true)
    private String email;
    @XmlElement(required = true)
    private String address;

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedRecord.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedRecord() {}

    /**
     * Constructs an {@code XmlAdaptedRecord} with the given record details.
     */
    public XmlAdaptedRecord(String name, String dayParam, String email, String address, List<XmlAdaptedTag> tagged) {
        this.name = name;
        this.dayParam = dayParam;
        this.email = email;
        this.address = address;
        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
    }

    /**
     * Converts a given Record into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedRecord
     */
    public XmlAdaptedRecord(Record source) {
        name = source.getName().fullName;
        dayParam = source.getDayParam().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        tagged = source.getTags().stream()
                .map(XmlAdaptedTag::new)
                .collect(Collectors.toList());
    }

    /**
     * Converts this jaxb-friendly adapted record object into the model's Record object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted record
     */
    public Record toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (dayParam == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, DayParam.class.getSimpleName()));
        }
        if (!DayParam.isValidDayParam(dayParam)) {
            throw new IllegalValueException(DayParam.MESSAGE_DAYPARAM_CONSTRAINTS);
        }
        final DayParam modelDayParam = new DayParam(dayParam);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_EMAIL_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_ADDRESS_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final Set<Tag> modelTags = new HashSet<>(personTags);
        return new Record(modelName, modelDayParam, modelEmail, modelAddress, modelTags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedRecord)) {
            return false;
        }

        XmlAdaptedRecord otherPerson = (XmlAdaptedRecord) other;
        return Objects.equals(name, otherPerson.name)
                && Objects.equals(dayParam, otherPerson.dayParam)
                && Objects.equals(email, otherPerson.email)
                && Objects.equals(address, otherPerson.address)
                && tagged.equals(otherPerson.tagged);
    }
}

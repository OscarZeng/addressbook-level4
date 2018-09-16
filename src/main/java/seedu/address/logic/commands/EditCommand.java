package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAYPARAM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_RECORDS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.record.Address;
import seedu.address.model.record.Email;
import seedu.address.model.record.Name;
import seedu.address.model.record.Record;
import seedu.address.model.record.DayParam;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing record in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the record identified "
            + "by the index number used in the displayed record list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_DAYPARAM + "DAYPARAM] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_DAYPARAM + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_RECORD_SUCCESS = "Edited Record: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_RECORD = "This record already exists in the address book.";

    private final Index index;
    private final EditRecordDescriptor editRecordDescriptor;

    /**
     * @param index of the record in the filtered record list to edit
     * @param editRecordDescriptor details to edit the record with
     */
    public EditCommand(Index index, EditRecordDescriptor editRecordDescriptor) {
        requireNonNull(index);
        requireNonNull(editRecordDescriptor);

        this.index = index;
        this.editRecordDescriptor = new EditRecordDescriptor(editRecordDescriptor);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Record> lastShownList = model.getFilteredRecordList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_RECORD_DISPLAYED_INDEX);
        }

        Record recordToEdit = lastShownList.get(index.getZeroBased());
        Record editedRecord = createEditedPerson(recordToEdit, editRecordDescriptor);

        if (!recordToEdit.isSameRecord(editedRecord) && model.hasRecord(editedRecord)) {
            throw new CommandException(MESSAGE_DUPLICATE_RECORD);
        }

        model.updateRecord(recordToEdit, editedRecord);
        model.updateFilteredRecordList(PREDICATE_SHOW_ALL_RECORDS);
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_EDIT_RECORD_SUCCESS, editedRecord));
    }

    /**
     * Creates and returns a {@code Record} with the details of {@code recordToEdit}
     * edited with {@code editRecordDescriptor}.
     */
    private static Record createEditedPerson(Record recordToEdit, EditRecordDescriptor editRecordDescriptor) {
        assert recordToEdit != null;

        Name updatedName = editRecordDescriptor.getName().orElse(recordToEdit.getName());
        DayParam updatedDayParam = editRecordDescriptor.getDayParam().orElse(recordToEdit.getDayParam());
        Email updatedEmail = editRecordDescriptor.getEmail().orElse(recordToEdit.getEmail());
        Address updatedAddress = editRecordDescriptor.getAddress().orElse(recordToEdit.getAddress());
        Set<Tag> updatedTags = editRecordDescriptor.getTags().orElse(recordToEdit.getTags());

        return new Record(updatedName, updatedDayParam, updatedEmail, updatedAddress, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
                && editRecordDescriptor.equals(e.editRecordDescriptor);
    }

    /**
     * Stores the details to edit the record with. Each non-empty field value will replace the
     * corresponding field value of the record.
     */
    public static class EditRecordDescriptor {
        private Name name;
        private DayParam dayParam;
        private Email email;
        private Address address;
        private Set<Tag> tags;

        public EditRecordDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditRecordDescriptor(EditRecordDescriptor toCopy) {
            setName(toCopy.name);
            setDayParam(toCopy.dayParam);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, dayParam, email, address, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setDayParam(DayParam dayParam) {
            this.dayParam = dayParam;
        }

        public Optional<DayParam> getDayParam() {
            return Optional.ofNullable(dayParam);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditRecordDescriptor)) {
                return false;
            }

            // state check
            EditRecordDescriptor e = (EditRecordDescriptor) other;

            return getName().equals(e.getName())
                    && getDayParam().equals(e.getDayParam())
                    && getEmail().equals(e.getEmail())
                    && getAddress().equals(e.getAddress())
                    && getTags().equals(e.getTags());
        }
    }
}

package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DAYPARAM_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showRecordAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_RECORD;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_RECORD;
import static seedu.address.testutil.TypicalRecords.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.EditCommand.EditRecordDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.record.Record;
import seedu.address.testutil.EditRecordDescriptorBuilder;
import seedu.address.testutil.RecordBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Record editedRecord = new RecordBuilder().build();
        EditRecordDescriptor descriptor = new EditRecordDescriptorBuilder(editedRecord).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_RECORD, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_RECORD_SUCCESS, editedRecord);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateRecord(model.getFilteredRecordList().get(0), editedRecord);
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastRecord = Index.fromOneBased(model.getFilteredRecordList().size());
        Record lastRecord = model.getFilteredRecordList().get(indexLastRecord.getZeroBased());

        RecordBuilder recordInList = new RecordBuilder(lastRecord);
        Record editedRecord = recordInList.withName(VALID_NAME_BOB).withDayParam(VALID_DAYPARAM_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditRecordDescriptor descriptor = new EditRecordDescriptorBuilder().withName(VALID_NAME_BOB)
                .withDayParam(VALID_DAYPARAM_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastRecord, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_RECORD_SUCCESS, editedRecord);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateRecord(lastRecord, editedRecord);
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_RECORD, new EditRecordDescriptor());
        Record editedRecord = model.getFilteredRecordList().get(INDEX_FIRST_RECORD.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_RECORD_SUCCESS, editedRecord);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showRecordAtIndex(model, INDEX_FIRST_RECORD);

        Record recordInFilteredList = model.getFilteredRecordList().get(INDEX_FIRST_RECORD.getZeroBased());
        Record editedRecord = new RecordBuilder(recordInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_RECORD,
                new EditRecordDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_RECORD_SUCCESS, editedRecord);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateRecord(model.getFilteredRecordList().get(0), editedRecord);
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateRecordUnfilteredList_failure() {
        Record firstRecord = model.getFilteredRecordList().get(INDEX_FIRST_RECORD.getZeroBased());
        EditRecordDescriptor descriptor = new EditRecordDescriptorBuilder(firstRecord).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_RECORD, descriptor);

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_RECORD);
    }

    @Test
    public void execute_duplicateRecordFilteredList_failure() {
        showRecordAtIndex(model, INDEX_FIRST_RECORD);

        // edit record in filtered list into a duplicate in address book
        Record recordInList = model.getAddressBook().getRecordList().get(INDEX_SECOND_RECORD.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_RECORD,
                new EditRecordDescriptorBuilder(recordInList).build());

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_RECORD);
    }

    @Test
    public void execute_invalidRecordIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredRecordList().size() + 1);
        EditRecordDescriptor descriptor = new EditRecordDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_RECORD_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidRecordIndexFilteredList_failure() {
        showRecordAtIndex(model, INDEX_FIRST_RECORD);
        Index outOfBoundIndex = INDEX_SECOND_RECORD;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getRecordList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditRecordDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_RECORD_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Record editedRecord = new RecordBuilder().build();
        Record recordToEdit = model.getFilteredRecordList().get(INDEX_FIRST_RECORD.getZeroBased());
        EditRecordDescriptor descriptor = new EditRecordDescriptorBuilder(editedRecord).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_RECORD, descriptor);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateRecord(recordToEdit, editedRecord);
        expectedModel.commitAddressBook();

        // edit -> first record edited
        editCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered record list to show all records
        expectedModel.undoAddressBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first record edited again
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredRecordList().size() + 1);
        EditRecordDescriptor descriptor = new EditRecordDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        // execution failed -> address book state not added into model
        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_RECORD_DISPLAYED_INDEX);

        // single address book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Edits a {@code Record} from a filtered list.
     * 2. Undo the edit.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously edited record in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the edit. This ensures {@code RedoCommand} edits the record object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameRecordEdited() throws Exception {
        Record editedRecord = new RecordBuilder().build();
        EditRecordDescriptor descriptor = new EditRecordDescriptorBuilder(editedRecord).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_RECORD, descriptor);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        showRecordAtIndex(model, INDEX_SECOND_RECORD);
        Record recordToEdit = model.getFilteredRecordList().get(INDEX_FIRST_RECORD.getZeroBased());
        expectedModel.updateRecord(recordToEdit, editedRecord);
        expectedModel.commitAddressBook();

        // edit -> edits second record in unfiltered record list / first record in filtered record list
        editCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered record list to show all records
        expectedModel.undoAddressBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(model.getFilteredRecordList().get(INDEX_FIRST_RECORD.getZeroBased()), recordToEdit);
        // redo -> edits same second record in unfiltered record list
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_RECORD, DESC_AMY);

        // same values -> returns true
        EditRecordDescriptor copyDescriptor = new EditRecordDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_RECORD, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_RECORD, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_RECORD, DESC_BOB)));
    }

}

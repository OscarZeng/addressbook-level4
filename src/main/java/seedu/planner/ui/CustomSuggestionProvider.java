package seedu.planner.ui;

import static seedu.planner.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.planner.model.tag.DefaultTags.getSampleTagsForSuggestion;
import static seedu.planner.ui.SuggestionClass.newCreate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import impl.org.controlsfx.autocompletion.SuggestionProvider;
import seedu.planner.logic.commands.AddCommand;
import seedu.planner.logic.commands.AddLimitCommand;
import seedu.planner.logic.commands.ArchiveCommand;
import seedu.planner.logic.commands.CheckLimitCommand;
import seedu.planner.logic.commands.ClearCommand;
import seedu.planner.logic.commands.DeleteByDateCommand;
import seedu.planner.logic.commands.DeleteCommand;
import seedu.planner.logic.commands.DeleteLimitCommand;
import seedu.planner.logic.commands.EditCommand;
import seedu.planner.logic.commands.EditLimitCommand;
import seedu.planner.logic.commands.ExitCommand;
import seedu.planner.logic.commands.ExportExcelCommand;
import seedu.planner.logic.commands.FindCommand;
import seedu.planner.logic.commands.FindTagCommand;
import seedu.planner.logic.commands.HelpCommand;
import seedu.planner.logic.commands.HistoryCommand;
import seedu.planner.logic.commands.ImportExcelCommand;
import seedu.planner.logic.commands.ListCommand;
import seedu.planner.logic.commands.RedoCommand;
import seedu.planner.logic.commands.SelectCommand;
import seedu.planner.logic.commands.SortCommand;
import seedu.planner.logic.commands.StatisticCommand;
import seedu.planner.logic.commands.SummaryByCategoryCommand;
import seedu.planner.logic.commands.SummaryByDateCommand;
import seedu.planner.logic.commands.SummaryByMonthCommand;
import seedu.planner.logic.commands.SummaryCommand;
import seedu.planner.logic.commands.UndoCommand;

/**
 *  This object is responsible for the logic behind the {@code AutoCompleteBox} that decides the range of possible
 *  suggested texts that are to appear whenever a user enters a new word.
 */
public class CustomSuggestionProvider {

    private static final Set<String> commandKeywordsSet =
            new HashSet<>(Arrays.asList(
                    AddCommand.COMMAND_WORD, AddLimitCommand.COMMAND_WORD,
                    ArchiveCommand.COMMAND_WORD, CheckLimitCommand.COMMAND_WORD,
                    ClearCommand.COMMAND_WORD, DeleteByDateCommand.COMMAND_WORD,
                    DeleteCommand.COMMAND_WORD, DeleteLimitCommand.COMMAND_WORD,
                    EditCommand.COMMAND_WORD, EditLimitCommand.COMMAND_WORD,
                    ExitCommand.COMMAND_WORD, ExportExcelCommand.COMMAND_WORD,
                    FindCommand.COMMAND_WORD, FindTagCommand.COMMAND_WORD,
                    HelpCommand.COMMAND_WORD, HistoryCommand.COMMAND_WORD,
                    ImportExcelCommand.COMMAND_WORD, ListCommand.COMMAND_WORD,
                    RedoCommand.COMMAND_WORD, SelectCommand.COMMAND_WORD,
                    SortCommand.COMMAND_WORD, StatisticCommand.COMMAND_WORD,
                    SummaryCommand.COMMAND_WORD, UndoCommand.COMMAND_WORD));

    private static final Set<String> summaryKeywordsSet = new HashSet<>(Arrays.asList(
            SummaryByMonthCommand.COMMAND_MODE_WORD, SummaryByDateCommand.COMMAND_MODE_WORD,
            SummaryByCategoryCommand.COMMAND_MODE_WORD));
    private static final Set<String> possibleTagKeywordsSet = new HashSet<>(
            Arrays.asList(AddCommand.COMMAND_WORD, EditCommand.COMMAND_WORD));

    private static final Set<String> sortKeywordsSet = Stream.concat(SortCommand.ORDER_SET.stream(),
            SortCommand.CATEGORY_SET.stream()).collect(Collectors.toSet());

    private static final Set<String> emptySet = new HashSet<>();
    private static final Set<String> defaultTagsSet = getSampleTagsForSuggestion();
    private static Map<String, Integer> tagKeywordsMap = new HashMap<>();
    private static Set<String> tagsSuggestionSet = new HashSet<>();

    private SuggestionProvider<String> suggestionProvider;

    public CustomSuggestionProvider() {
        suggestionProvider = newCreate(emptySet);
    }

    public SuggestionProvider<String> getSuggestions() {
        return suggestionProvider;
    }

    /**
     * Clears the current set of suggestions and insert the new set of suggestions
     * @param suggestions is the complete list of strings that can be suggested to the user
     */
    private void updateSuggestions(Set<String> suggestions) {
        suggestionProvider.clearSuggestions();
        suggestionProvider.addPossibleSuggestions(suggestions);
    }

    /**
     * This method is responsible for deciding which collection of Strings is to be used as the reference collection
     * for comparison to the input String by the user. It can autocomplete the command word or when a tag is expected
     * of the user input or when a sort parameter is expected.
     * @param userInput is the current word/substring to be automatically completed
     */
    public void updateSuggestions(String userInput, String prefix, String wordInput) {
        String[] inputs = userInput.split(" ");
        wordInput = prefix + wordInput;
        int strIndex = 0;
        for (; strIndex < inputs.length && !inputs[strIndex].equals(wordInput); strIndex++);

        if (strIndex == 0) {
            updateSuggestions(commandKeywordsSet);
        } else if (inputs[0].equals(SortCommand.COMMAND_WORD)) {
            if (inputs.length == 2) {
                updateSuggestions(sortKeywordsSet);
            } else if (inputs.length == 3) {
                if (strIndex == 1) {
                    if (SortCommand.ORDER_SET.contains(inputs[2])) {
                        updateSuggestions(SortCommand.CATEGORY_SET);
                    } else if (SortCommand.CATEGORY_SET.contains(inputs[2])) {
                        updateSuggestions(SortCommand.ORDER_SET);
                    } else {
                        updateSuggestions(sortKeywordsSet);
                    }
                } else { // strIndex = 2
                    if (SortCommand.ORDER_SET.contains(inputs[1])) {
                        updateSuggestions(SortCommand.CATEGORY_SET);
                    } else if (SortCommand.CATEGORY_SET.contains(inputs[1])) {
                        updateSuggestions(SortCommand.ORDER_SET);
                    } else {
                        updateSuggestions(emptySet);
                    }
                }
            } else {
                updateSuggestions(emptySet);
            }
        } else if (possibleTagKeywordsSet.contains(inputs[0])) {
            if (wordInput.startsWith(PREFIX_TAG.getPrefix())) {
                updateSuggestions(tagsSuggestionSet);
            } else {
                updateSuggestions(emptySet);
            }
        } else if (inputs[0].equals(SummaryCommand.COMMAND_WORD)) {
            if (strIndex == 1) {
                updateSuggestions(summaryKeywordsSet);
            } else {
                updateSuggestions(emptySet);
            }
        } else if (inputs[0].equals(FindTagCommand.COMMAND_WORD)) {
            updateSuggestions(tagsSuggestionSet);
        } else {
            updateSuggestions(emptySet);
        }
    }

    /**
     * Updates the TagMap in this class whenever the TagMap in the Model is updated.
     * @param newTagKeywordsMap is the new TagMap object that is to replace the old TagMap
     */
    public static void updateTagMap(HashMap<String, Integer> newTagKeywordsMap) {
        tagKeywordsMap = newTagKeywordsMap;
        updateTagSet();
    }

    private static void updateTagSet() {
        tagsSuggestionSet = Stream.concat(tagKeywordsMap.keySet().stream(),
                defaultTagsSet.stream()).collect(Collectors.toSet());
    }

}

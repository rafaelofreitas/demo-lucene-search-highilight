import model.LuceneMatching;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestLuceneHighLighter {

    @Test
    public void textHighLighter() throws Exception {
        String searchQuery = "Changed java";
        LuceneHighlighter luceneHighlighter = new LuceneHighlighter();
        LuceneMatching luceneMatching = luceneHighlighter.searchWithHighLightKeywords(searchQuery);

        ArrayList<String> words = new ArrayList<>();
        words.add("Action that Changed the World");
        words.add("Java In Action");
        words.add("How To Java");

        for (int i = 0; i < words.size(); i++) {
            assertEquals(words.get(i), luceneMatching.getWordsFound().get(i));
        }

        ArrayList<String> markedText = luceneMatching.getTaggedPhrases();

        if (markedText.size() == 1) {
            int startPosition = luceneMatching.getStartPosition(markedText.get(0));
            int endPosition = luceneMatching.getEndPosition(markedText.get(0));

            assertEquals(17, startPosition);
            assertEquals(23, endPosition);
        }
    }
}
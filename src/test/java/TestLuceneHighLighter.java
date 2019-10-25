import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestLuceneHighLighter {

    @Test
    public void textHighLighter() throws Exception {
        String searchQuery = "Changed";
        LuceneHighlighter luceneHighlighter = new LuceneHighlighter();
        luceneHighlighter.createIndex();
        luceneHighlighter.searchWithHighLightKeywords(searchQuery);
        ArrayList<String> fragment = luceneHighlighter.getFragment();

        if (fragment.size() == 1) {
            int startPosition = luceneHighlighter.getStartPosition(fragment.get(0));
            int endPosition = luceneHighlighter.getEndPosition(fragment.get(0));

            assertEquals(17, startPosition);
            assertEquals(23, endPosition);
        }
    }

    @Test
    public void textHighLighter2() throws Exception {
        String searchQuery = "Java Changed";
        LuceneHighlighter luceneHighlighter = new LuceneHighlighter();
        luceneHighlighter.createIndex();
        luceneHighlighter.searchIndex(searchQuery);
        ArrayList<String> matching = luceneHighlighter.getWordsFound();

        ArrayList<String> words = new ArrayList<>();
        words.add("Action that Changed the World");
        words.add("Java In Action");
        words.add("How To Java");

        for (int i = 0; i < words.size(); i++) {
            assertEquals(words.get(i), matching.get(i));
        }
    }
}
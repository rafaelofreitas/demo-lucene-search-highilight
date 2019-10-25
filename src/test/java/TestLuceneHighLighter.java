import org.junit.Test;

import java.util.ArrayList;

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
            System.out.println(startPosition);
            System.out.println(endPosition);
        }
    }

    @Test
    public void textHighLighter2() throws Exception {
        String searchQuery = "Java Changed";
        LuceneHighlighter luceneHighlighter = new LuceneHighlighter();
        luceneHighlighter.createIndex();

        luceneHighlighter.searchIndex(searchQuery);
    }
}
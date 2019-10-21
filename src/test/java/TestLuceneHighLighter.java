import org.junit.Test;

public class TestLuceneHighLighter {

    @Test
    public void textHighLighter() throws Exception {
        String searchQuery = "Changed";
        LuceneHighlighter luceneHighlighter = new LuceneHighlighter();
        luceneHighlighter.createIndex();

        luceneHighlighter.searchAndHighLightKeywords(searchQuery);
    }

    @Test
    public void textHighLighter2() throws Exception {
        String searchQuery = "Java";
        LuceneHighlighter luceneHighlighter = new LuceneHighlighter();
        luceneHighlighter.createIndex();

        luceneHighlighter.searchIndex(searchQuery);
    }
}

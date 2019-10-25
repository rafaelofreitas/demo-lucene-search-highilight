import model.LuceneMatching;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestLuceneHighLighter {

    @Test
    public void textHighLighter() throws Exception {
        String searchQuery = "Java";
        LuceneHighlighter luceneHighlighter = new LuceneHighlighter();
        ArrayList<LuceneMatching> luceneMatchings = luceneHighlighter.searchWithHighLightKeywords(searchQuery, "languages");

        ArrayList<String> words = new ArrayList<>();
        words.add("Java em Ação");
        words.add("Como o Java");

        for (int i = 0; i < words.size(); i++) {
            assertEquals(words.get(i), luceneMatchings.get(i).getIndexedPhrase());
        }

        if (luceneMatchings.size() == 1) {

            assertEquals(17, luceneMatchings.get(0).getStartPosition());
            assertEquals(23, luceneMatchings.get(0).getEndPosition());
        }
    }

    @Test
    public void textHighLighter2() throws Exception {
        String searchQuery = "linguagem java";
        LuceneHighlighter luceneHighlighter = new LuceneHighlighter();
        ArrayList<LuceneMatching> luceneMatchings = luceneHighlighter.searchWithHighLightKeywords(searchQuery, "titles");

        assertEquals("linguagem", luceneMatchings.get(0).getIndexedPhrase());

        for (int i = 0; i < luceneMatchings.size(); i++) {
            System.out.println(luceneMatchings.get(i).getStartPosition());
            System.out.println(luceneMatchings.get(i).getEndPosition());
        }
    }
}
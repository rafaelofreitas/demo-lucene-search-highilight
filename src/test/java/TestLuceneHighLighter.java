import model.LuceneMatching;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestLuceneHighLighter {

    @Test
    public void textHighLighter() throws Exception {
        String searchQuery = "Java";
        LuceneHighlighter luceneHighlighter = new LuceneHighlighter();
        ArrayList<LuceneMatching> luceneMatch = luceneHighlighter.searchWithHighLightKeywords(searchQuery, "languages");

        assertEquals("java", luceneMatch.get(0).getIndexedPhrase());

        assertEquals(0, luceneMatch.get(0).getStartPosition());
        assertEquals(4, luceneMatch.get(0).getEndPosition());
    }

    @Test
    public void textHighLighter2() throws Exception {
        String searchQuery = "linguagem java";
        LuceneHighlighter luceneHighlighter = new LuceneHighlighter();
        ArrayList<LuceneMatching> luceneMatch = luceneHighlighter.searchWithHighLightKeywords(searchQuery, "titles");

        class Indexes{
            public int starIndex;
            public int endIndex;
            public String phrase;

            Indexes(int starIndex, int endIndex, String phrase){
                this.starIndex = starIndex;
                this.endIndex = endIndex;
                this.phrase = phrase;
            }
        }

        ArrayList array = new ArrayList<>();
        array.add(new Indexes(0, 9, "linguagem"));
        array.add(new Indexes(10, 14, "java"));
        array.add(new Indexes(10, 14, "java"));

        for (int i = 0; i < luceneMatch.size(); i++) {
            Indexes indexes = (Indexes) array.get(i);
            assertEquals(indexes.starIndex, luceneMatch.get(i).getStartPosition());
            assertEquals(indexes.endIndex, luceneMatch.get(i).getEndPosition());
            assertEquals(indexes.phrase, luceneMatch.get(i).getOriginalPhrase());
        }
    }
}
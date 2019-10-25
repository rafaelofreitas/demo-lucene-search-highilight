import model.LuceneMatching;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.util.ArrayList;

public class LuceneHighlighter {
    private static final String INDEX_DIRECTORY_PATH = "index";

    public LuceneMatching searchWithHighLightKeywords(String searchQuery) throws Exception {
        this.createIndex();

        QueryParser queryParser = new QueryParser("title", new StandardAnalyzer());
        Query query = queryParser.parse(searchQuery);

        QueryScorer queryScorer = new QueryScorer(query, "title");
        SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<pre>", "<pro>");
        Highlighter highlighter = new Highlighter(htmlFormatter, queryScorer);

        Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);
        highlighter.setTextFragmenter(fragmenter);

        File indexFile = new File(INDEX_DIRECTORY_PATH);
        Directory directory = FSDirectory.open(indexFile.toPath());
        IndexReader indexReader = DirectoryReader.open(directory);

        Searcher searcher = new Searcher(INDEX_DIRECTORY_PATH);
        ScoreDoc[] scoreDocs = searcher.search(query, 10).scoreDocs;

        ArrayList<String> matching = new ArrayList<>();
        ArrayList<String> taggedPhrases = new ArrayList<>();

        for (ScoreDoc scoreDoc : scoreDocs) {
            Document document = searcher.getDocument(scoreDoc.doc);
            String title = document.get("title");
            matching.add(title);
            TokenStream tokenStream = TokenSources.getAnyTokenStream(indexReader, scoreDoc.doc, "title", document, new StandardAnalyzer());
            String markedText = highlighter.getBestFragment(tokenStream, title);

            taggedPhrases.add(markedText);
        }

        return new LuceneMatching(taggedPhrases, matching);
    }

    private void createIndex() throws Exception {
        Indexer indexer = new Indexer(INDEX_DIRECTORY_PATH);
        indexer.createIndex();
        indexer.close();
    }
}
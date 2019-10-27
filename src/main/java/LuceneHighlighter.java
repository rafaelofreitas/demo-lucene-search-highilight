import model.Constant;
import model.LuceneMatching;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
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

import static model.Constant.INDEX_DIRECTORY_PATH;

public class LuceneHighlighter {

    public ArrayList<LuceneMatching> searchWithHighLightKeywords(String searchQuery, String field) throws Exception {
        CharArraySet brArraySet = new CharArraySet(Constant.DEFAULT_REMOVED_WORDS, true);
        QueryParser queryParser = new QueryParser(field, new BrazilianAnalyzer(brArraySet));
        Query query = queryParser.parse(searchQuery);

        this.createIndex();

        SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<pre>", "<pro>");
        QueryScorer queryScorer = new QueryScorer(query, field);
        Highlighter highlighter = new Highlighter(htmlFormatter, queryScorer);

        Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);
        highlighter.setTextFragmenter(fragmenter);

        File indexFile = new File(INDEX_DIRECTORY_PATH);
        Directory directory = FSDirectory.open(indexFile.toPath());
        IndexReader indexReader = DirectoryReader.open(directory);

        Searcher searcher = new Searcher(INDEX_DIRECTORY_PATH);
        ScoreDoc[] scoreDocs = searcher.search(query, 50).scoreDocs;

        ArrayList<LuceneMatching> matchings = new ArrayList<>();
        for (ScoreDoc scoreDoc : scoreDocs) {
            Document document = searcher.getDocument(scoreDoc.doc);
            String indexedPhrase = document.get(field);

            TokenStream tokenStream = TokenSources.getAnyTokenStream(indexReader, scoreDoc.doc, field, document, new BrazilianAnalyzer(brArraySet));
            String taggedPhrase = highlighter.getBestFragment(tokenStream, indexedPhrase);

            matchings.add(new LuceneMatching(searchQuery, taggedPhrase, indexedPhrase));
        }

        return matchings;
    }

    private void createIndex() throws Exception {
        Indexer indexer = new Indexer();
        indexer.createIndex();
        indexer.close();
    }
}
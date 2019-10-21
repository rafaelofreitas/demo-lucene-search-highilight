import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;

public class LuceneHighlighter {
    private Searcher searcher;
    private static final String INDEX_DIRECTORY_PATH = "home\\rafaelfreitas\\Documentos";

    public void createIndex() throws Exception {
        Indexer indexer = new Indexer(INDEX_DIRECTORY_PATH);
        int maxDoc = indexer.createIndex();

        System.out.println("Index Created, total documents indexed: " + maxDoc);

        indexer.close();
    }

    public void searchIndex(String searchQuery) throws Exception {
        searcher = new Searcher(INDEX_DIRECTORY_PATH);
        Analyzer analyzer = new StandardAnalyzer();

        QueryParser queryParser = new QueryParser("title", analyzer);
        Query query = queryParser.parse(searchQuery);

        TopDocs topDocs = searcher.search(query, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        for (ScoreDoc scoreDoc : scoreDocs) {
            Document document = searcher.getDocument(scoreDoc.doc);
            String title = document.get("title");

            System.out.println(title);
        }
    }

    public void searchAndHighLightKeywords(String searchQuery) throws Exception {
        QueryParser queryParser = new QueryParser("title", new StandardAnalyzer());
        Query query = queryParser.parse(searchQuery);

        QueryScorer queryScorer = new QueryScorer(query, "title");
        Highlighter highlighter = new Highlighter(queryScorer);

        Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);
        highlighter.setTextFragmenter(fragmenter);

        File indexFile = new File(INDEX_DIRECTORY_PATH);
        Directory directory = FSDirectory.open(indexFile.toPath());
        IndexReader indexReader = DirectoryReader.open(directory);

        System.out.println();

        searcher = new Searcher(INDEX_DIRECTORY_PATH);
        ScoreDoc[] scoreDocs = searcher.search(query, 10).scoreDocs;

        for (ScoreDoc scoreDoc : scoreDocs) {
            Document document = searcher.getDocument(scoreDoc.doc);
            String title = document.get("title");
            TokenStream tokenStream = TokenSources.getAnyTokenStream(indexReader, scoreDoc.doc, "title", document, new StandardAnalyzer());
            String fragment = highlighter.getBestFragment(tokenStream, title);
            System.out.println(fragment);
        }
    }
}
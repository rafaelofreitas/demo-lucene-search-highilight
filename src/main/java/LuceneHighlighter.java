import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class LuceneHighlighter {
    private static Analyzer analyzer = new StandardAnalyzer();
    private static IndexWriterConfig config = new IndexWriterConfig(analyzer);
    private static Directory ramDirectory = new RAMDirectory();

    public static void main(String[] args) {
        Document doc = new Document();

        FieldType type = new FieldType();
        type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        type.setStored(true);
        type.setStoreTermVectors(true);
        type.setTokenized(true);
        type.setStoreTermVectorOffsets(true);

        Field field = new Field("title", "Nam efficitur", type);
        doc.add(field);

        Field f = new TextField("content", readFileString("content.txt"), Field.Store.YES);
        doc.add(f);

        try {
            IndexWriter indexWriter = new IndexWriter(ramDirectory, config);
            indexWriter.addDocument(doc);
            indexWriter.close();

            IndexReader idxReader = DirectoryReader.open(ramDirectory);
            IndexSearcher idxSearcher = new IndexSearcher(idxReader);

            Query queryToSearch = new QueryParser("title", analyzer).parse("Nam efficitur");

            TopDocs hits = idxSearcher.search(queryToSearch, idxReader.maxDoc());
            SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter();

            Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(queryToSearch));

            System.out.println("reader maxDoc is " + idxReader.maxDoc());
            System.out.println("scoreDoc size: " + hits.scoreDocs.length);

            for (int i = 0; i < hits.totalHits.value; i++) {
                int id = hits.scoreDocs[i].doc;

                Document docHit = idxSearcher.doc(id);
                String text = docHit.get("content");

                TokenStream tokenStream = TokenSources.getAnyTokenStream(idxReader, id, "content", analyzer);
                TextFragment[] frag = highlighter.getBestTextFragments(tokenStream, text, false, 4);

                for (TextFragment textFragment : frag) {
                    if ((textFragment != null) && (textFragment.getScore() > 0)) {
                        System.out.println((textFragment.toString()));
                    }
                }

                System.out.println("começar a destacar o título");

                text = docHit.get("title");

                tokenStream = TokenSources.getAnyTokenStream(
                        idxSearcher.getIndexReader(), hits.scoreDocs[i].doc,
                        "title", analyzer);

                frag = highlighter.getBestTextFragments(tokenStream, text, false, 4);

                for (TextFragment textFragment : frag) {
                    if ((textFragment != null) && (textFragment.getScore() > 0)) {
                        System.out.println((textFragment.toString()));
                    }
                }
            }

        } catch (IOException | ParseException | InvalidTokenOffsetsException e) {
            e.printStackTrace();
        }
    }

    private static String readFileString(String file) {
        StringBuffer text = new StringBuffer();

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file)), StandardCharsets.UTF_8));
            String line;

            while ((line = in.readLine()) != null) {
                text.append(line + "\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text.toString();
    }
}
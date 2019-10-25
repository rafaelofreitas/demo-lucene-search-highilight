import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;

/**
 * Classe para controle do índice.
 */
public class Indexer {
    private IndexWriter indexWriter;

    /**
     * O construtor instancia o objeto IndexWriter que é usado para criar o índice.
     *
     * @param indexerDirectoryPath
     * @throws Exception
     */
    protected Indexer(String indexerDirectoryPath) throws Exception {
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(OpenMode.CREATE);

        File indexFile = new File(indexerDirectoryPath);
        Directory directory = FSDirectory.open(indexFile.toPath());
        indexWriter = new IndexWriter(directory, indexWriterConfig);
    }

    /**
     * Criação de documentos à serem indexados.
     *
     * @throws Exception
     */
    protected void createIndex() throws Exception {
        String[] titles = {
                "Lucene In Action",
                "Hibernate In Action",
                "Java In Action",
                "Action Script",
                "Action that Changed the World",
                "How To Java",
                "How To C++",
                "Anroid In Action"
        };

        for (String title : titles) {
            Document document = new Document();
            document.add(new TextField("title", title, Store.YES));

            indexWriter.addDocument(document);
        }

        indexWriter.commit();
        indexWriter.numRamDocs();
    }

    protected void close() throws Exception {
        indexWriter.close();
    }
}
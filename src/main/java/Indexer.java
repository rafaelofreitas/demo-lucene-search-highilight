import model.Constant;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;

import static model.Constant.INDEX_DIRECTORY_PATH;

/**
 * Classe para controle do índice.
 */
public class Indexer {
    private IndexWriter indexWriter;

    /**
     * O construtor instancia o objeto IndexWriter que é usado para criar o índice.
     *
     * @throws Exception
     */
    protected Indexer() throws Exception {
        CharArraySet brArraySet = new CharArraySet(Constant.DEFAULT_REMOVED_WORDS, true);
        BrazilianAnalyzer brazilianAnalyzer = new BrazilianAnalyzer(brArraySet);

        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(brazilianAnalyzer);
        indexWriterConfig.setOpenMode(OpenMode.CREATE);

        File indexFile = new File(INDEX_DIRECTORY_PATH);
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
                "Lucene em Ação",
                "Hibernate em Ação",
                "Java em Ação",
                "Script em Ação",
                "Ação que mudou o mundo",
                "Como o Java",
                "Como o C++",
                "Anroid em Ação",
                "linguagem"
        };

        String[] languages = {
                "java",
                "Python",
                "Ruby",
                "JS",
                "C++",
                "C",
                "Go"
        };

        for (String title : titles) {
            Document document = new Document();
            document.add(new TextField("titles", title, Store.YES));

            indexWriter.addDocument(document);
        }

        for (String language : languages) {
            Document document = new Document();
            document.add(new TextField("languages", language, Store.YES));

            indexWriter.addDocument(document);
        }

        indexWriter.commit();
        indexWriter.numRamDocs();
    }

    protected void close() throws Exception {
        indexWriter.close();
    }
}
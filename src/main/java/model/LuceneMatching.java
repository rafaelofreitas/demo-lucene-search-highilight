package model;

public class LuceneMatching {
    private String query;
    private String originalPhrase = "";
    private String phraseWithMarkup;
    private String indexedPhrase;
    private int startPosition;
    private int endPosition;

    public LuceneMatching(String phraseWithMarkup, String indexedPhrase, String query) {
        this.indexedPhrase = indexedPhrase;
        this.phraseWithMarkup = phraseWithMarkup;
        this.query = query;

        this.setStartPosition(phraseWithMarkup);
        this.setEndPosition(phraseWithMarkup);
        this.setOriginalPhrase(phraseWithMarkup);
    }

    public String getOriginalPhrase() {
        return this.originalPhrase;
    }

    public String getPhraseWithMarkup() {
        return this.phraseWithMarkup;
    }

    public String getQuery() {
        return query;
    }

    public String getIndexedPhrase() {
        return this.indexedPhrase;
    }

    public int getStartPosition() {
        return this.startPosition;
    }

    public int getEndPosition() {
        return this.endPosition;
    }

    private void setOriginalPhrase(String phraseWithMarkup) {
        String tmp = (String) phraseWithMarkup.subSequence(this.startPosition, this.endPosition);
        int startIndex = this.query.indexOf(tmp.toLowerCase());
        this.originalPhrase = (String) query.subSequence(startIndex, tmp.length());
    }

    private void setStartPosition(String phraseWithMarkup) {
        int tmp = phraseWithMarkup.indexOf("<pre>");
        this.startPosition = tmp + 5;
    }

    private void setEndPosition(String phraseWithMarkup) {
        this.endPosition = phraseWithMarkup.indexOf("<pro>");
    }
}
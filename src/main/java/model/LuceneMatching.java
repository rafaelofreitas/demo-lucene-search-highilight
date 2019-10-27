package model;

public class LuceneMatching {
    private String query;
    private String originalPhrase;
    private String phraseWithMarkup;
    private String indexedPhrase;
    private int startPosition;
    private int endPosition;

    public LuceneMatching(String query, String phraseWithMarkup, String indexedPhrase) {
        this.query = query;
        this.phraseWithMarkup = phraseWithMarkup;
        this.indexedPhrase = indexedPhrase;

        this.setStartAndEndPosition(phraseWithMarkup);
        this.setOriginalPhrase();
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

    private void setOriginalPhrase() {
        this.originalPhrase = (String) query.subSequence(this.startPosition, this.endPosition);
    }

    private void setStartAndEndPosition(String phraseWithMarkup) {
        int tmp = phraseWithMarkup.indexOf("<pre>");
        int startIndex = tmp + 5;
        int endIndex = phraseWithMarkup.indexOf("<pro>");

        String phrase = (String) phraseWithMarkup.subSequence(startIndex, endIndex);

        this.startPosition = this.query.indexOf(phrase.toLowerCase());
        this.endPosition = this.startPosition + phrase.length();
    }
}
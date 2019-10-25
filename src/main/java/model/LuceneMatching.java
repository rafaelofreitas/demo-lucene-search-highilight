package model;

import java.util.ArrayList;

public class LuceneMatching {
    private ArrayList<String> taggedPhrases;
    private ArrayList<String> matching;

    public LuceneMatching(ArrayList<String> taggedPhrases, ArrayList<String> matching) {
        this.taggedPhrases = taggedPhrases;
        this.matching = matching;
    }

    public ArrayList<String> getTaggedPhrases() {
        return this.taggedPhrases;
    }

    public ArrayList<String> getWordsFound() {
        return this.matching;
    }

    public int getStartPosition(String phraseWithMarkup) {
        int startPosition = phraseWithMarkup.indexOf("<pre>");
        return startPosition + 5;
    }

    public int getEndPosition(String phraseWithMarkup) {
        int endPosition = phraseWithMarkup.indexOf("<pro>");
        return endPosition - 1;
    }
}
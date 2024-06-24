package com.example.littlelingo.ui.learningvocabulary;

public class Word {
    public String wordId;
    public String wordName;
    public String wordType;
    public String exampleSentences;
    public String image;
    public String audio;
    public String example1;
    public String example2;
    public String example3;

    // Default constructor required for calls to DataSnapshot.getValue(Word.class)
    public Word() {
    }

    public Word(String wordId, String wordName, String wordType, String exampleSentences,
                String image, String audio, String example1, String example2, String example3) {
        this.wordId = wordId;
        this.wordName = wordName;
        this.wordType = wordType;
        this.exampleSentences = exampleSentences;
        this.image = image;
        this.audio = audio;
        this.example1 = example1;
        this.example2 = example2;
        this.example3 = example3;
    }
}


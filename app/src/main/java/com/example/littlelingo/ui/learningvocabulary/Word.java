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
    public String getWordId() {
        return wordId;
    }
    public void setWordId(String wordId) {
        this.wordId = wordId;
    }
    public String getWordName() {
        return wordName;
    }
public void setWordName(String wordName) {
        this.wordName = wordName;

}
    public String getWordType() {
        return wordType;
    }
 public void setWordType(String wordType){
        this.wordType = wordType;
 }
    public String getExampleSentences() {
        return exampleSentences;
    }

    public void setExampleSentences(String exampleSentences) {
        this.exampleSentences = exampleSentences;
    }
    public String getImage() {
        return image;
    }
public void setImage(String image) {
        this.image = image;
}
    public String getAudio() {
        return audio;
    }
     public void setAudio(String audio) {
        this.audio = audio;
     }
    public String getExample1() {
        return example1;
    }

    public void setExample1(String example1) {
            this.example1 = example1;
    }

    public String getExample2() {
        return example2;
    }

    public void setExample2(String example2) {
        this.example2 = example2;
    }

    public String getExample3() {
        return example3;
    }
    public void setExample3(String example3) {
        this.example3 = example3;
    }

    public String getWordImage() {
        return image;
    }
}


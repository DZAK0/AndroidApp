package com.jw.flashcards;

public class Flashcard {

    private String flashcardAnswer;
    private String flashcardQuestion;

    public Flashcard() {
    }

    public Flashcard(String FiszkaPL, String FiszkaENG)
    {
        this.flashcardAnswer = FiszkaPL;
        this.flashcardQuestion = FiszkaENG;
    }

    public String getFiszkaPL() {
        return String.valueOf(flashcardAnswer);
    }

    public void setFiszkaPL(String fiszkaPL) {
        flashcardAnswer = fiszkaPL;
    }

    public String getFiszkaENG() {
        return flashcardQuestion;
    }

    public void setFiszkaENG(String fiszkaENG) {
        flashcardQuestion = fiszkaENG;
    }
}
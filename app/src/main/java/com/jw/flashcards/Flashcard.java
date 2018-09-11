package com.jw.flashcards;

public class Flashcard {

    private String FiszkaPL;
    private String FiszkaENG;

    public Flashcard() {
    }

    public Flashcard(String FiszkaPL, String FiszkaENG)
    {
        this.FiszkaPL = FiszkaPL;
        this.FiszkaENG = FiszkaENG;
    }

    public String getFiszkaPL() {
        return String.valueOf(FiszkaPL);
    }

    public void setFiszkaPL(String fiszkaPL) {
        FiszkaPL = fiszkaPL;
    }

    public String getFiszkaENG() {
        return FiszkaENG;
    }

    public void setFiszkaENG(String fiszkaENG) {
        FiszkaENG = fiszkaENG;
    }
}
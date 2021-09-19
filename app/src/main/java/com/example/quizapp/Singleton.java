package com.example.quizapp;

public class Singleton {
    String questions[];
    public static final Singleton ourInstance=new Singleton();
    public static Singleton getInstance()
    {
        return ourInstance;
    }
    public void setQuestions(String[] q)
    {
        questions=q;
    }
    public String[] getQuestions()
    {
        return questions;
    }

}

package com.jw.flashcards;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    private String Name;
    private String Password;
    private String Email;
    private String Fiszki;

    public User(String name, String password, String email, String Fiszki) {
        this.Name = name;
        this.Password = password;
        this.Email = email;
        this.Fiszki = Fiszki;
    }

    public User()
    {

    }
}
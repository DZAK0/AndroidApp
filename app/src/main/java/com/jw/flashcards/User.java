package com.jw.flashcards;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    String Name;
    String Password;
    String Email;
    String Fiszki;

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
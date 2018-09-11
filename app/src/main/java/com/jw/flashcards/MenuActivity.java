package com.jw.flashcards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private Button LogoutButtonMenu;
    private Button DodajFiszkebutton;
    private Button PrzegladajFiszki;
    private TextView HelloMenuText;
    private Button ZakonczoneFiszki;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(MenuActivity.this, LoginActivity.class));
        }

        FirebaseUser user = mAuth.getCurrentUser();

        LogoutButtonMenu = (Button) findViewById(R.id.WrocDoMenuSekcje);
        DodajFiszkebutton = (Button) findViewById(R.id.DodajFiszkeButton);
        PrzegladajFiszki = (Button) findViewById(R.id.PrzegladajFiszkiButton);
        HelloMenuText = (TextView) findViewById(R.id.HelloMenuText);
        ZakonczoneFiszki = (Button) findViewById(R.id.ZakonczoneFiszki);

        HelloMenuText.setText("Hello "+user.getEmail());

        ZakonczoneFiszki.setOnClickListener(this);
        DodajFiszkebutton.setOnClickListener(this);
        PrzegladajFiszki.setOnClickListener(this);
        LogoutButtonMenu.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Aplikacja");

    }

    @Override
    public void onClick(View view) {
        if(view == LogoutButtonMenu)
        {
            mAuth.signOut();
            finish();
            startActivity(new Intent(MenuActivity.this, LoginActivity.class));
        }
        if(view == DodajFiszkebutton)
        {
            startActivity(new Intent(MenuActivity.this, DodajFiszkeActivity.class));
        }
        if(view == ZakonczoneFiszki)
        {
            Intent i = new Intent(this, ZakonczoneFiszkiActivity.class);
            startActivity(i);
        }
        if(view == PrzegladajFiszki)
        {
            startActivity(new Intent(MenuActivity.this, SekcjaActivity.class));
        }

    }
}

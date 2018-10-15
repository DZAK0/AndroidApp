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
    private Button addFlashcardButton;
    private Button viewFlashcards;
    private Button endedFlashcards;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(MenuActivity.this, LoginActivity.class));
        }

        FirebaseUser user = mAuth.getCurrentUser();

        LogoutButtonMenu = (Button) findViewById(R.id.WrocDoMenuSekcje);
        addFlashcardButton = (Button) findViewById(R.id.DodajFiszkeButton);
        viewFlashcards = (Button) findViewById(R.id.PrzegladajFiszkiButton);
        TextView helloMenuText = (TextView) findViewById(R.id.HelloMenuText);
        endedFlashcards = (Button) findViewById(R.id.ZakonczoneFiszki);


        endedFlashcards.setOnClickListener(this);
        addFlashcardButton.setOnClickListener(this);
        viewFlashcards.setOnClickListener(this);
        LogoutButtonMenu.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.app);

    }

    @Override
    public void onClick(View view) {
        if(view == LogoutButtonMenu)
        {
            mAuth.signOut();
            finish();
            startActivity(new Intent(MenuActivity.this, LoginActivity.class));
        }
        if(view == addFlashcardButton)
        {
            startActivity(new Intent(MenuActivity.this, AddFlashcardActivity.class));
        }
        if(view == endedFlashcards)
        {
            Intent i = new Intent(this, FinishedFlashcardActivity.class);
            startActivity(i);
        }
        if(view == viewFlashcards)
        {
            startActivity(new Intent(MenuActivity.this, SectionActivity.class));
        }

    }
}

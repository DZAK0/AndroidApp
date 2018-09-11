package com.jw.flashcards;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class SekcjaActivity extends AppCompatActivity implements View.OnClickListener {

    private Button WrocDoMenu;
    private Button Sekcja1;
    private Button Sekcja2;
    private Button Sekcja3;
    private Button Sekcja4;
    private Button Sekcja5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sekcje);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Aplikacja");

        WrocDoMenu = (Button) findViewById(R.id.WrocDoMenuSekcje);
        Sekcja1 = (Button) findViewById(R.id.Sekcja1);
        Sekcja2 = (Button) findViewById(R.id.Sekcja2);
        Sekcja3 = (Button) findViewById(R.id.Sekcja3);
        Sekcja4 = (Button) findViewById(R.id.Sekcja4);
        Sekcja5 = (Button) findViewById(R.id.Sekcja5);

        WrocDoMenu.setOnClickListener(this);
        Sekcja1.setOnClickListener(this);
        Sekcja2.setOnClickListener(this);
        Sekcja3.setOnClickListener(this);
        Sekcja4.setOnClickListener(this);
        Sekcja5.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view == WrocDoMenu)
        {
            finish();
            startActivity(new Intent(SekcjaActivity.this, MenuActivity.class));
        }
        if (view == Sekcja1){
            Intent i = new Intent(this, PrzegladajFiszkiActivity.class);
            i.putExtra("Sekcja", 1);
            startActivity(i);
        }
        if (view == Sekcja2){
            Intent i = new Intent(this, PrzegladajFiszkiActivity.class);
            i.putExtra("Sekcja", 2);
            startActivity(i);
        }
        if (view == Sekcja3){
            Intent i = new Intent(this, PrzegladajFiszkiActivity.class);
            i.putExtra("Sekcja", 3);
            startActivity(i);
        }
        if (view == Sekcja4){
            Intent i = new Intent(this, PrzegladajFiszkiActivity.class);
            i.putExtra("Sekcja", 4);
            startActivity(i);
        }
        if (view == Sekcja5){
            Intent i = new Intent(this, PrzegladajFiszkiActivity.class);
            i.putExtra("Sekcja", 5);
            startActivity(i);
        }
    }
}
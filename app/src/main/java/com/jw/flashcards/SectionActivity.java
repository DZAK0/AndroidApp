package com.jw.flashcards;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class SectionActivity extends AppCompatActivity implements View.OnClickListener {

    private Button goBack;
    private Button section1;
    private Button section2;
    private Button section3;
    private Button section4;
    private Button section5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.section_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.app);

        goBack = (Button) findViewById(R.id.WrocDoMenuSekcje);
        section1 = (Button) findViewById(R.id.Sekcja1);
        section2 = (Button) findViewById(R.id.Sekcja2);
        section3 = (Button) findViewById(R.id.Sekcja3);
        section4 = (Button) findViewById(R.id.Sekcja4);
        section5 = (Button) findViewById(R.id.Sekcja5);

        goBack.setOnClickListener(this);
        section1.setOnClickListener(this);
        section2.setOnClickListener(this);
        section3.setOnClickListener(this);
        section4.setOnClickListener(this);
        section5.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view == goBack)
        {
            finish();
            startActivity(new Intent(SectionActivity.this, MenuActivity.class));
        }
        if (view == section1){
            Intent i = new Intent(this, ChoseSectionActivity.class);
            i.putExtra("Sekcja", 1);
            startActivity(i);
        }
        if (view == section2){
            Intent i = new Intent(this, ChoseSectionActivity.class);
            i.putExtra("Sekcja", 2);
            startActivity(i);
        }
        if (view == section3){
            Intent i = new Intent(this, ChoseSectionActivity.class);
            i.putExtra("Sekcja", 3);
            startActivity(i);
        }
        if (view == section4){
            Intent i = new Intent(this, ChoseSectionActivity.class);
            i.putExtra("Sekcja", 4);
            startActivity(i);
        }
        if (view == section5){
            Intent i = new Intent(this, ChoseSectionActivity.class);
            i.putExtra("Sekcja", 5);
            startActivity(i);
        }
    }
}
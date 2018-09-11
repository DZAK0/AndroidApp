package com.jw.flashcards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DodajFiszkeActivity extends AppCompatActivity{

    private TextView WpiszPoPolskuTextView;
    private TextView WpiszPoAngielskuTextView;
    private Button WrocDomenuDodajFiszke;
    private EditText FiszkaPoPolsku;
    private EditText FiszkaPoAngielsku;
    private Button DodajFiszke;
    private String PoPolsku;
    private String PoAngielsku;


    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodaj_fiszke_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Wroc do menu");



        //DATABASE

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();
        userID = user.getUid().toString();

        //END DATABASE

        FiszkaPoPolsku = (EditText) findViewById(R.id.FiszkaPoPolsku);
        FiszkaPoAngielsku = (EditText) findViewById(R.id.FiszkaPoAngielsku);
        WrocDomenuDodajFiszke = (Button) findViewById(R.id.WrocDoMenuDodajFiszke);
        DodajFiszke = (Button) findViewById(R.id.DodajFiszkeButton);

        WrocDomenuDodajFiszke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(DodajFiszkeActivity.this, MenuActivity.class));
            }
        });


        DodajFiszke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dodaj();
            }
        });

    }

    private void Dodaj() {

        PoPolsku = FiszkaPoPolsku.getText().toString();
        PoAngielsku = FiszkaPoAngielsku.getText().toString();

        if(PoPolsku.isEmpty() && PoAngielsku.isEmpty())
        {
            Toast.makeText(DodajFiszkeActivity.this, "Wprowadz dane!", Toast.LENGTH_SHORT).show();
        }
        else if (PoPolsku.isEmpty()) {
            Toast.makeText(DodajFiszkeActivity.this, "Wprowadz fiszke po polski!", Toast.LENGTH_SHORT).show();
            return;
        } else if (PoAngielsku.isEmpty()) {
            Toast.makeText(DodajFiszkeActivity.this, "Wprowadz fiszke po angielsku!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {

            Flashcard flashcard = new Flashcard(PoPolsku, PoAngielsku);
            String id = mDatabase.push().getKey();
            mDatabase.child("users").child(userID).child("Fiszki").child("Fiszki_1").child(id).setValue(flashcard);
            //mDatabase.child(userID).child("Fiszki").child("Angielski").setValue(PoAngielsku);
            Toast.makeText(this, "Fiszka dodana!", Toast.LENGTH_SHORT).show();
        }
    }
}
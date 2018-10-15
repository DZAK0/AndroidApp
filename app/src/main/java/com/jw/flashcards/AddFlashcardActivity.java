package com.jw.flashcards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFlashcardActivity extends AppCompatActivity{

    private EditText FlashcardOne;
    private EditText FlashcardTwo;


    private DatabaseReference mDatabase;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_flashcard_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Go Back");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        FlashcardOne = (EditText) findViewById(R.id.FiszkaPoPolsku);
        FlashcardTwo = (EditText) findViewById(R.id.FiszkaPoAngielsku);
        Button goBackToMenu = (Button) findViewById(R.id.WrocDoMenuDodajFiszke);
        Button addFlashcard = (Button) findViewById(R.id.DodajFiszkeButton);

        goBackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(AddFlashcardActivity.this, MenuActivity.class));
            }
        });


        addFlashcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Add();
            }
        });

    }

    private void Add() {

        String flashcardOneAnswer = FlashcardOne.getText().toString();
        String flashcardTwoAnswer = FlashcardTwo.getText().toString();

        if(flashcardOneAnswer.isEmpty() && flashcardTwoAnswer.isEmpty())
        {
            Toast.makeText(AddFlashcardActivity.this, R.string.emptyFieldToast, Toast.LENGTH_SHORT).show();
        }
        else if (flashcardOneAnswer.isEmpty()) {
            Toast.makeText(AddFlashcardActivity.this,  R.string.emptyFieldToast, Toast.LENGTH_SHORT).show();
            return;
        } else if (flashcardTwoAnswer.isEmpty()) {
            Toast.makeText(AddFlashcardActivity.this,  R.string.emptyFieldToast, Toast.LENGTH_SHORT).show();
            return;
        }
        else {

            Flashcard flashcard = new Flashcard(flashcardOneAnswer, flashcardTwoAnswer);
            String id = mDatabase.push().getKey();
            mDatabase.child("users").child(userID).child("Fiszki").child("Fiszki_1").child(id).setValue(flashcard);
            Toast.makeText(this,  R.string.FlashcardAdded, Toast.LENGTH_SHORT).show();
        }
    }
}
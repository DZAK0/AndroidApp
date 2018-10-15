package com.jw.flashcards;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class FinishedFlashcardActivity extends AppCompatActivity {

    private DatabaseReference mDataBaseOver;
    private DatabaseReference mDataBaseLower;
    private ProgressDialog pd;
    private int i=0;
    private List<String> keys = new LinkedList<>();
    private List<Flashcard> flashcards = new LinkedList<>();
    private TextView answerText;
    private EditText questionText;
    private TextView correct;
    private String enteredText;
    private String answerString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.pdWaitMessage));
        pd.show();
        pd.setCancelable(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completed_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        mDataBaseOver = mFirebaseDatabase.getReference("users/"+ userID +"/Fiszki/Known");
        mDataBaseLower = mFirebaseDatabase.getReference("users/"+ userID +"/Fiszki/Fiszki_5");

        questionText = (EditText) findViewById(R.id.TextPoAngielskuEdittextKoniec);
        answerText = (TextView) findViewById(R.id.WyswietlTekstPoPolskuKoniec);
        Button doKnowButton = (Button) findViewById(R.id.WiemButtonKoniec);
        Button dontKnowButton = (Button) findViewById(R.id.NieWiemButtonKoniec);
        Button showFlashcardButton = (Button) findViewById(R.id.PokazButtonKoniec);
        correct = (TextView) findViewById(R.id.PoprawneTlumaczenieKoniec);


        mDataBaseOver.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
                pd.dismiss();
                Dialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        showFlashcardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flashcards.isEmpty())
                {
                    Toast.makeText(FinishedFlashcardActivity.this, R.string.emptyFieldToast, Toast.LENGTH_SHORT).show();
                }
                else {
                    answerString = flashcards.get(i).getFiszkaENG();
                    i++;
                    correct.setText(answerString);
                    enteredText = questionText.getText().toString();
                    if(enteredText.equals(answerString))
                    {
                        Toast.makeText(FinishedFlashcardActivity.this, R.string.correctToast, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        doKnowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(flashcards.isEmpty())
                {
                    Toast.makeText(FinishedFlashcardActivity.this, R.string.emptyFieldToast, Toast.LENGTH_SHORT).show();
                }
                else {
                    if (i >= flashcards.size()) {
                        i=1;
                        dialogEndMessage();
                    }
                    answerText.setText(flashcards.get(i).getFiszkaPL());
                }
            }
        });

        dontKnowButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if((correct.equals("")||(answerString.isEmpty())))
                {
                    Toast.makeText(FinishedFlashcardActivity.this, R.string.tryToast, Toast.LENGTH_SHORT).show();
                }
                else{
                    if (i >= flashcards.size()) {
                        GoLower(5, (i-1));
                        i=1;
                        dialogEndMessage();
                    }
                    else {
                        GoLower(5, (i - 1));
                        answerText.setText(flashcards.get(i).getFiszkaPL());
                    }
                }
            }
        });

    }

    private void showData(DataSnapshot dataSnapshot)
    {
        for(DataSnapshot ds : dataSnapshot.getChildren())
        {
            Map<String, String> mapOfFlashcards = (Map<String, String>) ds.getValue();
            String answer = mapOfFlashcards.get("fiszkaPL");
            String question = mapOfFlashcards.get("fiszkaENG");
            keys.add(ds.getKey());
            Flashcard flashcard = new Flashcard(mapOfFlashcards.get("fiszkaPL"), mapOfFlashcards.get("fiszkaENG"));
            flashcards.add(flashcard);
        }
    }

    private void GoLower(int sekcja, int value) {
        if (5>1){
            Flashcard flashcard = new Flashcard(flashcards.get(value).getFiszkaPL().toString(), flashcards.get(value).getFiszkaENG().toString());
            mDataBaseLower.child(keys.get(value).toString()).setValue(flashcard);
            mDataBaseOver.child(keys.get(value).toString()).removeValue();
        }
    }

    public void Dialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FinishedFlashcardActivity.this);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle(R.string.dialogReady);
        dialogBuilder.setPositiveButton(R.string.dialogStart, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(flashcards.isEmpty()){
                    Toast.makeText(FinishedFlashcardActivity.this, R.string.emptyFieldToast, Toast.LENGTH_SHORT).show();
                }
                else {
                    answerText.setText(flashcards.get(0).getFiszkaPL());
                }
            }
        });
        dialogBuilder.setNegativeButton(R.string.goBack, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                startActivity(new Intent(FinishedFlashcardActivity.this, MenuActivity.class));
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    public void dialogEndMessage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FinishedFlashcardActivity.this);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle(R.string.dialogEnd);
        dialogBuilder.setMessage(R.string.dialogEndMore);
        dialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                startActivity(new Intent(FinishedFlashcardActivity.this, SectionActivity.class));
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }
}

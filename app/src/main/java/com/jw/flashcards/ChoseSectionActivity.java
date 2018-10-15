package com.jw.flashcards;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ChoseSectionActivity extends AppCompatActivity {

    private TextView answer;
    private EditText question;
    private TextView goodAnswer;
    private int i=0;
    private String enteredAnswer;
    private String translation;
    private List<String> keys = new LinkedList<>();
    private List<Flashcard> flashcards = new LinkedList<>();

    //DATABASE

    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDataBaseHigher;
    private DatabaseReference mDataBaseOver;
    private DatabaseReference mDatabase;
    private DatabaseReference mDataBaseLower;

    private int section;
    private ProgressDialog pd;

    protected void onCreate(Bundle savedInstanceState) {

        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.pdWaitMessage));
        pd.show();
        pd.setCancelable(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chose_section_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            section = bundle.getInt("Sekcja");
        }

        mDatabase = mFirebaseDatabase.getReference("users/"+ userID +"/Fiszki/Fiszki_"+section);
        mDataBaseHigher = mFirebaseDatabase.getReference("users/"+ userID +"/Fiszki/Fiszki_"+(section+1));
        mDataBaseOver = mFirebaseDatabase.getReference("users/"+ userID +"/Fiszki/Known");
        mDataBaseLower = mFirebaseDatabase.getReference("users/"+ userID +"/Fiszki/Fiszki_"+(section-1));

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
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

        Button dontKnowButton = (Button) findViewById(R.id.NieWiemButtonKoniec);
        Button doKnowButton = (Button) findViewById(R.id.WiemButton);
        answer = (TextView) findViewById(R.id.WyswietlTekstPoPolskuKoniec);
        question = (EditText) findViewById(R.id.TextPoAngielskuEditText);
        Button showFlashcardButton = (Button) findViewById(R.id.PokazFiszkeButton);
        goodAnswer = (TextView) findViewById(R.id.PoprawneTlumaczenieKoniec);


        doKnowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(flashcards.isEmpty())
                {
                    Toast.makeText(ChoseSectionActivity.this, R.string.emptyFieldToast, Toast.LENGTH_SHORT).show();
                }
                else {
                    if (i >= flashcards.size()) {
                        GoHigher(section,(i-1));
                        dialogMessageEnd();
                    }
                    else {
                        GoHigher(section, (i - 1));
                        answer.setText(flashcards.get(i).getFiszkaPL());
                    }
                }
            }
        });

        showFlashcardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flashcards.isEmpty())
                {
                    Toast.makeText(ChoseSectionActivity.this, R.string.emptyFieldToast, Toast.LENGTH_SHORT).show();
                }
                else {
                    translation = flashcards.get(i).getFiszkaENG();
                    i++;
                    goodAnswer.setText(translation);
                    enteredAnswer = question.getText().toString();
                    if(enteredAnswer.equals(translation))
                    {
                        Toast.makeText(ChoseSectionActivity.this, R.string.correctToast, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        dontKnowButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if((goodAnswer.equals("")||(translation.isEmpty())))
                {
                    Toast.makeText(ChoseSectionActivity.this, R.string.tryToast, Toast.LENGTH_SHORT).show();
                }
                else{
                    if (i >= flashcards.size()) {
                        GoLower(section, (i - 1));
                        dialogMessageEnd();
                    }
                    else {
                        GoLower(section, (i-1));
                        answer.setText(flashcards.get(i).getFiszkaPL());
                    }
                }
            }
        });

    }

    private void GoLower(int sekcja, int value) {
        if (sekcja>1){
            Flashcard flashcard = new Flashcard(flashcards.get(value).getFiszkaPL().toString(), flashcards.get(value).getFiszkaENG().toString());
            mDataBaseLower.child(keys.get(value).toString()).setValue(flashcard);
            mDatabase.child(keys.get(value).toString()).removeValue();
        }
    }


    private void showData(DataSnapshot dataSnapshot)
    {
        for(DataSnapshot ds : dataSnapshot.getChildren())
        {
            Map<String, String> mapOfFlashcards = (Map<String, String>) ds.getValue();
            String answerFlashcard = mapOfFlashcards.get("fiszkaPL");
            String questionFlashcard = mapOfFlashcards
                    .get("fiszkaENG");
            keys.add(ds.getKey());
            Flashcard flashcard = new Flashcard(mapOfFlashcards.get("fiszkaPL"), mapOfFlashcards.get("fiszkaENG"));
            flashcards.add(flashcard);
        }
    }

    private void GoHigher(int sekcja, int value){
        if (sekcja<5){
            Flashcard flashcard = new Flashcard(flashcards.get(value).getFiszkaPL().toString(), flashcards.get(value).getFiszkaENG().toString());
            mDatabase.child(keys.get(value).toString()).removeValue();
            mDataBaseHigher.child(keys.get(value).toString()).setValue(flashcard);
        }
        if (sekcja==5){
            Flashcard flashcard = new Flashcard(flashcards.get(value).getFiszkaPL().toString(), flashcards.get(value).getFiszkaENG().toString());
            mDatabase.child(keys.get(value).toString()).removeValue();
            mDataBaseOver.child(keys.get(value).toString()).setValue(flashcard);
        }
    }

    public void Dialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ChoseSectionActivity.this);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle(R.string.dialogReady);
        dialogBuilder.setPositiveButton(R.string.dialogStart, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(flashcards.isEmpty()){
                    Toast.makeText(ChoseSectionActivity.this, R.string.emptyFieldToast, Toast.LENGTH_SHORT).show();
                }
                else {
                    answer.setText(flashcards.get(0).getFiszkaPL());
                }
            }
        });
        dialogBuilder.setNegativeButton(R.string.goBack, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                startActivity(new Intent(ChoseSectionActivity.this, MenuActivity.class));
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    public void dialogMessageEnd(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ChoseSectionActivity.this);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle(R.string.dialogEnd);
        dialogBuilder.setMessage(R.string.dialogEndMore);
        dialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                startActivity(new Intent(ChoseSectionActivity.this, SectionActivity.class));
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }


}
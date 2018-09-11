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
import android.util.Log;
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

public class PrzegladajFiszkiActivity extends AppCompatActivity {

    private Button Wroc;
    private Button NieWiem;
    private Button Wiem;
    private Button PokazFiszkeButton;
    private TextView TekstPoPolsku;
    private EditText TekstPoAngielsku;
    private TextView PoprawneTlumaczenie;
    private int i=0;
    private int j=0;
    private String polski;
    private String angielski;
    private String wpisany;
    private String tlumaczenie;
    private Map<String, String> map;
    private List<String> klucze = new LinkedList<>();
    private List<Flashcard> fiszki = new LinkedList<>();

    //DATABASE

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;

    private DatabaseReference mDataBaseHigher;
    private DatabaseReference mDataBaseOver;
    private DatabaseReference mDatabase;
    private DatabaseReference mDataBaseLower;

    private String userID;
    private int sekcja;
    private ProgressDialog pd;
    //DATABASE END

    //TREST
    protected void onCreate(Bundle savedInstanceState) {

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();
        pd.setCancelable(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.przegladaj_fiszki);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();
        userID = user.getUid();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            sekcja = bundle.getInt("Sekcja");
        }

        mDatabase = mFirebaseDatabase.getReference("users/"+userID+"/Fiszki/Fiszki_"+sekcja);
        mDataBaseHigher = mFirebaseDatabase.getReference("users/"+userID+"/Fiszki/Fiszki_"+(sekcja+1));
        mDataBaseOver = mFirebaseDatabase.getReference("users/"+userID+"/Fiszki/Known");
        mDataBaseLower = mFirebaseDatabase.getReference("users/"+userID+"/Fiszki/Fiszki_"+(sekcja-1));

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
                pd.dismiss();
                Dialog();
                //Log.d("TESTVALUE", "jakas wartosc?? "+dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        NieWiem = (Button) findViewById(R.id.NieWiemButton);
        Wiem = (Button) findViewById(R.id.WiemButton);
        TekstPoPolsku = (TextView) findViewById(R.id.WyswietlTekstPoPolsku);
        TekstPoAngielsku = (EditText) findViewById(R.id.TextPoAngielskuEditText);
        PokazFiszkeButton = (Button) findViewById(R.id.PokazFiszkeButton);
        PoprawneTlumaczenie = (TextView) findViewById(R.id.PoprawneTlumaczenie);


        Wiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fiszki.isEmpty())
                {
                    Toast.makeText(PrzegladajFiszkiActivity.this, "Najpierw dodaj jakies fiszki!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (i >= fiszki.size()) {
                        GoHigher(sekcja,(i-1));
                        i=1;
                        Dialog_Koniec();
                    }
                    GoHigher(sekcja, (i-1));
                    TekstPoPolsku.setText(fiszki.get(i).getFiszkaPL());
                }
            }
        });

        PokazFiszkeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fiszki.isEmpty())
                {
                    Toast.makeText(PrzegladajFiszkiActivity.this, "Najpierw dodaj jakies fiszki!", Toast.LENGTH_SHORT).show();
                }
                else {
                    tlumaczenie = fiszki.get(i).getFiszkaENG();
                    i++;
                    PoprawneTlumaczenie.setText(tlumaczenie);
                    wpisany = TekstPoAngielsku.getText().toString();
                    if(wpisany.equals(tlumaczenie))
                    {
                        Toast.makeText(PrzegladajFiszkiActivity.this, "Łaria approved!!", Toast.LENGTH_LONG).show();
                        Log.d("TESTVALUE", "LARIA");
                    }
                }
            }
        });

        NieWiem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if((PoprawneTlumaczenie.equals("")&&(tlumaczenie.isEmpty())))
                {
                    Toast.makeText(PrzegladajFiszkiActivity.this, "Najpierw sprawdz slowko!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (i >= fiszki.size()) {
                        GoLower(sekcja, (i-1));
                        i=1;
                        Dialog_Koniec();
                    }
                    GoLower(sekcja, (i-1));
                    TekstPoPolsku.setText(fiszki.get(i).getFiszkaPL());
                }
            }
        });

    }

    private void GoLower(int sekcja, int value) {
        if (sekcja>1){
            Flashcard flashcard = new Flashcard(fiszki.get(value).getFiszkaPL().toString(), fiszki.get(value).getFiszkaENG().toString());
            mDataBaseLower.child(klucze.get(value).toString()).setValue(flashcard);
            mDatabase.child(klucze.get(value).toString()).removeValue();
        }
    }


    private void showData(DataSnapshot dataSnapshot)
    {
        for(DataSnapshot ds : dataSnapshot.getChildren())
        {
            map = (Map<String, String>) ds.getValue();
            polski = map.get("fiszkaPL");
            angielski = map.get("fiszkaENG");
            klucze.add(ds.getKey());
            Flashcard flashcard = new Flashcard(map.get("fiszkaPL"), map.get("fiszkaENG"));
            fiszki.add(flashcard);
        }
    }

    private void GoHigher(int sekcja, int value){
        if (sekcja<5){
            Flashcard flashcard = new Flashcard(fiszki.get(value).getFiszkaPL().toString(), fiszki.get(value).getFiszkaENG().toString());
            mDatabase.child(klucze.get(value).toString()).removeValue();
            mDataBaseHigher.child(klucze.get(value).toString()).setValue(flashcard);
        }
        if (sekcja==5){
            Flashcard flashcard = new Flashcard(fiszki.get(value).getFiszkaPL().toString(), fiszki.get(value).getFiszkaENG().toString());
            mDatabase.child(klucze.get(value).toString()).removeValue();
            mDataBaseOver.child(klucze.get(value).toString()).setValue(flashcard);
        }
    }

    public void Dialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PrzegladajFiszkiActivity.this);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle("Jestes gotow?");
        dialogBuilder.setPositiveButton("Rozpocznij", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TekstPoPolsku.setText(fiszki.get(0).getFiszkaPL());
            }
        });
        dialogBuilder.setNegativeButton("Wroc", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                startActivity(new Intent(PrzegladajFiszkiActivity.this, MenuActivity.class));
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    public void Dialog_Koniec(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PrzegladajFiszkiActivity.this);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle("To juz koniec....");
        dialogBuilder.setMessage("...wroc do wyboru fiszek");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                startActivity(new Intent(PrzegladajFiszkiActivity.this, SekcjaActivity.class));
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }


}
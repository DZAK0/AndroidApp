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

import org.w3c.dom.Text;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class ZakonczoneFiszkiActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDataBaseOver;
    private DatabaseReference mDataBaseLower;
    private String userID;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private ProgressDialog pd;
    private int sekcja;
    private int i=0;
    private Map<String, String> map;
    private List<String> klucze = new LinkedList<>();
    private List<Flashcard> fiszki = new LinkedList<>();
    private String polski;
    private String angielski;
    private Button NieWiem;
    private Button Wiem;
    private Button PokazFiszkeButton;
    private TextView TekstPoPolsku;
    private EditText TekstPoAngielsku;
    private TextView PoprawneTlumaczenie;
    private String wpisany;
    private String tlumaczenie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();
        pd.setCancelable(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zakonczone_fiszki);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();
        userID = user.getUid();
        mDataBaseOver = mFirebaseDatabase.getReference("users/"+userID+"/Fiszki/Known");
        mDataBaseLower = mFirebaseDatabase.getReference("users/"+userID+"/Fiszki/Fiszki_5");

        TekstPoAngielsku = (EditText) findViewById(R.id.TextPoAngielskuEdittextKoniec);
        TekstPoPolsku = (TextView) findViewById(R.id.WyswietlTekstPoPolskuKoniec);
        Wiem = (Button) findViewById(R.id.WiemButtonKoniec);
        NieWiem = (Button) findViewById(R.id.NieWiemButtonKoniec);
        PokazFiszkeButton = (Button) findViewById(R.id.PokazButtonKoniec);
        PoprawneTlumaczenie = (TextView) findViewById(R.id.PoprawneTlumaczenieKoniec);


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

        PokazFiszkeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fiszki.isEmpty())
                {
                    Toast.makeText(ZakonczoneFiszkiActivity.this, "Najpierw dodaj jakies fiszki!", Toast.LENGTH_SHORT).show();
                }
                else {
                    tlumaczenie = fiszki.get(i).getFiszkaENG();
                    i++;
                    PoprawneTlumaczenie.setText(tlumaczenie);
                    wpisany = TekstPoAngielsku.getText().toString();
                    if(wpisany.equals(tlumaczenie))
                    {
                        Toast.makeText(ZakonczoneFiszkiActivity.this, "Łaria approved!!", Toast.LENGTH_LONG).show();
                        Log.d("TESTVALUE", "LARIA");
                    }
                }
            }
        });

        Wiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fiszki.isEmpty())
                {
                    Toast.makeText(ZakonczoneFiszkiActivity.this, "Najpierw dodaj jakies fiszki!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (i >= fiszki.size()) {
                        i=1;
                        Dialog_Koniec();
                    }
                    TekstPoPolsku.setText(fiszki.get(i).getFiszkaPL());
                }
            }
        });

        NieWiem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if((PoprawneTlumaczenie.equals("")||(tlumaczenie.isEmpty())))
                {
                    Toast.makeText(ZakonczoneFiszkiActivity.this, "Najpierw sprawdz slowko!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (i >= fiszki.size()) {
                        GoLower(5, (i-1));
                        i=1;
                        Dialog_Koniec();
                    }
                    else {
                        GoLower(5, (i - 1));
                        TekstPoPolsku.setText(fiszki.get(i).getFiszkaPL());
                    }
                }
            }
        });

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

    private void GoLower(int sekcja, int value) {
        if (sekcja>1){
            Flashcard flashcard = new Flashcard(fiszki.get(value).getFiszkaPL().toString(), fiszki.get(value).getFiszkaENG().toString());
            mDataBaseLower.child(klucze.get(value).toString()).setValue(flashcard);
            mDataBaseOver.child(klucze.get(value).toString()).removeValue();
        }
    }

    public void Dialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ZakonczoneFiszkiActivity.this);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle("Jestes gotow?");
        dialogBuilder.setPositiveButton("Rozpocznij", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(fiszki.isEmpty()){
                    Toast.makeText(ZakonczoneFiszkiActivity.this, "Najpierw dodaj jakies fiszki!", Toast.LENGTH_SHORT).show();
                }
                else {
                    TekstPoPolsku.setText(fiszki.get(0).getFiszkaPL());
                }
            }
        });
        dialogBuilder.setNegativeButton("Wroc", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                startActivity(new Intent(ZakonczoneFiszkiActivity.this, MenuActivity.class));
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    public void Dialog_Koniec(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ZakonczoneFiszkiActivity.this);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle("To juz koniec....");
        dialogBuilder.setMessage("...wroc do wyboru fiszek");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                startActivity(new Intent(ZakonczoneFiszkiActivity.this, SekcjaActivity.class));
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }
}

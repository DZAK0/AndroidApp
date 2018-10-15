package com.jw.flashcards;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {


    private EditText UsernameRegister;
    private EditText PasswordRegister;
    private EditText EmailRegister;
    private Button RegisterButton;
    private FirebaseAuth mAuth;
    private TextView goBackToLogin;
    private String login;
    private String password;
    private String email;
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        UsernameRegister = (EditText) findViewById(R.id.UsernameRegister);
        PasswordRegister = (EditText) findViewById(R.id.PasswordRegister);
        EmailRegister = (EditText) findViewById(R.id.EmailRegister);
        RegisterButton = (Button) findViewById(R.id.ButtonRegister);
        goBackToLogin = (TextView) findViewById(R.id.WrocDoLogowania);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null)
        {
            //start profile activity
            finish();
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
        }

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Regiser();
            }
        });

        goBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }



    private void Regiser()
    {
        login = UsernameRegister.getText().toString();
        password = PasswordRegister.getText().toString();
        email = EmailRegister.getText().toString();

        if (email.isEmpty())
        {
            Toast.makeText(this, R.string.enterEmailToast, Toast.LENGTH_SHORT).show();
            return;
        }
        else if (password.isEmpty())
        {
            Toast.makeText(this, R.string.enterPasswordToast, Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            SetUserOnDatabase();
                            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this, R.string.errorRegisterToast, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void SetUserOnDatabase()
    {
        User userName = new User(login, password, email, "Fiszki");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        mDatabase.child("users").child(userID).setValue(userName);
    }
}
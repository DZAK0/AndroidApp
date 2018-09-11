package com.jw.flashcards;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;




public class LoginActivity extends AppCompatActivity {

    private EditText EmailLogin;
    private EditText PasswrodLogin;
    private Button LoginButton;
    private TextView ZarejestrujSieLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        EmailLogin = (EditText) findViewById(R.id.EmailLogin);
        PasswrodLogin = (EditText) findViewById(R.id.PasswordLogin);
        ZarejestrujSieLogin = (TextView) findViewById(R.id.ZarejestrujSieLogin);
        LoginButton = (Button) findViewById(R.id.LoginButton);
        mAuth = FirebaseAuth.getInstance();

        //Check is user is already loggin
        if(mAuth.getCurrentUser() != null)
        {
            //start profile activity
            finish();
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
        }

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        ZarejestrujSieLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void userLogin()
    {
        String email = EmailLogin.getText().toString();
        String password = PasswrodLogin.getText().toString();

        if (email.isEmpty())
        {
            Toast.makeText(this, "Please enter email!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (password.isEmpty())
        {
            Toast.makeText(this, "Please enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful())
                        {
                            //start the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Uzytkownik o podanych dancyh nie istnieje!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void PrzejdzDoRejestracji(View view) {
        finish();
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }
}
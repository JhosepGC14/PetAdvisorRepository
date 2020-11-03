package com.example.petadvisormainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextView registerOnLogin;
    EditText emailLogin, passwordLogin;
    Button buttonLogin;
    ProgressBar progressBar;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();


        //declarando la variable del link hacia el register
        registerOnLogin = findViewById(R.id.linkRegisterOnLogin);
        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        buttonLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBarLogin);
        fAuth = FirebaseAuth.getInstance();

        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String email = emailLogin.getText().toString().trim();
                String password = passwordLogin.getText().toString().trim();

                //validaciones si email o password estan vacios
                if (TextUtils.isEmpty(email)) {
                    emailLogin.setError("Email es requerido.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordLogin.setError("Password es requerido.");
                    return;
                }

                if (password.length() > 6) {
                    progressBar.setVisibility(View.VISIBLE);

                    //llamada al Register user en firebase
                    //classe de firebase para el registro de usuarios.
                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Bienvenido a Pet Advisor !!.", Toast.LENGTH_SHORT).show();
                                if (fAuth.getCurrentUser() != null) {
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                } else {
                                    return;
                                }
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, task.getException().toString() , Toast.LENGTH_SHORT).show();
                            }
                        }
                    }));
                } else {
                    passwordLogin.setError("La contraseña debe ser mayor a 6 dígitos.");
                    return;

                }

            }
        });

        //evento para redireccionar al login
        registerOnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });
    }
}
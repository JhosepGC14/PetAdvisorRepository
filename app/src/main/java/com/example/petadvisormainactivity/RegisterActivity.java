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

public class RegisterActivity extends AppCompatActivity {

    // 1.-primero se declara las variables con el tipo de dato o view.
    EditText namesRegister, lastNameRegister, emailRegister, passwordRegister, repeatPasswordRegister;
    Button buttonRegister;
    TextView loginTextOnRegister;
    FirebaseAuth fAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        // 2.- Segundo se instancia las variables con el id del elemento en el layout con el metodo findViewById
        //datos a ingresar (instancias)
        namesRegister = findViewById(R.id.nameClientRegister);
        lastNameRegister = findViewById(R.id.lastNameClientRegister);
        emailRegister = findViewById(R.id.emailRegister);
        passwordRegister = findViewById(R.id.passwordPassword);
        repeatPasswordRegister = findViewById(R.id.repeatPasswordRegister);
        buttonRegister = findViewById(R.id.btnRegister);
        loginTextOnRegister = findViewById(R.id.linkLoginOnRegister);

        //firebase instancias y el progress bar
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBarRegister);


        //evento para redireccionar al login
        loginTextOnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        //declaramos la clase que dara la accion onClick en el Boton "Registrar",
        // y algunas validaciones

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailRegister.getText().toString().trim();
                String password = passwordRegister.getText().toString().trim();
                String repassword = repeatPasswordRegister.getText().toString().trim();

                //validaciones si email o password estan vacios
                if (TextUtils.isEmpty(email)) {
                    emailRegister.setError("Email es requerido.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordRegister.setError("Password es requerido.");
                    return;
                }

                if (TextUtils.isEmpty(repassword)) {
                    repeatPasswordRegister.setError("Repetir tu password es requerido.");
                    return;
                }

                if (password.length() < 6) {
                    passwordRegister.setError("La contraseña debe ser mayor a 6 dígitos.");
                    return;

                }

                if (password.equals(repassword)) {
                    progressBar.setVisibility(View.VISIBLE);

                    //llamada al Register user en firebase
                    //classe de firebase para el registro de usuarios.
                    fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Usuario Registrado Correctamente!.", Toast.LENGTH_SHORT).show();
                                if (fAuth.getCurrentUser() != null) {
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                } else {
                                    return;
                                }
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(RegisterActivity.this, "Algo Ocurrío, Intentalo de nuevo!.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }));
                } else {
                    repeatPasswordRegister.setError("Los password deben ser iguales.");
                    passwordRegister.setError("Los password deben ser iguales.");
                    return;

                }


            }
        });

    }
}
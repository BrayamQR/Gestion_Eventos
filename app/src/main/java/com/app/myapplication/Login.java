package com.app.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText txtEmailValid, txtPasswordValid;
    Button btnLogin;
    TextView UserNew;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    String correo = "", password = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Iniciar Sesión");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        txtEmailValid = findViewById(R.id.txtEmailValid);
        txtPasswordValid = findViewById(R.id.txtPasswordValid);
        btnLogin = findViewById(R.id.btnLogin);
        UserNew = findViewById(R.id.UserNew);

        firebaseAuth = firebaseAuth.getInstance();

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarDatos();
            }
        });
        UserNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }

    private void ValidarDatos() {
        correo = txtEmailValid.getText().toString();
        password = txtPasswordValid.getText().toString();
        if(TextUtils.isEmpty(correo) || TextUtils.isEmpty(password)){
            Toast.makeText(this,"Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(this,"Ingrese un correo valido", Toast.LENGTH_SHORT).show();
        }
        else{
            LoginUser();
        }
    }

    private void LoginUser() {
        progressDialog.setMessage("Iniciando Sesión ...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(correo,password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    startActivity(new Intent(Login.this, Home.class));
                    Toast.makeText(Login.this,"Bienvenido(a): "+firebaseUser.getEmail(),Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "Verifique crendeciales",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
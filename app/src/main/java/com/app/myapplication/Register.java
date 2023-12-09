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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    EditText txtNombre, txtCorreo, txtPassword, txtConfigPassword;
    Button btnRegisterUser;
    TextView Tengounacuenta;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    String nombre = "", correo = "", password = "", configpassword = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Registrar");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        txtNombre = findViewById(R.id.txtNombre);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfigPassword = findViewById(R.id.txtConfigPassword);

        btnRegisterUser = findViewById(R.id.btnRegisterUser);
        Tengounacuenta = findViewById(R.id.Tengounacuenta);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(Register.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        btnRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidadDatos();
            }
        });
        Tengounacuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });
    }

    private  void ValidadDatos(){
        nombre = txtNombre.getText().toString();
        correo = txtCorreo.getText().toString();
        password = txtPassword.getText().toString();
        configpassword = txtConfigPassword.getText().toString();

        if(TextUtils.isEmpty(nombre) || TextUtils.isEmpty(correo) || TextUtils.isEmpty(password) || TextUtils.isEmpty(configpassword)){
            Toast.makeText(this,"Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(this,"Ingrese un correo valido", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(configpassword)){
            Toast.makeText(this,"Las contraseñas no coinsiden", Toast.LENGTH_SHORT).show();
        }
        else {
            CrearUsuario();
        }
    }

    private void CrearUsuario() {
        progressDialog.setMessage("Creando su cuenta ...");
        progressDialog.show();

        //Crear un usuario en firebase

        firebaseAuth.createUserWithEmailAndPassword(correo,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                GuardarInformacion();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Register.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void GuardarInformacion() {
        progressDialog.setMessage("Guardando su información");
        progressDialog.dismiss();

        String uid = firebaseAuth.getUid();
        HashMap<String , String> Datos = new HashMap<>();
        Datos.put("uid", uid);
        Datos.put("correo",correo);
        Datos.put("nombre",nombre);
        Datos.put("password",password);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
        databaseReference.child(uid).setValue(Datos).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(Register.this, "Usuario registrado con exito", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Register.this, Home.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Register.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
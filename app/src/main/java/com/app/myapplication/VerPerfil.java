package com.app.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class VerPerfil extends AppCompatActivity {

    TextView viewNombreUser,viewCorreoUser, btn_EditEmail, btn_EditName, RestorePassword;

    String uid_User,nombre_User,correo_User,pass_User;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        uid_User = bundle.getString("uid_User");
        nombre_User = bundle.getString("nombre_User");
        correo_User = bundle.getString("correo_User");
        pass_User = bundle.getString("pass_User");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Info usuario");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        viewNombreUser = findViewById(R.id.viewNombreUser);
        viewCorreoUser = findViewById(R.id.viewCorreoUser);
        btn_EditEmail = findViewById(R.id.btn_EditEmail);
        btn_EditName = findViewById(R.id.btn_EditName);
        RestorePassword = findViewById(R.id.RestorePassword);

        viewNombreUser.setText(bundle.getString("nombre_User"));
        viewCorreoUser.setText(bundle.getString("correo_User"));



        btn_EditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerPerfil.this, EdirNameForm.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid_User",uid_User);
                bundle.putString("nombre",nombre_User);
                bundle.putString("correo",correo_User);
                bundle.putString("password",pass_User);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btn_EditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerPerfil.this, EditEmailForm.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid_User",uid_User);
                bundle.putString("nombre",nombre_User);
                bundle.putString("correo",correo_User);
                bundle.putString("password",pass_User);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        RestorePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerPerfil.this, EditPassForm.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid_User",uid_User);
                bundle.putString("nombre",nombre_User);
                bundle.putString("correo",correo_User);
                bundle.putString("password",pass_User);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
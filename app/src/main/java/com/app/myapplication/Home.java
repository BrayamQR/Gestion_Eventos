package com.app.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {
    Button btn_CerrarSesion, btn_AgregarEvento, btn_ListarEvento, btn_ViewPerfil;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    TextView NombresHome, CorreoHome;
    LinearLayoutCompat cont_eventhome, cont_userhome;
    ProgressBar ProgresBarDatos;
    DatabaseReference Usuarios;
    String uid_User, nombre_User, correo_User, pass_User;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Gestion de eventos");

        btn_CerrarSesion = findViewById(R.id.btn_CerrarSesion);
        btn_AgregarEvento = findViewById(R.id.btn_AgregarEvento);
        btn_ListarEvento = findViewById(R.id.btn_ListarEvento);
        btn_ViewPerfil = findViewById(R.id.btn_ViewPerfil);
        firebaseAuth = firebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        NombresHome = findViewById(R.id.NombresHome);
        CorreoHome = findViewById(R.id.CorreoHome);
        ProgresBarDatos = findViewById(R.id.ProgresBarDatos);
        cont_eventhome = findViewById(R.id.cont_eventhome);
        cont_userhome = findViewById(R.id.cont_userhome);

        Usuarios = FirebaseDatabase.getInstance().getReference("usuarios");

        btn_CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SalirAplicacion();
            }
        });
        btn_AgregarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, EventoForm.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid_User",uid_User);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btn_ListarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, ListaEventos.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid_User",uid_User);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btn_ViewPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, VerPerfil.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid_User",uid_User);
                bundle.putString("nombre_User",nombre_User);
                bundle.putString("correo_User",correo_User);
                bundle.putString("pass_User",pass_User);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        ValidarInicio();
        super.onStart();
    }

    private void SalirAplicacion() {
        firebaseAuth.signOut();
        startActivity(new Intent(Home.this, MainActivity.class));
    }
    private void CargarDatos(){
        Usuarios.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ProgresBarDatos.setVisibility(View.GONE);
                    NombresHome.setVisibility(View.VISIBLE);
                    CorreoHome.setVisibility(View.VISIBLE);
                    cont_eventhome.setVisibility(View.VISIBLE);
                    cont_userhome.setVisibility(View.VISIBLE);
                    String nombres = ""+snapshot.child("nombre").getValue();
                    String correo = ""+snapshot.child("correo").getValue();
                    uid_User = ""+snapshot.child("uid").getValue();
                    nombre_User = ""+snapshot.child("nombre").getValue();
                    correo_User = ""+snapshot.child("correo").getValue();
                    pass_User = ""+snapshot.child("password").getValue();
                    NombresHome.setText(nombres);
                    CorreoHome.setText(correo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void ValidarInicio(){
        if(firebaseUser != null){
            CargarDatos();
        }
        else{
            startActivity(new Intent(Home.this,MainActivity.class));
            finish();
        }
    }
}
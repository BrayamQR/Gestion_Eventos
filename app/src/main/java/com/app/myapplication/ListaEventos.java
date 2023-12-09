package com.app.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.app.myapplication.model.Evento;
import com.app.myapplication.model.EventoAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaEventos extends AppCompatActivity implements EventoAdapter.OnClickItemListener{

    private List<Evento> listEvento = new ArrayList<>();
    private EventoAdapter eventoAdapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView lstEvento;
    String uid_User;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_eventos);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Eventos");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        uid_User = bundle.getString("uid_User");
        lstEvento = findViewById(R.id.lstEvento);
        InizializarFirebase();
        ListarDatos();

    }

    private void InizializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void ListarDatos() {
        databaseReference.child("eventos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listEvento.clear();
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    Evento evento = objSnapshot.getValue(Evento.class);
                    listEvento.add(evento);
                }
                eventoAdapter = new EventoAdapter(ListaEventos.this,listEvento, ListaEventos.this);
                lstEvento.setLayoutManager(new LinearLayoutManager(ListaEventos.this));
                lstEvento.setAdapter(eventoAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.btn_iconadd).setVisible(false);
        menu.findItem(R.id.btn_iconsave).setVisible(false);
        menu.findItem(R.id.btn_icondelete).setVisible(false);
        menu.findItem(R.id.btn_iconedit).setVisible(false);
        menu.findItem(R.id.btn_iconaddlist).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.btn_iconaddlist){
            Intent intent = new Intent(ListaEventos.this, EventoForm.class);
            Bundle bundle = new Bundle();
            bundle.putString("uid_User",uid_User);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onItemClick(Evento evento) {

        Intent intent = new Intent(ListaEventos.this, VerEvento.class);
        Bundle bundle = new Bundle();
        bundle.putString("uid_User",uid_User);
        bundle.putString("view_uidevento",evento.getUid());
        bundle.putString("view_nombreevento", evento.getNombre());
        bundle.putString("view_organizadorevento", evento.getOrganizador());
        bundle.putString("view_descripcionevento",evento.getDescripcion());
        bundle.putString("view_fechaevento", evento.getFecha());
        bundle.putString("view_horaevento", evento.getHora());
        bundle.putString("view_iduser",evento.getIduser());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
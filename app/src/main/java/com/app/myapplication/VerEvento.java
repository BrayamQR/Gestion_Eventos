package com.app.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myapplication.model.Evento;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VerEvento extends AppCompatActivity {
    TextView viewNombreEvento,viewOrganizadorEvento,viewFechaEvento,viewHoraEvento,viewDescripcionEvento;
    String uid_User, view_uidevento,view_nombreevento, view_organizadorevento, view_fechaevento,view_horaevento, view_iduser, view_descripcionevento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_evento);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        uid_User = bundle.getString("uid_User");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Info Evento");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        view_uidevento = bundle.getString("view_uidevento");
        view_nombreevento = bundle.getString("view_nombreevento");
        view_organizadorevento = bundle.getString("view_organizadorevento");
        view_fechaevento = bundle.getString("view_fechaevento");
        view_horaevento = bundle.getString("view_horaevento");
        view_iduser = bundle.getString("view_iduser");
        view_descripcionevento = bundle.getString("view_descripcionevento");

        viewNombreEvento = findViewById(R.id.viewNombreEvento);
        viewOrganizadorEvento = findViewById(R.id.viewOrganizadorEvento);
        viewFechaEvento = findViewById(R.id.viewFechaEvento);
        viewHoraEvento = findViewById(R.id.viewHoraEvento);
        viewDescripcionEvento = findViewById(R.id.viewDescripcionEvento);

        viewNombreEvento.setText(view_nombreevento);
        viewOrganizadorEvento.setText(view_organizadorevento);
        viewFechaEvento.setText(view_fechaevento);
        viewHoraEvento.setText(view_horaevento);
        viewDescripcionEvento.setText(view_descripcionevento);

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
        if(uid_User.equals(view_iduser)){
            menu.findItem(R.id.btn_icondelete).setVisible(true);
            menu.findItem(R.id.btn_iconedit).setVisible(true);
        }
        else{
            menu.findItem(R.id.btn_icondelete).setVisible(false);
            menu.findItem(R.id.btn_iconedit).setVisible(false);
        }
        menu.findItem(R.id.btn_iconadd).setVisible(false);
        menu.findItem(R.id.btn_iconsave).setVisible(false);

        menu.findItem(R.id.btn_iconaddlist).setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.btn_iconedit){
            Intent intent = new Intent(VerEvento.this, EventoForm.class);
            Bundle bundle = new Bundle();
            bundle.putString("evento",view_nombreevento);
            bundle.putString("organizador",view_organizadorevento);
            bundle.putString("fecha",view_fechaevento);
            bundle.putString("hora",view_horaevento);
            bundle.putString("descripcion",view_descripcionevento);
            bundle.putString("uid",view_uidevento);
            bundle.putString("iduser",view_iduser);
            bundle.putString("uid_User",uid_User);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.btn_icondelete){
            Evento ev = new Evento();
            ev.setUid(view_uidevento);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Seguro que desea eliminar el registro?");
            builder.setCancelable(true);
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("eventos");
                    databaseReference.child(ev.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(VerEvento.this, "Se elimino el registro exitosamente", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(VerEvento.this, ListaEventos.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("uid_User",uid_User);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        }
                    });
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
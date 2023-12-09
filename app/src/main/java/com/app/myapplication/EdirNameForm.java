package com.app.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.app.myapplication.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.guieffect.qual.UI;


public class EdirNameForm extends AppCompatActivity {
    EditText txtEditNombre;
    String uid_User, nombre, correo, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edir_name_form);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        uid_User = bundle.getString("uid_User");
        correo = bundle.getString("correo");
        password = bundle.getString("password");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Modificar nombre");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        txtEditNombre = findViewById(R.id.txtEditNombre);

        txtEditNombre.setText(bundle.getString("nombre"));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.btn_iconadd).setVisible(false);
        menu.findItem(R.id.btn_iconsave).setVisible(true);
        menu.findItem(R.id.btn_icondelete).setVisible(false);
        menu.findItem(R.id.btn_iconaddlist).setVisible(false);
        menu.findItem(R.id.btn_iconedit).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        nombre = txtEditNombre.getText().toString();
        if(item.getItemId() == R.id.btn_iconsave){
            if(nombre.equals("")){
                ValidarCampos();
            }
            else{
                Usuario us = new Usuario();
                us.setUid(uid_User);
                us.setNombre(nombre);
                us.setCorreo(correo);
                us.setPassword(password);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
                databaseReference.child(us.getUid()).setValue(us).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(EdirNameForm.this, "Usuario modificado exitosamente", Toast.LENGTH_SHORT).show();
                            LimpiarCampos();
                            Intent intent = new Intent(EdirNameForm.this, VerPerfil.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("uid_User",uid_User);
                            bundle.putString("nombre_User",nombre);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(EdirNameForm.this, "Error al modificar el usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void LimpiarCampos() {
        txtEditNombre.setText("");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    private void ValidarCampos() {
        nombre = txtEditNombre.getText().toString();
        if(nombre.equals("")){
            txtEditNombre.setError("Required");
        }
    }

}
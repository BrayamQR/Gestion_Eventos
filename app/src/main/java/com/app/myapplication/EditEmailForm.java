package com.app.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.app.myapplication.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditEmailForm extends AppCompatActivity {

    EditText txtEditCorreo;
    FirebaseAuth firebaseAuth;
    String uid_User, nombre, correo, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email_form);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        uid_User = bundle.getString("uid_User");
        nombre = bundle.getString("nombre");
        password = bundle.getString("password");
        firebaseAuth = firebaseAuth.getInstance();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Modificar email");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        txtEditCorreo = findViewById(R.id.txtEditCorreo);
        txtEditCorreo.setText(bundle.getString("correo"));
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
        correo = txtEditCorreo.getText().toString();

        if(item.getItemId() == R.id.btn_iconsave) {
            if (correo.equals("")) {
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
                            Toast.makeText(EditEmailForm.this, "Usuario modificado exitosamente", Toast.LENGTH_SHORT).show();
                            LimpiarCampos();
                            SalirAplicacion();
                        }
                        else{
                            Toast.makeText(EditEmailForm.this, "Error al modificar el usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void LimpiarCampos() {
        txtEditCorreo.setText("");
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    private void ValidarCampos() {
        correo = txtEditCorreo.getText().toString();
        if(correo.equals("")){
            txtEditCorreo.setError("Required");
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            txtEditCorreo.setError("Ingrese un correo valido");
        }
    }
    private void SalirAplicacion() {
        firebaseAuth.signOut();
        startActivity(new Intent(EditEmailForm.this, MainActivity.class));
    }
}
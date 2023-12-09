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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditPassForm extends AppCompatActivity {
    EditText txtEditPasswordActual,txtEditNewPassword,txtEditConfirNewPassword;
    FirebaseAuth firebaseAuth;
    String uid_User, nombre, correo, password, passactual, newpass, confnewpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pass_form);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        uid_User = bundle.getString("uid_User");
        nombre = bundle.getString("nombre");
        password = bundle.getString("password");
        correo = bundle.getString("correo");
        firebaseAuth = firebaseAuth.getInstance();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Cambiar contraseña");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        txtEditPasswordActual = findViewById(R.id.txtEditPasswordActual);
        txtEditNewPassword = findViewById(R.id.txtEditNewPassword);
        txtEditConfirNewPassword = findViewById(R.id.txtEditConfirPassword);
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
        passactual = txtEditPasswordActual.getText().toString();
        newpass = txtEditNewPassword.getText().toString();
        confnewpass = txtEditConfirNewPassword.getText().toString();
        if(item.getItemId() == R.id.btn_iconsave) {
            if(passactual.equals("")|| newpass.equals("") || confnewpass.equals("")){
                ValidarCampos();
            }
            else{
                if(!passactual.equals(password)){
                    Toast.makeText(this,"La contraseña  actual es incorrecta", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!newpass.equals(confnewpass)){
                        Toast.makeText(this,"Las contraseñas no coinsiden", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Usuario us = new Usuario();
                        us.setUid(uid_User);
                        us.setCorreo(correo);
                        us.setNombre(nombre);
                        us.setPassword(newpass);
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
                        databaseReference.child(us.getUid()).setValue(us).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(EditPassForm.this, "Usuario modificado exitosamente", Toast.LENGTH_SHORT).show();
                                    LimpiarCampos();
                                    SalirAplicacion();
                                }
                                else{
                                    Toast.makeText(EditPassForm.this, "Error al modificar el usuario", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    private void LimpiarCampos() {
        txtEditNewPassword.setText("");
        txtEditPasswordActual.setText("");
        txtEditConfirNewPassword.setText("");
    }
    private void ValidarCampos() {
        passactual = txtEditPasswordActual.getText().toString();
        newpass = txtEditNewPassword.getText().toString();
        confnewpass = txtEditConfirNewPassword.getText().toString();
        if(passactual.equals("")){
            txtEditPasswordActual.setError("Required");
        }
        else if(newpass.equals("")){
            txtEditNewPassword.setError("Required");
        }
        else if(confnewpass.equals("")){
            txtEditConfirNewPassword.setError("Required");
        }
    }
    private void SalirAplicacion() {
        firebaseAuth.signOut();
        startActivity(new Intent(EditPassForm.this, MainActivity.class));
    }
}
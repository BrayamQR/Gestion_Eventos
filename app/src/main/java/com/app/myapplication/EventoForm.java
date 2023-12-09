package com.app.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.myapplication.model.Evento;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.UUID;

public class EventoForm extends AppCompatActivity {
    Button btnShowCalendar,btnShowTime;
    EditText txtFecha,txtHora,txtNombreEvento,txtOrganizador,txtDescripcion;
    int day, month, year, hour, minute;
    String uid_User, evento = "", organizador = "", descripcion = "", fecha = "", hora = "", uid= "", iduser = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_form);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        uid_User = bundle.getString("uid_User");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Formulario Evento");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        btnShowCalendar = findViewById(R.id.btnShowCalendar);
        btnShowTime = findViewById(R.id.btnShowTime);
        txtFecha = findViewById(R.id.txtFecha);
        txtHora = findViewById(R.id.txtHora);
        txtNombreEvento = findViewById(R.id.txtNombreEvento);
        txtOrganizador = findViewById(R.id.txtOrganizador);
        txtDescripcion = findViewById(R.id.txtDescripcion);

        if(bundle.getString("uid") != null && !bundle.getString("uid").isEmpty()){
            uid = bundle.getString("uid");
            iduser = bundle.getString("iduser");
            RellenarCampos();
        }

        btnShowCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();

                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EventoForm.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int yearSelected, int monthSelected, int daySelected) {
                        String FormatDay, FormatMonth;

                        if(daySelected < 10){
                            FormatDay = "0"+String.valueOf(daySelected);
                        }else{
                            FormatDay = String.valueOf(daySelected);
                        }

                        int Mes = monthSelected + 1;
                        if(Mes < 10){
                            FormatMonth = "0"+String.valueOf(Mes);
                        }
                        else{
                            FormatMonth = String.valueOf(Mes);
                        }
                        txtFecha.setText(FormatDay+"/"+FormatMonth+"/"+yearSelected);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        btnShowTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();

                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(EventoForm.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourSelected, int minuteSelected) {
                        String FormatHour, FormatMinute;
                        if(hourSelected<10){
                            FormatHour = "0"+String.valueOf(hourSelected);
                        }else{
                            FormatHour = String.valueOf(hourSelected);
                        }
                        if(minuteSelected< 10){
                            FormatMinute = "0"+String.valueOf(minuteSelected);
                        }
                        else{
                            FormatMinute = String.valueOf(minuteSelected);
                        }
                        txtHora.setText(FormatHour+":"+FormatMinute);
                    }
                },hour,minute,false);
                timePickerDialog.show();
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
        if(!uid.equals("")){
            menu.findItem(R.id.btn_iconadd).setVisible(false);
            menu.findItem(R.id.btn_iconsave).setVisible(true);
        }
        else{
            menu.findItem(R.id.btn_iconadd).setVisible(true);
            menu.findItem(R.id.btn_iconsave).setVisible(false);
        }
        menu.findItem(R.id.btn_icondelete).setVisible(false);
        menu.findItem(R.id.btn_iconaddlist).setVisible(false);
        menu.findItem(R.id.btn_iconedit).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        evento = txtNombreEvento.getText().toString();
        organizador = txtOrganizador.getText().toString();
        descripcion = txtDescripcion.getText().toString();
        fecha = txtFecha.getText().toString();
        hora = txtHora.getText().toString();



        if(item.getItemId() == R.id.btn_iconadd){
            if(evento.equals("") || organizador.equals("") || descripcion.equals("") || fecha.equals("") || hora.equals("")){
                ValidarCampos();
            }
            else{
                Evento ev = new Evento();
                ev.setUid(UUID.randomUUID().toString());
                ev.setNombre(evento);
                ev.setOrganizador(organizador);
                ev.setDescripcion(descripcion);
                ev.setFecha(fecha);
                ev.setHora(hora);
                ev.setIduser(uid_User);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("eventos");
                databaseReference.child(ev.getUid()).setValue(ev).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(EventoForm.this, "Evento guardado exitosamente", Toast.LENGTH_SHORT).show();
                            LimpiarCampos();
                            Intent intent = new Intent(EventoForm.this, ListaEventos.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("uid_User",uid_User);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(EventoForm.this, "Error al guardar el evento", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }
        else if(item.getItemId() == R.id.btn_iconsave){
            if(!uid.equals("")){
                if(evento.equals("") || organizador.equals("") || descripcion.equals("") || fecha.equals("") || hora.equals("")){
                    ValidarCampos();
                }
                else{
                    Evento ev = new Evento();
                    ev.setUid(uid);
                    ev.setNombre(evento.trim());
                    ev.setOrganizador(organizador.trim());
                    ev.setDescripcion(descripcion.trim());
                    ev.setFecha(fecha.trim());
                    ev.setHora(hora.trim());
                    ev.setIduser(iduser);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("eventos");
                    databaseReference.child(ev.getUid()).setValue(ev).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(EventoForm.this, "Evento modificado exitosamente", Toast.LENGTH_SHORT).show();
                                LimpiarCampos();
                                Intent intent = new Intent(EventoForm.this, ListaEventos.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("uid_User",uid_User);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(EventoForm.this, "Error al modificar el evento", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void LimpiarCampos() {
        txtNombreEvento.setText("");
        txtOrganizador.setText("");
        txtDescripcion.setText("");
        txtFecha.setText("");
        txtHora.setText("");
    }
    private void RellenarCampos(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        txtNombreEvento.setText(bundle.getString("evento"));
        txtOrganizador.setText(bundle.getString("organizador"));
        txtFecha.setText(bundle.getString("fecha"));
        txtHora.setText(bundle.getString("hora"));
        txtDescripcion.setText(bundle.getString("descripcion"));
    }
    private void ValidarCampos() {
        evento = txtNombreEvento.getText().toString();
        organizador = txtOrganizador.getText().toString();
        descripcion = txtDescripcion.getText().toString();
        fecha = txtFecha.getText().toString();
        hora = txtHora.getText().toString();
        if(evento.equals("")){
            txtNombreEvento.setError("Required");
        }
        else if(organizador.equals("")){
            txtOrganizador.setError("Required");
        }
        else if(descripcion.equals("")){
            txtDescripcion.setError("Required");
        }
        else if(fecha.equals("")){
            txtFecha.setError("Required");
        }
        else if(hora.equals("")){
            txtHora.setError("Required");
        }
    }
}
package com.example.mymedsdsm;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    EditText title_input, doctor_input, cant_input;
    Spinner type_input;

    TimePicker hour_input;
    Button update_button, delete_button;

    String id, title, doctor, cant, type;
    int hour, min;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        title_input = findViewById(R.id.title_input2);
        doctor_input = findViewById(R.id.doctor_input2);
        cant_input = findViewById(R.id.cant_input2);
        type_input = findViewById(R.id.type_input2);
        hour_input = findViewById(R.id.timePicker_input2);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);

        //First we call this
        getAndSetIntentData();

        //Set actionbar title after getAndSetIntentData method
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
        }

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //And only then we call this
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                title = title_input.getText().toString().trim();
                doctor = doctor_input.getText().toString().trim();
                cant = cant_input.getText().toString().trim();
                type = type_input.getSelectedItem().toString().trim();
                hour = hour_input.getHour();
                min = hour_input.getMinute();
                myDB.updateData(id, title, doctor, cant, type, hour, min);

                Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });

    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("title") &&
                getIntent().hasExtra("doctor") && getIntent().hasExtra("cant") &&
                getIntent().hasExtra("type")&&
                getIntent().hasExtra("hour")&&
                getIntent().hasExtra("min")){
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            doctor = getIntent().getStringExtra("doctor");
            cant = getIntent().getStringExtra("cant");
            type = getIntent().getStringExtra("type");
            hour = Integer.parseInt(getIntent().getStringExtra("hour"));
            min = Integer.parseInt(getIntent().getStringExtra("min"));
            Log.d("myTag", type);
            //Setting Intent Data
            title_input.setText(title);
            doctor_input.setText(doctor);
            cant_input.setText(cant);
            hour_input.setHour(hour);
            hour_input.setMinute(min);
            if(type.equals("Tabletas o cápsulas")){
                type_input.setSelection(1);
            } else if(type.equals("Bebible")){
                type_input.setSelection(2);
            } else if(type.equals("Inyectable")){
                type_input.setSelection(3);
            }

        }else{
            Toast.makeText(this, "Sin datos.", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Borrar " + title);
        builder.setMessage("¿Está seguro de borrar " + title + "?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.deleteOneRow(id);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}
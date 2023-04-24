package com.example.mymedsdsm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

public class AddActivity extends AppCompatActivity {

    EditText title_input, doctor_input, cant_input;
    Spinner type_input;
    TimePicker time_input;
    Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title_input = findViewById(R.id.title_input);
        doctor_input = findViewById(R.id.doctor_input);
        cant_input = findViewById(R.id.cant_input);
        type_input = findViewById(R.id.type_input);
        time_input = findViewById(R.id.timePicker_input);
        add_button = findViewById(R.id.add_button);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                myDB.addMed(title_input.getText().toString().trim(),
                        doctor_input.getText().toString().trim(),
                        Integer.valueOf(cant_input.getText().toString().trim()),
                        type_input.getSelectedItem().toString().trim(),
                        time_input.getHour(), time_input.getMinute()

                );Log.d("HOLA", " "+time_input.getHour());Log.d("HOLA", " "+time_input.getMinute());

                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
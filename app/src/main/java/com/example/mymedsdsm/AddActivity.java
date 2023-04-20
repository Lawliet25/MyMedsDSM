package com.example.mymedsdsm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    EditText title_input, doctor_input, cant_input;
    DatePicker date_input;
    Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title_input = findViewById(R.id.title_input);
        doctor_input = findViewById(R.id.doctor_input);
        cant_input = findViewById(R.id.cant_input);
        date_input = findViewById(R.id.datePicker);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day = date_input.getDayOfMonth();
                int month = date_input.getMonth() + 1;
                int year = date_input.getYear();
                String date = day + "/" + month + "/" + year;

                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                myDB.addMed(title_input.getText().toString().trim(),
                        doctor_input.getText().toString().trim(),
                        Integer.valueOf(cant_input.getText().toString().trim()),
                        date);

                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
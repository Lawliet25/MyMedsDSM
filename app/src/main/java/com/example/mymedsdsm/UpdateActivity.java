package com.example.mymedsdsm;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

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
                showNotification(view, hour_input.getHour(), hour_input.getMinute(), title_input.getText().toString().trim());
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
    public static long getAlarmDelay(int hour, int minute) {
        // Obtener la hora actual
        Calendar now = Calendar.getInstance();
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int currentMinute = now.get(Calendar.MINUTE);

        Log.d("Delay", "Current hour " + currentHour);
        Log.d("Delay", "Current minute " + currentMinute);

        // Calcular la diferencia de tiempo en milisegundos
        long delayMillis = ((hour - currentHour) * 60 + (minute - currentMinute)) * 60 * 1000;

        // Si la hora que recibimos es anterior a la hora actual, sumar un día en milisegundos
        if (delayMillis < 0) {
            delayMillis += 24 * 60 * 60 * 1000;
        }

        Log.d("Delay", "Delay " + delayMillis);

        return delayMillis;
    }

    public void showNotification(View view, int hour, int minute, String name) {
        long delay = getAlarmDelay(hour, minute);

        Log.d("Delay", "Delay" + delay);
        view.postDelayed(new Runnable() {
            public void run() {
                UpdateActivity.NotificationHelper notificationHelper = new UpdateActivity.NotificationHelper(UpdateActivity.this);
                notificationHelper.createNotification("My Meds", "Es hora de tomar tu medicamento: " + name);
            }
        }, delay);
    }

    class NotificationHelper {
        private Context mContext;
        private static final int NOTIFICATION_ID = 0;
        private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
        private NotificationManager mNotificationManager;

        public NotificationHelper(Context context) {
            mContext = context;
            mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        public void createNotification(String title, String message) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID, "My Meds", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(true);
                notificationChannel.setDescription("Notificación de My Meds");
                mNotificationManager.createNotificationChannel(notificationChannel);
            }

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, PRIMARY_CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher);

            mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
    }
}
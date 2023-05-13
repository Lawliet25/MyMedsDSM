package com.example.mymedsdsm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
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

import java.util.Calendar;

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
                );

                Log.d("HOLA", " " + time_input.getHour());
                Log.d("HOLA", " " + time_input.getMinute());

                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);


                showNotification(view, time_input.getHour(), time_input.getMinute(), title_input.getText().toString().trim());
            }
        });
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
                NotificationHelper notificationHelper = new NotificationHelper(AddActivity.this);
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
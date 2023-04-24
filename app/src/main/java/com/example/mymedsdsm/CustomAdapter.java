package com.example.mymedsdsm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList med_id, med_title, med_doctor, med_cant, med_type, med_hour, med_min;

    CustomAdapter(Activity activity, Context context, ArrayList med_id, ArrayList med_title, ArrayList med_doctor,
                  ArrayList med_cant, ArrayList med_type, ArrayList med_hour, ArrayList med_min){
        this.activity = activity;
        this.context = context;
        this.med_id = med_id;
        this.med_title = med_title;
        this.med_doctor = med_doctor;
        this.med_cant = med_cant;
        this.med_type = med_type;
        this.med_hour = med_hour;
        this.med_min = med_min;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.med_id_txt.setText(String.valueOf("ID: " + med_id.get(position)));
        holder.med_title_txt.setText(String.valueOf(med_title.get(position)));
        holder.med_doctor_txt.setText(String.valueOf("Doctor: " + med_doctor.get(position)));
        holder.med_cant_txt.setText(String.valueOf(med_cant.get(position)));
        holder.med_type_txt.setText(String.valueOf("Tipo: " + med_type.get(position)));
        holder.med_hour_txt.setText(String.valueOf("Horario: " + med_hour.get(position)+ ": " + med_min.get(position)));
        //Recyclerview onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(med_id.get(position)));
                intent.putExtra("title", String.valueOf(med_title.get(position)));
                intent.putExtra("doctor", String.valueOf(med_doctor.get(position)));
                intent.putExtra("cant", String.valueOf(med_cant.get(position)));
                intent.putExtra("type", String.valueOf(med_type.get(position)));
                intent.putExtra("hour", String.valueOf(med_hour.get(position)));
                intent.putExtra("min", String.valueOf(med_min.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });


    }

    @Override
    public int getItemCount() {
        return med_id.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView med_id_txt, med_title_txt, med_doctor_txt, med_cant_txt, med_type_txt, med_hour_txt;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            med_id_txt = itemView.findViewById(R.id.med_id_txt);
            med_title_txt = itemView.findViewById(R.id.med_title_txt);
            med_doctor_txt = itemView.findViewById(R.id.med_doctor_txt);
            med_cant_txt = itemView.findViewById(R.id.med_cant_txt);
            med_type_txt = itemView.findViewById(R.id.med_type_txt);
            med_hour_txt = itemView.findViewById(R.id.med_hour_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            //Animate Recyclerview
            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }

    }

}

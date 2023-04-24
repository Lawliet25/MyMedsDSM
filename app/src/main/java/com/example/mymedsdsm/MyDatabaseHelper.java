package com.example.mymedsdsm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import androidx.annotation.Nullable;

class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Mymeds.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_meds";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "med_title";
    private static final String COLUMN_DOCTOR = "med_doctor";
    private static final String COLUMN_CANT = "med_cant";
    private static final String COLUMN_TYPE = "med_type";
    private static final String COLUMN_HOUR = "med_hour";
    private static final String COLUMN_MIN = "med_min";

    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DOCTOR + " TEXT, " +
                COLUMN_CANT + " INTEGER, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_HOUR + " INTEGER, " +
                COLUMN_MIN + " INTEGER);";
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addMed(String title, String doctor, int cant, String type, int hour, int min){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_DOCTOR, doctor);
        cv.put(COLUMN_CANT, cant);
        cv.put(COLUMN_TYPE, type);
        cv.put(COLUMN_HOUR, hour);
        cv.put(COLUMN_MIN, min);
        long result = db.insert(TABLE_NAME,null, cv);
        if(result == -1){
            Toast.makeText(context, "Error al agregar", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "¡Medicamento agregado exitosamente!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateData(String row_id, String title, String doctor, String cant, String type, int hour, int min){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_DOCTOR, doctor);
        cv.put(COLUMN_CANT, cant);
        cv.put(COLUMN_TYPE, type);
        cv.put(COLUMN_HOUR, hour);
        cv.put(COLUMN_MIN, min);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "¡Medicamento actualizado exitosamente!", Toast.LENGTH_SHORT).show();
        }

    }

    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Medicamento eliminado exitosamente", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

}

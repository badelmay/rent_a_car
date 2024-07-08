package com.example.babel_car;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "babel_car.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE arac_kirala (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "aracAdi TEXT," +
                "musteriAdSoyad TEXT," +
                "musteriTc TEXT," +
                "musteriTel TEXT," +
                "alisTarih TEXT," +
                "verisTarih TEXT," +
                "toplamFiyat REAL" +
                ")";
        db.execSQL(createTableQuery);
    }

    public void addAracKiralama(String aracAdi, String musteriAdSoyad, String musteriTc, String musteriTel, String alisTarih, String verisTarih, double toplamFiyat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("aracAdi", aracAdi);
        values.put("musteriAdSoyad", musteriAdSoyad);
        values.put("musteriTc", musteriTc);
        values.put("musteriTel", musteriTel);
        values.put("alisTarih", alisTarih);
        values.put("verisTarih", verisTarih);
        values.put("toplamFiyat", toplamFiyat);

        db.insert("arac_kirala", null, values);
        db.close();
    }

    public boolean aracMüsaitlikKontrolu(String aracAdi, String alisTarih, String verisTarih) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"id"};
        String selection = "aracAdi = ? AND ((alisTarih <= ? AND verisTarih >= ?) OR (alisTarih <= ? AND verisTarih >= ?))";
        String[] selectionArgs = {aracAdi, alisTarih, alisTarih, verisTarih, verisTarih};
        Cursor cursor = db.query("arac_kirala", columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count == 0;
    }

    public List<String> getPreviousRentals(String musteriTc) {
        List<String> rentals = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"aracAdi", "alisTarih", "verisTarih"};
        String selection = "musteriTc = ?";
        String[] selectionArgs = {musteriTc};
        Cursor cursor = db.query("arac_kirala", columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String rental = "Araç: " + cursor.getString(cursor.getColumnIndex("aracAdi")) +
                        ", Alış Tarihi: " + cursor.getString(cursor.getColumnIndex("alisTarih")) +
                        ", Veriş Tarihi: " + cursor.getString(cursor.getColumnIndex("verisTarih"));
                rentals.add(rental);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return rentals;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

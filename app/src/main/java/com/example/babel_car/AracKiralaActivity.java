package com.example.babel_car;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AracKiralaActivity extends AppCompatActivity {

    private static final double DAILY_RATE = 500.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arac_kirala);

        String aracAdi = getIntent().getStringExtra("aracAdi");

        TextView textViewCarName = findViewById(R.id.textViewCarName);
        textViewCarName.setText(aracAdi);

        EditText alisTarihEditText = findViewById(R.id.alis_tarih);
        EditText verisTarihEditText = findViewById(R.id.veris_tarih);
        EditText musteriAdEditText = findViewById(R.id.müsteri_ad);
        EditText musteriTcEditText = findViewById(R.id.müsteri_tc);
        EditText musteriTelEditText = findViewById(R.id.müsteri_tel);
        TextView textViewToplam = findViewById(R.id.textViewToplam);

        // Müşteri ad kısmına sadece harf girilmesini sağlıyoruz
        InputFilter onlyLettersFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetter(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        musteriAdEditText.setFilters(new InputFilter[]{onlyLettersFilter});

        // TC ve Telefon numarası için 11 karakter sınırlaması
        InputFilter lengthFilter11 = new LengthFilter(11);
        musteriTcEditText.setFilters(new InputFilter[]{lengthFilter11});
        musteriTelEditText.setFilters(new InputFilter[]{lengthFilter11});

        DatePickerDialog.OnDateSetListener alisDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                alisTarihEditText.setText(selectedDate);
                updateTotalPrice(alisTarihEditText, verisTarihEditText, textViewToplam);
            }
        };

        DatePickerDialog.OnDateSetListener verisDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                verisTarihEditText.setText(selectedDate);
                updateTotalPrice(alisTarihEditText, verisTarihEditText, textViewToplam);
            }
        };

        alisTarihEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(alisTarihEditText, alisDateSetListener);
            }
        });

        verisTarihEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(verisTarihEditText, verisDateSetListener);
            }
        });

        ImageView kiralaButton = findViewById(R.id.kirala_buton);
        kiralaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelper = new DatabaseHelper(AracKiralaActivity.this);
                String alisTarih = alisTarihEditText.getText().toString();
                String verisTarih = verisTarihEditText.getText().toString();
                if (dbHelper.aracMüsaitlikKontrolu(aracAdi, alisTarih, verisTarih)) {
                    double toplamFiyat = calculateTotalPrice(alisTarih, verisTarih);
                    dbHelper.addAracKiralama(aracAdi, musteriAdEditText.getText().toString(), musteriTcEditText.getText().toString(), musteriTelEditText.getText().toString(), alisTarih, verisTarih, toplamFiyat);
                    showSuccessDialog(musteriTcEditText.getText().toString(), dbHelper);
                } else {
                    Toast.makeText(AracKiralaActivity.this, "Seçilen tarihlerde araç dolu!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView geriButon = findViewById(R.id.geri_buton);
        geriButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AracKiralaActivity.this, AracSayfasiActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showDatePickerDialog(final EditText editText, DatePickerDialog.OnDateSetListener dateSetListener) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(AracKiralaActivity.this, dateSetListener, year, month, day);
        dialog.show();
    }

    private void updateTotalPrice(EditText alisTarihEditText, EditText verisTarihEditText, TextView textViewToplam) {
        String alisTarih = alisTarihEditText.getText().toString();
        String verisTarih = verisTarihEditText.getText().toString();
        if (!alisTarih.isEmpty() && !verisTarih.isEmpty()) {
            double toplamFiyat = calculateTotalPrice(alisTarih, verisTarih);
            textViewToplam.setText(String.format(Locale.getDefault(), "%.2f TL", toplamFiyat));
        }
    }

    private double calculateTotalPrice(String alisTarih, String verisTarih) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date startDate = dateFormat.parse(alisTarih);
            Date endDate = dateFormat.parse(verisTarih);
            if (startDate != null && endDate != null) {
                long diffInMillies = endDate.getTime() - startDate.getTime();
                long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                return diffInDays * DAILY_RATE;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private void showSuccessDialog(String musteriTc, DatabaseHelper dbHelper) {
        List<String> previousRentals = dbHelper.getPreviousRentals(musteriTc);
        StringBuilder message = new StringBuilder("Kiralama başarılı!\n\nÖnceki Kiralamalar:\n");

        for (String rental : previousRentals) {
            message.append(rental).append("\n");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bilgi")
                .setMessage(message.toString())
                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
}

package com.example.babel_car;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AracSayfasiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arac_sayfasi);

        // Tüm butonlar
        Button hundaiKiralaButton = findViewById(R.id.hundai_kirala);
        Button clioKiralaButton = findViewById(R.id.clio_kirala);
        Button opelKiralaButton = findViewById(R.id.opel_kirala);
        Button fiatEgeaKiralaButton = findViewById(R.id.fiat_egea_kirala);
        Button daciaDusterKiralaButton = findViewById(R.id.dacia_duster_kirala);
        Button renaultCapturKiralaButton = findViewById(R.id.renault_captur_kirala);
        Button nissanQashqaiKiralaButton = findViewById(R.id.nissan_kirala);
        Button renauldAustralKiralaButton = findViewById(R.id.renauld_austral_kirala);
        Button fordTourneoKiralaButton = findViewById(R.id.ford_tourneo_kirala);
        Button renauldTraficKiralaButton = findViewById(R.id.renauld_trafic_kirala);

        // Tüm butonlar için tıklama olayı
        hundaiKiralaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AracKiralaActivity'yi başlat
                aracKiralaActivityBaslat("Hundai Bayon");
            }
        });

        clioKiralaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AracKiralaActivity'yi başlat
                aracKiralaActivityBaslat("Renault Clio");
            }
        });

        opelKiralaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AracKiralaActivity'yi başlat
                aracKiralaActivityBaslat("Opel Corsa");
            }
        });

        fiatEgeaKiralaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AracKiralaActivity'yi başlat
                aracKiralaActivityBaslat("Fiat Egea");
            }
        });

        daciaDusterKiralaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AracKiralaActivity'yi başlat
                aracKiralaActivityBaslat("Dacia Duster");
            }
        });

        renaultCapturKiralaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AracKiralaActivity'yi başlat
                aracKiralaActivityBaslat("Renault Captur");
            }
        });

        nissanQashqaiKiralaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AracKiralaActivity'yi başlat
                aracKiralaActivityBaslat("Nissan QASHQAI");
            }
        });

        renauldAustralKiralaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AracKiralaActivity'yi başlat
                aracKiralaActivityBaslat("Renauld Austral");
            }
        });

        fordTourneoKiralaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AracKiralaActivity'yi başlat
                aracKiralaActivityBaslat("Ford Tourneo");
            }
        });

        renauldTraficKiralaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AracKiralaActivity'yi başlat
                aracKiralaActivityBaslat("Renauld Trafic");
            }
        });
    }

    // AracKiralaActivity'yi araç adı ile başlatan metod
    private void aracKiralaActivityBaslat(String aracAdi) {
        Intent intent = new Intent(AracSayfasiActivity.this, AracKiralaActivity.class);
        intent.putExtra("aracAdi", aracAdi); // AracKiralaActivity'e araç adını aktar
        startActivity(intent);
    }
}

package com.view.idnplay;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    long mBackPressed;
    CardView grid1,grid2,grid3,grid4,grid5,grid6,grid7,grid8,grid9,grid10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grid1 = findViewById(R.id.grid1);
        grid2 = findViewById(R.id.grid2);
        grid3 = findViewById(R.id.grid3);
        grid4 = findViewById(R.id.grid4);
        grid5 = findViewById(R.id.grid5);
        grid6 = findViewById(R.id.grid6);
        grid7 = findViewById(R.id.grid7);
        grid8 = findViewById(R.id.grid8);
        grid9 = findViewById(R.id.grid9);
        grid10 = findViewById(R.id.grid10);

        grid1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Your Message", Toast.LENGTH_LONG).show();
                Intent i = new Intent(MainActivity.this, GameActivity.class);
//                i.putExtra("url", "https://play.famobi.com/okey-classic");
                i.putExtra("url", "http://asian2bet.com/?ref=asian2bet");
                startActivity(i);
            }
        });

        grid2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Your Message", Toast.LENGTH_LONG).show();
                Intent i = new Intent(MainActivity.this, GameActivity.class);
//                i.putExtra("url", "https://play.famobi.com/blackjack-bet");
                i.putExtra("url", "http://asian2bet.com/?ref=asian2bet");
                startActivity(i);
            }

        });
        grid3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GameActivity.class);
//                i.putExtra("url", "https://play.famobi.com/mafia-poker");
                i.putExtra("url", "http://asian2bet.com/?ref=asian2bet");
                startActivity(i);
            }
        });
        grid4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GameActivity.class);
//                i.putExtra("url", "https://play.famobi.com/freecell-solitaire-classic");
                i.putExtra("url", "http://asian2bet.com/?ref=asian2bet");
                startActivity(i);
            }
        });
        grid5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GameActivity.class);
//                i.putExtra("url", "https://play.famobi.com/tri-peaks-solitaire-classic");
                i.putExtra("url", "http://asian2bet.com/?ref=asian2bet");
                startActivity(i);
            }
        });
        grid6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GameActivity.class);
//                i.putExtra("url", "https://play.famobi.com/dominoes-classic");
                i.putExtra("url", "http://asian2bet.com/?ref=asian2bet");
                startActivity(i);
            }
        });
        grid7.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GameActivity.class);
//                i.putExtra("url", "https://play.famobi.com/domino-shades");
                i.putExtra("url", "http://asian2bet.com/?ref=asian2bet");
                startActivity(i);
            }
        });
        grid8.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GameActivity.class);
//                i.putExtra("url", "https://play.famobi.com/solitaire-legend");
                i.putExtra("url", "http://asian2bet.com/?ref=asian2bet");
                startActivity(i);
            }
        });
        grid9.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GameActivity.class);
//                i.putExtra("url", "https://play.famobi.com/mahjong-flowers");
                i.putExtra("url", "http://asian2bet.com/?ref=asian2bet");
                startActivity(i);
            }
        });
        grid10.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ActivityNotFound.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        int count = this.getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            if (mBackPressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                clickDone();

            }
        } else {
            super.onBackPressed();
        }
    }

    public void clickDone() {
        new AlertDialog.Builder(this, R.style.DialogStyle)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.exit))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
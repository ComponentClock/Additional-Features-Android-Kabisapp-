package com.andro.siap;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMenu;

    private ArrayList<Game> list = new ArrayList<>();
    ImageView url,whatsapp;

    String link,wa;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        url = findViewById(R.id.url);
        whatsapp = findViewById(R.id.whatsaap);
        rvMenu = findViewById(R.id.menu);

        rvMenu.setHasFixedSize(true);
        rvMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("webview");


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = dataSnapshot.child("url").getValue(String.class);
                String whatsa = dataSnapshot.child("chat").getValue(String.class);

                link = value;
                wa = whatsa;

                if(value.isEmpty()){
                    url.setEnabled(false);
                }else {
                    url.setEnabled(true);
                }

                if(whatsa.isEmpty()){
                    whatsapp.setEnabled(false);
                }else {
                    whatsapp.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

            url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Uri uri = Uri.parse("googlechrome://navigate?url=" + link);
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } catch (ActivityNotFoundException e) {
                        // Chrome is probably not installed
                    }
                }
            });

            whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                myRef.setValue("NYOBAK IKI");
                    boolean installed = appInstalledOrNot("com.whatsapp");
                    if (installed) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + wa + "&text=HALO!"));
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Whats app not installed on your device", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        list.addAll(GameData.getListData());
        showRecyclerList();
    }

    private void showRecyclerList(){
        ListGameAdapter listGameAdapter = new ListGameAdapter(list);
        rvMenu.setAdapter(listGameAdapter);

        listGameAdapter.setOnItemClickCallback(new ListGameAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Game data) {
//                showSelectedHero(data);
                Intent intent = new Intent(MainActivity.this, WebviewActivity.class);
                intent.putExtra("url", data.getLink());
                startActivity(intent);
            }
        });
    }

    private void showSelectedHero(Game game) {
        Toast.makeText(this, "Kamu memilih " + game.getJudul(), Toast.LENGTH_SHORT).show();
    }

    //Create method appInstalledOrNot
    private boolean appInstalledOrNot(String url){
        PackageManager packageManager =getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }catch (PackageManager.NameNotFoundException e){
            app_installed = false;
        }
        return app_installed;
    }
}
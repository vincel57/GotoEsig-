package com.example.gotoesigeproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class Accueil extends AppCompatActivity {
BottomNavigationView bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accueil);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
       bottom = findViewById(R.id.bottom_nav);
        bottom.setOnItemSelectedListener(item -> {



            if (item.getItemId() == R.id.nav_home) {
                moveToFragment(new AccueilFragment());
            } else if (item.getItemId() == R.id.nav_trajets) {
                moveToFragment(new TrajetsFragment());
            } else if (item.getItemId() == R.id.nav_stats) {
                moveToFragment(new StatsFragment());
            } else if (item.getItemId() == R.id.nav_profil) {
                moveToFragment(new ProfilFragment());
            }

            return true;

        });


    }

    private void moveToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container_view, fragment).commit();
    }


}
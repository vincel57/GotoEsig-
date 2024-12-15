package com.example.gotoesigeproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gotoesigeproject.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
Button signbutton;
Button logbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        signbutton = findViewById(R.id.signbutton);
        signbutton.setOnClickListener(this);
        logbutton = findViewById(R.id.loginbutton);
        logbutton.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
    if(view==signbutton){
        Intent myIntent = new Intent(MainActivity.this, Inscription.class);
        startActivity(myIntent);
    }

    if(view==logbutton){
        Intent myIntent = new Intent(MainActivity.this, Connexion.class);
        startActivity(myIntent);
    }

    }
}
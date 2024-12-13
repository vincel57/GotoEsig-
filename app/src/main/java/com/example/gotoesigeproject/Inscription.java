package com.example.gotoesigeproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

public class Inscription extends AppCompatActivity {
    private EditText editNameText, editPrenomText, editPhoneText, editEmailText, editPasswordText, editVilleText;
    private Button validateButton, retourI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inscription);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        editNameText = findViewById(R.id.editnametext);
        editPrenomText = findViewById(R.id.editprenomtext);
        editPhoneText = findViewById(R.id.editTextNumber);
        editEmailText = findViewById(R.id.editTextTextEmailAddress);
        editPasswordText = findViewById(R.id.editTextTextPassword);
        editVilleText = findViewById(R.id.editVilleText);
        validateButton = findViewById(R.id.validate);
        retourI = findViewById(R.id.retourI);

        validateButton.setOnClickListener(view -> {
            String nom = editNameText.getText().toString();
            String prenom = editPrenomText.getText().toString();
            String email = editEmailText.getText().toString();
            String mdp = editPasswordText.getText().toString();
            String telephone = editPhoneText.getText().toString();
            String ville = editVilleText.getText().toString();

            if (nom.isEmpty() || prenom.isEmpty() || telephone.isEmpty() || email.isEmpty() || mdp.isEmpty()) {
                Toast.makeText(Inscription.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            } else {
                // Création d'un objet utilisateur
                Utilisateur user = new Utilisateur(nom, prenom, telephone, email, mdp,ville);
                db.collection("utilisateurs")
                        .add(user)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(Inscription.this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(Inscription.this, "Erreur d'inscription : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
        retourI.setOnClickListener(view -> {
            Intent myIntent = new Intent(Inscription.this, MainActivity.class);

            startActivity(myIntent);
        });

    }
}
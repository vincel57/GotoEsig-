package com.example.gotoesigeproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class Connexion extends AppCompatActivity {
    private EditText editEmailText, editPasswordText;
    private Button logbutton, retourC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_connexion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        editEmailText = findViewById(R.id.emailTextlog);
        editPasswordText = findViewById(R.id.mdpTextLog);
        logbutton = findViewById(R.id.conbutton);
        retourC = findViewById(R.id.retourC);

        logbutton.setOnClickListener(view -> {
            String email = editEmailText.getText().toString();
            String pass = editPasswordText.getText().toString();
            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
            } else {
                CollectionReference rf = db.collection("utilisateurs");
                Query query = rf.whereEqualTo("email", email).whereEqualTo("mdp", pass);
                query.get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        QuerySnapshot querySnapshot = task.getResult();

                        if (!querySnapshot.isEmpty()) {
                            String nom = querySnapshot.getDocuments().get(0).getString("nom");
                            String prenom = querySnapshot.getDocuments().get(0).getString("prenom");
                            String telephone = querySnapshot.getDocuments().get(0).getString("telephone");
                            String ville = querySnapshot.getDocuments().get(0).getString("ville");
                            String mail = querySnapshot.getDocuments().get(0).getString("email");

                            SharedPreferences sharedPreferences = getSharedPreferences("SessionUtilisateur", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("nom", nom);
                            editor.putString("prenom", prenom);
                            editor.putString("telephone", telephone);
                            editor.putString("ville", ville);
                            editor.putString("email", mail);
                            editor.apply();

                            Toast.makeText(this, "Connexion réussie !", Toast.LENGTH_SHORT).show();
                         Intent myIntent = new Intent(Connexion.this, Accueil.class);

                            startActivity(myIntent);

                        } else {
                            Toast.makeText(this, "Identifiants incorrects.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(this, "Erreur lors de la connexion. Réessayez.", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
        retourC.setOnClickListener(view -> {
            Intent myIntent = new Intent(Connexion.this, MainActivity.class);

            startActivity(myIntent);
        });

    }
}
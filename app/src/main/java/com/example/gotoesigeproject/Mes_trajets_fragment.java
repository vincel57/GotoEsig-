package com.example.gotoesigeproject;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;

import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Mes_trajets_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class Mes_trajets_fragment extends Fragment {
    TextView duree, heure, trajet ;
    CardView card;
    RecyclerView recyclerView;
   private List<Trajet> trajets;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Mes_trajets_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Mes_trajets_fragment.
     */

    // TODO: Rename and change types and number of parameters
    public static Mes_trajets_fragment newInstance(String param1, String param2) {
        Mes_trajets_fragment fragment = new Mes_trajets_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_mes_trajets_fragment, container, false);


        trajets = new ArrayList<>();
        setupAdapter(); // Configure l'Adapter dans le Fragment
        loadTrajetsFromFirestore();


        return view;
    }

    private void setupAdapter() {
        // Adapter défini directement dans le Fragment
        RecyclerView.Adapter adapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Charger le layout de l'élément
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_mes_trajets_fragment, parent, false);
                return new RecyclerView.ViewHolder(view) {};
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                // Remplir les données dans les vues
                Trajet trajet = trajets.get(position);

                TextView pointDepartArrivee = holder.itemView.findViewById(R.id.point_depart_arrivee);
                TextView dateHeure = holder.itemView.findViewById(R.id.date_heure);
                TextView dureeTotale = holder.itemView.findViewById(R.id.duree_totale);

                pointDepartArrivee.setText(trajet.getPointDepart() + " ➜ ESIGELEC");
                dateHeure.setText(trajet.getDate() + " à " + trajet.getHeure());
                dureeTotale.setText("Durée totale : " + trajet.getDuree());
            }

            @Override
            public int getItemCount() {
                return trajets.size();
            }
        };

        recyclerView.setAdapter(adapter);
    }

    private void loadTrajetsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trajets")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    trajets.clear(); // Nettoyer la liste existante
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Trajet trajet = document.toObject(Trajet.class);
                        trajets.add(trajet);
                    }
                    recyclerView.getAdapter().notifyDataSetChanged(); // Mettre à jour l'Adapter
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Erreur : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}

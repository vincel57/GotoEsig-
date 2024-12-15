package com.example.gotoesigeproject.Fragments;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gotoesigeproject.Adapter.ChercherAdapter;
import com.example.gotoesigeproject.Adapter.TrajetAdapter;
import com.example.gotoesigeproject.R;
import com.example.gotoesigeproject.Model.Trajet;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Chercher_trajet_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Chercher_trajet_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Chercher_trajet_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Chercher_trajet_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Chercher_trajet_fragment newInstance(String param1, String param2) {
        Chercher_trajet_fragment fragment = new Chercher_trajet_fragment();
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

        View v= inflater.inflate(R.layout.fragment_chercher_trajet_fragment, container, false);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("SessionUtilisateur", getContext().MODE_PRIVATE);
        String nom = sharedPreferences.getString("nom", "");
        String prenom = sharedPreferences.getString("prenom", "");
        String email = sharedPreferences.getString("email", "");


        EditText depart = v.findViewById(R.id.searchdepart);
        EditText date = v.findViewById(R.id.searchdate);
        Button search = v.findViewById(R.id.search);
        RecyclerView recyclerTrajets = v.findViewById(R.id.searchrecycler);


        recyclerTrajets.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Trajet> trajets = new ArrayList<>();
        ChercherAdapter adapter = new ChercherAdapter(trajets);
        recyclerTrajets.setAdapter(adapter);


        date.setOnClickListener(vi -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
                date.setText(String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        search.setOnClickListener(v1 -> {
            String pointDepart = depart.getText().toString().trim();
            String dateTrajet = date.getText().toString().trim();

            if (!pointDepart.isEmpty() && !dateTrajet.isEmpty()) {
                chercherTrajets(pointDepart, dateTrajet, trajets, adapter);
            } else {
                Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            }
        });


        return v;
    }

    private void chercherTrajets(String pointDepart, String date, List<Trajet> trajets, ChercherAdapter adapter) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("trajets")
                .whereEqualTo("pointDepart", pointDepart)
                .whereEqualTo("date", date)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        trajets.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Trajet trajet = document.toObject(Trajet.class);
                            trajets.add(trajet);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Erreur lors de la récupération des trajets", Toast.LENGTH_SHORT).show();
                    }
                });
    }



}
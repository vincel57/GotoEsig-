package com.example.gotoesigeproject.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gotoesigeproject.Model.Trajet;
import com.example.gotoesigeproject.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatsFragment newInstance(String param1, String param2) {
        StatsFragment fragment = new StatsFragment();
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
        View v = inflater.inflate(R.layout.fragment_stats, container, false);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("SessionUtilisateur", getContext().MODE_PRIVATE);
        String nom = sharedPreferences.getString("nom", "");
        String prenom = sharedPreferences.getString("prenom", "");
        String email = sharedPreferences.getString("email", "");

        TextView contribution = v.findViewById(R.id.txtContribution);
        TextView nbretrajet = v.findViewById(R.id.txtTrajetsCount);
        ArrayList<Trajet> trajetsList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference rf = db.collection("trajets");
        Query query = rf.whereEqualTo("email", email);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();

                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    double totalMontantEncaisse = 0;
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Log.d("FirestoreDebug", "Document ID : " + document.getId());
                        Log.d("FirestoreDebug", "Données : " + document.getData());

                        Trajet trajet = document.toObject(Trajet.class);
                        trajetsList.add(trajet);
                        totalMontantEncaisse += Double.parseDouble(trajet.getContribution());
                    }

                    nbretrajet.setText("Nombre de trajets proposés : " + trajetsList.size());
                    contribution.setText("Total encaissé : " + totalMontantEncaisse);
                    Toast.makeText(getContext(), "Stats récupérées!", Toast.LENGTH_SHORT).show();

                } else {
                    Log.d("FirestoreDebug", "Aucun trajet trouvé pour l'email : " + email);
                    Toast.makeText(getContext(), "Vous n'avez pas de trajets !", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("FirestoreError", "Erreur de récupération : ", task.getException());
                Toast.makeText(getContext(), "Erreur de récupération!", Toast.LENGTH_SHORT).show();
            }
        });



        return v;
    }

}
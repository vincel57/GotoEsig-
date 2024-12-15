package com.example.gotoesigeproject.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gotoesigeproject.Activity.Connexion;
import com.example.gotoesigeproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccueilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccueilFragment extends Fragment {
    Button btndec;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccueilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccueilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccueilFragment newInstance(String param1, String param2) {
        AccueilFragment fragment = new AccueilFragment();
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
        View view= inflater.inflate(R.layout.fragment_accueil, container, false);

        // Inflate the layout for this fragment
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("SessionUtilisateur", getContext().MODE_PRIVATE);
        String nom = sharedPreferences.getString("nom", "");
        String prenom = sharedPreferences.getString("prenom", "");
        TextView textView = view.findViewById(R.id.bienvenu);
        textView.setText("Bienvenue " + prenom + " " + nom);

        btndec = view.findViewById(R.id.deconnexion);
        btndec.setOnClickListener(b -> {
            SharedPreferences deco = requireActivity().getSharedPreferences("SessionUtilisateur", getContext().MODE_PRIVATE);
            SharedPreferences.Editor editor = deco.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(getActivity(), Connexion.class);
            startActivity(intent);

            requireActivity().finish();



        });
        return view;
    }
}
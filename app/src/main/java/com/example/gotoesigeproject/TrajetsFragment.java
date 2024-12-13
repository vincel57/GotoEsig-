package com.example.gotoesigeproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrajetsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrajetsFragment extends Fragment {
    private View navigationView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TrajetsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrajetsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrajetsFragment newInstance(String param1, String param2) {
        TrajetsFragment fragment = new TrajetsFragment();
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
        View view = inflater.inflate(R.layout.fragment_trajets, container, false);

        // Initialisation de la Toolbar et du NavigationView
        Toolbar toolbar = view.findViewById(R.id.toolBar);
        navigationView = view.findViewById(R.id.navigation_view);
        TextView actionText = view.findViewById(R.id.action_text);

        // Gestion de l'icône de navigation pour afficher/masquer le menu
        toolbar.setNavigationOnClickListener(v -> toggleMenu());

        // Gestion des clics dans le menu
        NavigationView navView = view.findViewById(R.id.navigation_view);
        navView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.action_mes_trajets){

                loadFragment(new Mes_trajets_fragment());
            } else if (item.getItemId() == R.id.action_chercher_trajet) {

                loadFragment(new Chercher_trajet_fragment());
            } else if (item.getItemId() == R.id.action_ajouter_trajet) {

                loadFragment(new Ajout_trajet_fragment());

            }

            toggleMenu(); // Masquer le menu après sélection
            return true;
        });

        return view;
    }

    // Fonction pour afficher/masquer le menu
    private void toggleMenu() {
        if (navigationView.getVisibility() == View.GONE) {
            navigationView.setVisibility(View.VISIBLE);
        } else {
            navigationView.setVisibility(View.GONE);
        }
    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.container_fragment, fragment);
        transaction.addToBackStack(null); // Permet de revenir en arrière
        transaction.commit();
    }





}
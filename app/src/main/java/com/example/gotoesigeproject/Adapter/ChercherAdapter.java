package com.example.gotoesigeproject.Adapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gotoesigeproject.Fragments.CarteFragment;
import com.example.gotoesigeproject.Model.Trajet;
import com.example.gotoesigeproject.R;

import java.util.List;

public class ChercherAdapter extends RecyclerView.Adapter<ChercherAdapter.ChercherViewHolder> {

    private List<Trajet> trajets;

    public ChercherAdapter(List<Trajet> trajets) {
        this.trajets = trajets;
    }

    @NonNull
    @Override
    public ChercherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_search, parent, false);
        return new ChercherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChercherViewHolder holder, int position) {

        Trajet trajet = trajets.get(position);
        holder.pointDepart.setText(trajet.getPointDepart() +"->"+"ESIGELEC");
        holder.date.setText(trajet.getDate());
        holder.duree.setText(trajet.getDuree());


        holder.itemView.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("SessionTrajet", v.getContext().MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("pointDepart", trajet.getPointDepart());
            editor.putString("date", trajet.getDate());
            editor.putString("duree", trajet.getDuree());
            editor.apply();
            CarteFragment carteFragment = new CarteFragment();

            ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.chercher_fragment, carteFragment) // Remplace avec l'ID de ton conteneur
                    .addToBackStack(null) // Ajoute la transaction à la pile pour permettre de revenir en arrière
                    .commit();




        });
    }

    @Override
    public int getItemCount() {
        return trajets.size();
    }

    public static class ChercherViewHolder extends RecyclerView.ViewHolder {
        TextView pointDepart, date, duree;

        public ChercherViewHolder(@NonNull View itemView) {
            super(itemView);
            pointDepart = itemView.findViewById(R.id.point_depart_arrivee_search);
            date = itemView.findViewById(R.id.date_heure_search);
            duree = itemView.findViewById(R.id.duree_totale_search);
        }
    }
}


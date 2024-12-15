package com.example.gotoesigeproject.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gotoesigeproject.R;
import com.example.gotoesigeproject.Model.Trajet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TrajetAdapter extends RecyclerView.Adapter<TrajetAdapter.TrajetViewHolder> {

    private List<Trajet> trajets;

    public TrajetAdapter(List<Trajet> trajets) {
        this.trajets = trajets;
    }

    @NonNull
    @Override
    public TrajetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new TrajetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrajetViewHolder holder, int position) {
        Trajet trajet = trajets.get(position);
        holder.pointDepartArrivee.setText(trajet.getPointDepart() +"->"+"ESIGELEC");
        holder.dateHeure.setText(String.format("%s %s", trajet.getDate(), trajet.getHeure()));
        holder.dureeTotale.setText(String.format(trajet.getDuree()));

        boolean estRealise = estTrajetRealise(trajet.getDate(), trajet.getHeure());
        if (estRealise) {
            holder.icon.setImageResource(R.drawable.baseline_check_circle_outline_24); // Icône verte
            holder.statut.setText("Réalisé");
            holder.statut.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.forest_green));
        } else {
            holder.icon.setImageResource(R.drawable.baseline_hourglass_empty_24);
            holder.statut.setText("En cours");
            holder.statut.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.yellow));
        }
    }

    @Override
    public int getItemCount() {
        return trajets.size();
    }

    public static class TrajetViewHolder extends RecyclerView.ViewHolder {

        TextView pointDepartArrivee, dateHeure, dureeTotale,statut;
        ImageView icon;

        public TrajetViewHolder(@NonNull View itemView) {
            super(itemView);
            pointDepartArrivee = itemView.findViewById(R.id.point_depart_arrivee);
            dateHeure = itemView.findViewById(R.id.date_heure);
            dureeTotale = itemView.findViewById(R.id.duree_totale);
            icon = itemView.findViewById(R.id.icon);
            statut = itemView.findViewById(R.id.statut);


        }
    }
    private boolean estTrajetRealise(String dateTrajet, String heureTrajet) {
        try {

            String dateHeureStr = dateTrajet + " " + heureTrajet;

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            Date dateHeureTrajet = format.parse(dateHeureStr);


            Date maintenant = new Date();

            return dateHeureTrajet != null && dateHeureTrajet.before(maintenant);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
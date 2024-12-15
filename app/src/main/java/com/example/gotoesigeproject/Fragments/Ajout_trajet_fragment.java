package com.example.gotoesigeproject.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.example.gotoesigeproject.R;
import com.example.gotoesigeproject.Model.Trajet;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Ajout_trajet_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Ajout_trajet_fragment extends Fragment {
    private FirebaseFirestore firestore;
    private ArrayAdapter<String> transportAdapter;
    private ArrayList<String> transportList;
    private Spinner spinnerTransport;
    private EditText editPointDepart, editDate, editHeure, editRetardTolere, editPlaces, editContribution;
    private Button btnValider;
    private TextView editDistance, editDuration,editEmail;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
private String email;
    public Ajout_trajet_fragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Ajout_trajet_fragment newInstance(String param1, String param2) {
        Ajout_trajet_fragment fragment = new Ajout_trajet_fragment();
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
        View v = inflater.inflate(R.layout.fragment_ajout_trajet_fragment, container, false);

        // Accéder aux SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("SessionUtilisateur", getContext().MODE_PRIVATE);
        String nom = sharedPreferences.getString("nom", "");
        String prenom = sharedPreferences.getString("prenom", "");
        String email = sharedPreferences.getString("email", "");

        // Initialisation des éléments de l'UI
        spinnerTransport = v.findViewById(R.id.spinner_transport);
        editPointDepart = v.findViewById(R.id.edit_point_depart);
        editDate = v.findViewById(R.id.edit_date);
        editHeure = v.findViewById(R.id.edit_heure);
        editRetardTolere = v.findViewById(R.id.edit_retard_tolere);
        editPlaces = v.findViewById(R.id.edit_places);
        editContribution = v.findViewById(R.id.edit_contribution);
        btnValider = v.findViewById(R.id.btn_valider);
        editDuration = v.findViewById(R.id.editDuration);
        editDistance = v.findViewById(R.id.editDistance);
        editEmail = v.findViewById(R.id.editEmail);
editEmail.setText(email);
        // Initialisation Firestore
        firestore = FirebaseFirestore.getInstance();

        // Configuration des moyens de transport
        transportList = new ArrayList<>();
        transportAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, transportList);
        transportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTransport.setAdapter(transportAdapter);

        // Charger les options de transport depuis Firestore
        loadTransportOptions();

        // Gestion des événements
        setupDatePicker();
        setupTimePicker();
        setupTransportSpinner();

        // Action sur le bouton de validation
        btnValider.setOnClickListener(vi -> validateAndSubmit());

        return v;
    }

    private void loadTransportOptions() {
        firestore.collection("transport").get().addOnSuccessListener(queryDocumentSnapshots -> {
            transportList.clear();
            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                transportList.add(document.getString("Type")); // Ajoute chaque nom à la liste
            }
            transportAdapter.notifyDataSetChanged(); // Met à jour l'adaptateur
        }).addOnFailureListener(e -> {
            Toast.makeText(requireContext(), "Erreur lors du chargement des moyens de transport", Toast.LENGTH_SHORT).show();
        });
    }


    private void setupDatePicker() {
        editDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
                editDate.setText(String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void setupTimePicker() {
        editHeure.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new TimePickerDialog(requireContext(), (view, hourOfDay, minute) -> {
                editHeure.setText(String.format("%02d:%02d", hourOfDay, minute));
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        });
    }

    private void setupTransportSpinner() {
        spinnerTransport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTransport = transportList.get(position);
                if (selectedTransport.equalsIgnoreCase("Véhicule")) {
                    editContribution.setVisibility(View.VISIBLE);
                } else {
                    editContribution.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void validateAndSubmit() {
        Log.d("TrajetsFragment", "Bouton Valider cliqué"); // Ajout de log

        String pointDepart = editPointDepart.getText().toString();
        String date = editDate.getText().toString();
        String heure = editHeure.getText().toString();
        String retardTolere = editRetardTolere.getText().toString();
        String places = editPlaces.getText().toString();
        String contribution = editContribution.getText().toString();


        if (pointDepart.isEmpty() || date.isEmpty() || heure.isEmpty() || retardTolere.isEmpty() || places.isEmpty()) {
            Toast.makeText(requireContext(), "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        // Étape 1 : Récupérer les coordonnées du point de départ et de l'ESIGELEC
        String esigelecAddress = "ESIGELEC";
        getCoordinates(pointDepart, coordinatesDepart -> {

            getCoordinates(esigelecAddress, coordinatesArrivee -> {
                // Étape 2 : Utiliser les coordonnées pour récupérer la distance et la durée
                getDistanceAndDuration (coordinatesDepart[1], coordinatesDepart[0], coordinatesArrivee[1],coordinatesArrivee[0], (distance, duration) -> {
                    // Mettre à jour les TextViews
                    editDistance.setText(String.format("Distance : %s km", distance));
                    editDuration.setText(String.format("Durée : %s minutes", duration));

                    String duree = editDuration.getText().toString();
                    String distanceK = editDistance.getText().toString();
                    String mail = editEmail.getText().toString();
                    ArrayList<String> inscrits = new ArrayList<>();
                    inscrits.add(mail);


                    // Étape 3 : Confirmation avant l'ajout
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Confirmation")
                            .setMessage("Voulez-vous ajouter ce trajet avec les informations fournies?")
                            .setPositiveButton("Oui", (dialog, which) -> {
                                // Ajouter les données à Firestore
                                firestore.collection("trajets")
                                        .add(new Trajet(pointDepart, date, heure, retardTolere, places, contribution, duree, distanceK, mail, inscrits))
                                        .addOnSuccessListener(documentReference -> {
                                            Toast.makeText(requireContext(), "Trajet ajouté avec succès", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(requireContext(), "Erreur lors de l'ajout du trajet : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });                            })
                            .setNegativeButton("Non", null)
                            .show();
                });
            });
        });
    }
    private void getCoordinates(String address, OnCoordinatesFetchedListener listener) {
        Log.d("cordinate", "Méthode lancée"); // Ajout de log

        String url = "https://api.openrouteservice.org/geocode/search?api_key=5b3ce3597851110001cf6248d11ab086b9554b5582d817a4e6149d6e&text=" + address;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Erreur lors de la récupération des coordonnées", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseBody);
                        JSONArray features = jsonObject.getJSONArray("features");
                        if (features.length() > 0) {
                            JSONObject geometry = features.getJSONObject(0).getJSONObject("geometry");
                            JSONArray coordinates = geometry.getJSONArray("coordinates");
                            double longitude = coordinates.getDouble(0);
                            double latitude = coordinates.getDouble(1);
                            Log.d("LongLat", " Fini"); // Ajout de log


                            listener.onCoordinatesFetched(new double[]{longitude, latitude});
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getDistanceAndDuration(double startLat, double startLng, double endLat, double endLng, OnDistanceAndDurationFetchedListener listener) {
        OkHttpClient client = new OkHttpClient();
        String url = String.format(
                "https://api.openrouteservice.org/v2/directions/driving-car?api_key=5b3ce3597851110001cf6248d11ab086b9554b5582d817a4e6149d6e&start=%f,%f&end=%f,%f",
                startLng, startLat, endLng, endLat);

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Erreur dans la réponse de l'API", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                String responseBody = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(responseBody);
                    // Accéder à la liste "features"
                    JSONArray features = jsonObject.getJSONArray("features");
                    if (features.length() > 0) {
                        // Obtenir le premier élément de "features"
                        JSONObject feature = features.getJSONObject(0);

                        // Accéder à "properties" > "summary"
                        JSONObject summary = feature.getJSONObject("properties").getJSONObject("summary");

                        // Récupérer la distance et la durée
                        double distance = summary.getDouble("distance") / 1000; // Convertir en kilomètres
                        double duration = summary.getDouble("duration") / 60;  // Convertir en minutes

                        // Appeler le listener avec les données formatées
                        requireActivity().runOnUiThread(() -> {
                            Log.d("disdur", " fini"); // Ajout de log
                            listener.onDistanceAndDurationFetched(String.format("%.2f", distance), String.format("%.2f", duration));
                        });
                    } else {
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(requireContext(), "Aucun trajet trouvé", Toast.LENGTH_SHORT).show();
                        });
                    }
                } catch (JSONException e) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Erreur de parsing JSON", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    interface OnCoordinatesFetchedListener {
        void onCoordinatesFetched(double[] coordinates);
    }

    interface OnDistanceAndDurationFetchedListener {
        void onDistanceAndDurationFetched(String distance, String duration);
    }


}






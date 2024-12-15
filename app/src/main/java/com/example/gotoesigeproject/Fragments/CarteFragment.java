package com.example.gotoesigeproject.Fragments;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.gotoesigeproject.Model.Trajet;
import com.example.gotoesigeproject.R;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MapView map;

    public CarteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CarteFragment newInstance(String param1, String param2) {
        CarteFragment fragment = new CarteFragment();
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
        View v = inflater.inflate(R.layout.fragment_carte, container, false);
        Configuration.getInstance().setUserAgentValue(requireContext().getPackageName());

        SharedPreferences sh = requireActivity().getSharedPreferences("SessionTrajet", getContext().MODE_PRIVATE);
        String depart = sh.getString("pointDepart","");
        String date = sh.getString("date","");
        String duree = sh.getString("duree","");

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("SessionUtilisateur", getContext().MODE_PRIVATE);
        String nom = sharedPreferences.getString("nom", "");
        String prenom = sharedPreferences.getString("prenom", "");
        String email = sharedPreferences.getString("email", "");


        Button retour = v.findViewById(R.id.retourtrajet);
        Button inscrit = v.findViewById(R.id.inscriretrajet);
         map = v.findViewById(R.id.map);
         map.setMultiTouchControls(true);
         map.getController().setZoom(15.0);
         String esigelecAddress = "ESIGELEC";
        getCoordinates(depart, coordinatesDepart -> {

            getCoordinates(esigelecAddress, coordinatesArrivee -> {

                getWayPoints(coordinatesDepart[1], coordinatesDepart[0], coordinatesArrivee[1], coordinatesArrivee[0]);


            });
        });
        retour.setOnClickListener(view -> {
            Chercher_trajet_fragment fragment = new Chercher_trajet_fragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.cartefragment, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

        });

        inscrit.setOnClickListener(view -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("trajets")
                    .whereEqualTo("pointDepart", depart) // Filtre sur le point de départ
                    .whereEqualTo("date", date)              // Filtre sur la date
                    .whereEqualTo("duree", duree)            // Filtre sur l'heure
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Trajet trajet = document.toObject(Trajet.class);

                                    if (trajet.getInscrits().size() < Integer.parseInt(trajet.getPlaces())) {

                                        db.collection("trajets").document(document.getId())
                                                .update("inscrits", FieldValue.arrayUnion(email))
                                                .addOnSuccessListener(aVoid -> {
                                                    Toast.makeText(getContext(), "Inscription réussie!", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(getContext(), "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show();
                                                });
                                    } else {
                                        Toast.makeText(getContext(), "Plus de places disponibles", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(getContext(), "Aucun trajet correspondant trouvé", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Erreur lors de la récupération des trajets", Toast.LENGTH_SHORT).show();
                        }
                    });


        });


        return v;
    }

    private void getCoordinates(String address, OnCoordinatesFetchedListener listener) {
        Log.d("cordinate", "Méthode lancée");

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

                            listener.onCoordinatesFetched(new double[]{longitude, latitude});
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getWayPoints(double startLat, double startLng, double endLat, double endLng) {
        OkHttpClient client = new OkHttpClient();
        String url = String.format(
                "https://api.openrouteservice.org/v2/directions/driving-car?api_key=5b3ce3597851110001cf6248d11ab086b9554b5582d817a4e6149d6e&start=%f,%f&end=%f,%f",
                startLng, startLat, endLng, endLat);

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Erreur réseau", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "Erreur réponse API", Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                String responseBody = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseBody);
                    JSONArray features = jsonObject.getJSONArray("features");
                    JSONObject geometry = features.getJSONObject(0).getJSONObject("geometry");
                    JSONArray coordinates = geometry.getJSONArray("coordinates");

                    JSONObject properties = features.getJSONObject(0).getJSONObject("properties");
                    JSONArray segments = properties.getJSONArray("segments");


                    ArrayList<GeoPoint> waypoints = new ArrayList<>();


                    for (int i = 0; i < segments.length(); i++) {
                        JSONArray steps = segments.getJSONObject(i).getJSONArray("steps");

                        for (int j = 0; j < steps.length(); j++) {
                            JSONArray wayPointsIndexes = steps.getJSONObject(j).getJSONArray("way_points");


                            for (int k = 0; k < wayPointsIndexes.length(); k++) {
                                int index = wayPointsIndexes.getInt(k);
                                JSONArray point = coordinates.getJSONArray(index);
                                double longitude = point.getDouble(0);
                                double latitude = point.getDouble(1);

                                waypoints.add(new GeoPoint(latitude, longitude));
                            }
                        }
                    }

                    // Ajouter directement les waypoints à la carte
                    requireActivity().runOnUiThread(() -> {
                        Polyline polyline = new Polyline(map);
                        polyline.setColor(Color.BLUE);
                        polyline.setWidth(5);
                        polyline.setPoints(waypoints);
                        map.getOverlays().add(polyline);

                        // Centrer la carte sur les waypoints
                        if (!waypoints.isEmpty()) {

                            GeoPoint startPoint = waypoints.get(0); // Premier point
                            GeoPoint endPoint = waypoints.get(waypoints.size() - 1); // Dernier point

                            // Ajouter le marqueur du départ
                            addMarker(startPoint, "Départ", R.drawable.baseline_location_pin_24); // Remplace par ton icône de départ

                            // Ajouter le marqueur de l'arrivée
                            addMarker(endPoint, "Arrivée", R.drawable.baseline_location_pin_24); // Remplace par ton icône d'arrivée

                            map.getController().setCenter(waypoints.get(0));
                            BoundingBox boundingBox = getBoundingBox(waypoints);
                            map.zoomToBoundingBox(boundingBox, true);
                        }

                        map.invalidate();
                    });

                } catch (JSONException e) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "Erreur de parsing JSON", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
    private BoundingBox getBoundingBox(ArrayList<GeoPoint> points) {
        double minLat = Double.MAX_VALUE, maxLat = Double.MIN_VALUE;
        double minLon = Double.MAX_VALUE, maxLon = Double.MIN_VALUE;

        for (GeoPoint point : points) {
            double lat = point.getLatitude();
            double lon = point.getLongitude();

            if (lat < minLat) minLat = lat;
            if (lat > maxLat) maxLat = lat;
            if (lon < minLon) minLon = lon;
            if (lon > maxLon) maxLon = lon;
        }

        return new BoundingBox(maxLat, maxLon, minLat, minLon);
    }

    private void addMarker(GeoPoint point, String title, int iconResId) {
        Marker marker = new Marker(map);
        marker.setPosition(point);
        marker.setTitle(title);
        marker.setIcon(getResources().getDrawable(iconResId, null));
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marker);

    }

    interface OnCoordinatesFetchedListener {
        void onCoordinatesFetched(double[] coordinates);
    }




}
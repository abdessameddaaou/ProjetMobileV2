package com.example.projetmobilev2;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.projetmobilev2.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Ajouter des marqueurs à la carte pour chaque position
        for (int i = 0; i < AllPositions.trajet_id.size(); i++) {
            double lat = Double.parseDouble(AllPositions.trajet_lat.get(i));
            double lon = Double.parseDouble(AllPositions.trajet_lon.get(i));
            String nomTrajet = AllPositions.trajet_nom.get(i);

            LatLng position = new LatLng(lat, lon);
            mMap.addMarker(new MarkerOptions().position(position).title(nomTrajet));
        }

        // Déplacer la caméra pour afficher tous les marqueurs
        if (!AllPositions.trajet_lat.isEmpty() && !AllPositions.trajet_lon.isEmpty()) {
            double lat = Double.parseDouble(AllPositions.trajet_lat.get(0));
            double lon = Double.parseDouble(AllPositions.trajet_lon.get(0));
            LatLng initialPosition = new LatLng(lat, lon);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, 10f));
        }
    }

}
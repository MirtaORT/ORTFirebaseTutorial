package com.ort.ortfirebasetutorial.ui.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ort.ortfirebasetutorial.R;

import org.json.JSONObject;

public class MapFragment extends Fragment implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    private MapViewModel mapViewModel;
    private GoogleMap mMap;
    private static final LatLng EL_CLUB_DE_LA_BIRRA = new LatLng(-34.6253417,-58.427949);
    private static final LatLng ORT_YATAY = new LatLng(-34.6099906,-58.431331);
    private static final LatLng ORT_BELGRANO = new LatLng(-34.5497116,-58.4563225);
    private static final LatLng DISCO_J_M_MOREO = new LatLng(-34.5497116,-58.4563225);

    private Marker mBirra;
    private Marker mOrtYatay;
    private Marker mOrtBelgrano;
    private Marker mDiscoJMM;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mapViewModel =
                ViewModelProviders.of(this).get(MapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        //   final TextView textView = root.findViewById(R.id.text_gallery);
        mapViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //     textView.setText(s);
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    Boolean actualPosition = true;
    JSONObject jso;
    Double longitudOrigen, latitudOrigen;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Chequeo los permisos
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

                return;
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                return;
            }

        }

        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        //lo pueden cambiar por el que mas les guste
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //son las opciones de navegacion
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false); // controles de zoom
        uiSettings.setMyLocationButtonEnabled(true); //boton de mi ubicacion

        //agrego marcadores
        // Add some markers to the map, and add a data object to each marker.
        mBirra = mMap.addMarker(new MarkerOptions()
                .position(EL_CLUB_DE_LA_BIRRA)
                .title("El Club de La Birra"));
        mBirra.setTag(0);

        mOrtYatay = mMap.addMarker(new MarkerOptions()
                .position(ORT_YATAY)
                .title("ORT Almagro"));
        mOrtYatay.setTag(0);

        mOrtBelgrano = mMap.addMarker(new MarkerOptions()
                .position(ORT_BELGRANO)
                .title("ORT Belgrano"));
        mOrtBelgrano.setTag(0);

        mDiscoJMM = mMap.addMarker(new MarkerOptions()
                .position(DISCO_J_M_MOREO)
                .title("Disco J M Moreno"));
        mDiscoJMM.setTag(0);

        //para simular el click del set location y mover la camara a donde estoy ahora
        GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange (Location location) {
                LatLng loc = new LatLng (location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                mMap.addMarker(new MarkerOptions()
                        .position(loc)
                        .title("Estas Acà Perdido!!")
                        .snippet("Este sos vos!!")
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

                );

            }
        };
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);


    }






    @Override
    /*estoy dentro de un fragment y por esto tengo que llamar a super
    * */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.

                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.

                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.

    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getContext(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getContext(), "Current location:\n" + location, Toast.LENGTH_LONG).show();

    }
}



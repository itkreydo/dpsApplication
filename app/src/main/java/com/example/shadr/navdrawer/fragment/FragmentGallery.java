package com.example.shadr.navdrawer.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shadr.navdrawer.NavigationDrawer;
import com.example.shadr.navdrawer.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;


public class FragmentGallery extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private LocationManager locationManager;
    SupportMapFragment mapFragment;
    GoogleMap map;

    Bitmap marker_picture;
    AlertDialog alert;

    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public FragmentGallery() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentGallery newInstance(String param1, String param2) {
        FragmentGallery fragment = new FragmentGallery();
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
        requestPermissions(INITIAL_PERMS, 1337);
        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_gallery, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ImageButton menuswitch = v.findViewById(R.id.menuswitch);
        menuswitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setTag(R.drawable.ic_baseline_my_location_24px);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch ((Integer) view.getTag()) {
                    case R.drawable.ic_baseline_my_location_24px:

                        showDialog();


                        ((FloatingActionButton) view).setImageDrawable(getResources().getDrawable(R.drawable.ic_close_black_24dp));
                        view.setTag(R.drawable.ic_close_black_24dp);

                        // Добавляем прицел чтобы поставить метку
                        ImageView imageView = new ImageView(getContext());
                        imageView.setId(R.id.target);
                        imageView.setImageResource(R.drawable.ic_baseline_my_location_24px);
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.gravity = Gravity.CENTER;
                        layoutParams.width = dp_to_px(36);
                        layoutParams.height = dp_to_px(36);
                        imageView.setLayoutParams(layoutParams);
                        ((FrameLayout) getView()).addView(imageView);

                        //  Добавляем кнопку check чтобы поставить метку
                        LinearLayout linearLayout = getView().findViewById(R.id.buttons_map);
                        FloatingActionButton floatingActionButton = new FloatingActionButton(getContext());
                        floatingActionButton.setId(R.id.addtarget);
                        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_black_24dp));
                        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams1.setMargins(dp_to_px(16), dp_to_px(16), dp_to_px(16), dp_to_px(16));
                        floatingActionButton.setLayoutParams(layoutParams1);
                        linearLayout.addView(floatingActionButton);
                        // Срабатывает когда ставим метку
                        floatingActionButton.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public void onClick(View v) {
                                LatLng center = map.getCameraPosition().target;
                                LatLng location = new LatLng(locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER).getLatitude(),
                                        locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER).getLongitude());
                                marker_put(location, center);
                            }
                        });

                        break;
                    case R.drawable.ic_close_black_24dp:
                        ((FloatingActionButton) view).setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_my_location_24px));
                        view.setTag(R.drawable.ic_baseline_my_location_24px);
                        // удаляем кнопку добавить маркер
                        LinearLayout linearLayout1 = getView().findViewById(R.id.buttons_map);
                        FloatingActionButton add = getView().findViewById(R.id.addtarget);
                        linearLayout1.removeView(add);
                        // удаляем прицел для метки
                        ImageView picture = getView().findViewById(R.id.target);
                        ((FrameLayout) getView()).removeView(picture);
                        break;
                }


            }

        });
        // Inflate the layout for this fragment
        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 10, 10, locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
                locationListener);

    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    // Показываем диалог через нажатие на кнопку "добавить метку"
    private void showDialog(final LatLng location, final LatLng place) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
        LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_title, null);
        adb.setCustomTitle(view);
        view = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog, null);
        adb.setView(view);
        // Нажата кнопка ДПС
        ImageButton button = view.findViewById(R.id.dps_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marker_picture = BitmapFactory.decodeResource(getResources(), R.raw.dps_icon);
                if(location.longitude!=0 && location.latitude!=0 && place.longitude!=0 && place.latitude!=0)
                    marker_put(location, place);
                alert.dismiss();
            }
        });

        // Нажата кнопка Камера
        button = view.findViewById(R.id.camera_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marker_picture = BitmapFactory.decodeResource(getResources(), R.raw.cam);
                if(location.longitude!=0 && location.latitude!=0 && place.longitude!=0 && place.latitude!=0)
                    marker_put(location, place);
                alert.dismiss();
            }
        });

        // Нажата кнопка Помощь
        button = view.findViewById(R.id.help_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marker_picture = BitmapFactory.decodeResource(getResources(), R.raw.sos);
                if(location.longitude!=0 && location.latitude!=0 && place.longitude!=0 && place.latitude!=0)
                    marker_put(location, place);
                alert.dismiss();
            }
        });

        // Если закрыли диалоговое очко
        adb.setCancelable(true);
        adb.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                marker_picture = null;
                FloatingActionButton fab = getView().findViewById(R.id.fab);
                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_my_location_24px));
                fab.setTag(R.drawable.ic_baseline_my_location_24px);
                // удаляем кнопку добавить маркер
                LinearLayout linearLayout1 = getView().findViewById(R.id.buttons_map);
                FloatingActionButton add = getView().findViewById(R.id.addtarget);
                linearLayout1.removeView(add);
                // удаляем прицел для метки
                ImageView picture = getView().findViewById(R.id.target);
                ((FrameLayout) getView()).removeView(picture);
            }
        });
        alert = adb.create();
        alert.show();
    }

    // Показываем диалог через долгое нажатие по карте
    private void showDialog() {
        showDialog(new LatLng(0,0), new LatLng(0,0));
    }
    // Ставим маркер на карте
    private boolean marker_put(LatLng location, LatLng place) {
        float[] distance = new float[1];
        Location.distanceBetween(place.latitude, place.longitude, location.latitude, location.longitude, distance);
        if(distance[0]<=3000) {

            map.addMarker(new MarkerOptions().position(place).icon(BitmapDescriptorFactory.fromBitmap(marker_picture)));
            // удаляем кнопку check
            LinearLayout linearLayout1 = getView().findViewById(R.id.buttons_map);
            FloatingActionButton add = getView().findViewById(R.id.addtarget);
            linearLayout1.removeView(add);
            // меняем картинку у кнопки
            add = getView().findViewById(R.id.fab);
            add.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_my_location_24px));
            add.setTag(R.drawable.ic_baseline_my_location_24px);
            // удаляем прицел для метки
            ImageView picture = getView().findViewById(R.id.target);
            ((FrameLayout) getView()).removeView(picture);
            return true;
        }
        else {
            Toast.makeText(getContext(),"Вы находитесь слишком далеко от метки", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setMyLocationEnabled(true);
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                LatLng location = new LatLng(locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER).getLatitude(),
                        locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER).getLongitude());
                showDialog(location, latLng);
            }
        });

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER).getLatitude(),
                        locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER).getLongitude()))
                .zoom(16)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);
    }

    private int dp_to_px(int dp) {
        Resources r = getContext().getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        return px;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

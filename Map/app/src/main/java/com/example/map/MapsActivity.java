package com.example.map;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ListView listView;
    private CustomAdapter customAdapter;
    private List<Places>list;
    private AlertDialog dialog;
    private RelativeLayout map_layout;
    private RelativeLayout list_layout;
    DatabaseHelper myDB;
    private boolean visibility = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        list_layout = findViewById(R.id.list_layout);
        map_layout = findViewById(R.id.map_layout);
        listView = findViewById(R.id.listofplaces);
        list = new ArrayList<>();
        customAdapter = new CustomAdapter(this, list);
        listView.setAdapter(customAdapter);
        myDB = new DatabaseHelper(this);

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
        for(int i=0;i<list.size();i++){
            LatLng pos = new LatLng(list.get(i).getLatitude(),list.get(i).getLongitude());
            mMap.addMarker(new MarkerOptions().position(pos).title(list.get(i).getTitle()).snippet(list.get(i).getDescription()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsActivity.this);
                final View mView =getLayoutInflater().inflate(R.layout.dialog_place,null);
                final EditText location_name =mView.findViewById(R.id.dialog_name);
                final EditText location_description = mView.findViewById(R.id.dialog_description);
                final String loc = location_name.getText().toString();
                final String desc = location_description.getText().toString();
                Button yes = mView.findViewById(R.id.yes);
                Button no = mView.findViewById(R.id.no);

//                LatLng sydney = new LatLng(-34, 151);
//                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney").snippet("sdsdwdw"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMap.addMarker(new MarkerOptions().position(latLng).title(loc).snippet(desc));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        Places places = new Places(location_name.getText().toString(),location_description.getText().toString(),latLng.latitude,latLng.longitude);
                        //list.add(0,new Places(location_name.getText().toString(),location_description.getText().toString(),latLng.latitude,latLng.longitude));
                        myDB.insertNote(places);
                        list = myDB.getNotes();
                        customAdapter.setContents(list);
                        dialog.cancel();
                    }
                });
                mBuilder.setView(mView);
                dialog = mBuilder.create();
                dialog.show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LatLng pos = new LatLng(list.get(position).getLatitude(),list.get(position).getLongitude());
                map_layout.setVisibility(View.VISIBLE);
                list_layout.setVisibility(View.GONE);
                //map.moveCamera(CameraUpdateFactory.newLatLng(pos));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(pos));
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        list = myDB.getNotes();
        customAdapter = new CustomAdapter(this,list);
        listView.setAdapter(customAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.places:
                if (visibility == false) {
                    list_layout.setVisibility(View.VISIBLE);
                    map_layout.setVisibility(View.INVISIBLE);
                    visibility = true;
                }
                else {
                    list_layout.setVisibility(View.INVISIBLE);
                    map_layout.setVisibility(View.VISIBLE);
                    visibility=false;
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void radioClicked(View view){
     switch (view.getId()){
         case R.id.standard:
             mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
             break;
         case R.id.satellite:
             mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
             break;
         case R.id.hybrid:
             mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
             break;
     }
    }

}

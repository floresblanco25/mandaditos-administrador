package com.mandaditos.administrador;

import android.location.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v7.app.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.inputmethod.*;
import android.widget.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.tasks.*;
import com.mandaditos.administrador.*;
import java.io.*;
import java.util.*;

import com.mandaditos.administrador.R;

public class mapPicker extends AppCompatActivity implements OnMapReadyCallback
{

	private MapView mapView;
    private GoogleMap gmap;
	private MarkerOptions marker;
	public static String tag ="mapPicker";
	private LatLng newLatLng;
	private LatLng latLng;
	private Bundle args;
	private String partidaODestino;
	private Button guardarPosBt,BuscarButton;
	String TAG = "search map";
	private EditText mSearchText;








	//map ready
	@Override
	public void onMapReady(GoogleMap p1)
	{
		gmap = p1;
        gmap.setMinZoomPreference(12);
		gmap.setMyLocationEnabled(false);
		
		gmap.clear();
		marker = new MarkerOptions().position(latLng);
		newLatLng = latLng;
		gmap.addMarker(marker);
		
		

			gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,13));
			CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(latLng)
				.zoom(20).bearing(90).build();          
			gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));       


		//add marker onckick
		gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
				@Override
				public void onMapClick(LatLng point) {
					gmap.clear();
					marker = new MarkerOptions().position(point);
					newLatLng = point;
					gmap.addMarker(marker);
				}
			});
		guardarPosBt.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					mAdapter.setLatLng(partidaODestino,newLatLng);
					Toast.makeText(p1.getContext(),"Guardado",Toast.LENGTH_SHORT).show();
					finish();
				}
			});
			


	}













	//Constructor
	public static mapPicker newInstance()
	{
        mapPicker fragment = new mapPicker();
        return fragment;
    }

	private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";









	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		args = getIntent().getExtras();
		latLng = args.getParcelable("latLng");
		partidaODestino = args.getString("partidaODestino");
		setContentView(R.layout.map_marker_edit);
		guardarPosBt = findViewById(R.id.address_pickerButtonSaveMarker);
		BuscarButton = findViewById(R.id.BuscarmapmarkereditButton1);
		
		
		
		
		Bundle mapViewBundle = null;
		if (savedInstanceState != null) {
			mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
		}
		mapView = findViewById(R.id.map_view_picker);
		mapView.onCreate(mapViewBundle);
		mapView.getMapAsync(this);
		mSearchText = findViewById(R.id.BuscarmapmarkereditEditText1);
		
		
		BuscarButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					searchLocation();
					// TODO: Implement this method
				}
			});




		
		
		


























	}




	
	
	
	
	
	
//search
	public void searchLocation() {  
        String location = mSearchText.getText().toString();  
        List<Address> addressList = null;  

        if (location != null || !location.equals("")) {  
            Geocoder geocoder = new Geocoder(this);  
            try {  
                addressList = geocoder.getFromLocationName(location, 1);  

            } catch (IOException e) {  
                e.printStackTrace();  
            }  
			try{
            Address address = addressList.get(0);  
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());  
				gmap.clear();
				marker = new MarkerOptions().position(latLng);
				newLatLng = latLng;
				gmap.addMarker(marker);
				gmap.animateCamera(CameraUpdateFactory.newLatLng(latLng));  
			}catch(Exception e){
				Toast.makeText(getApplicationContext(),"No hay lugares",Toast.LENGTH_LONG).show();
			}
			
        }  
    }  









	//instance
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
		if (mapViewBundle == null) {
			mapViewBundle = new Bundle();
			outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
		}

		mapView.onSaveInstanceState(mapViewBundle);
	}













	//cycles
	@Override
	public void onResume()
	{
		super.onResume();
		mapView.onResume();
	}

	@Override
	public void onStart()
	{
		super.onStart();
		mapView.onStart();
	}

	@Override
	public void onStop()
	{
		super.onStop();
		mapView.onStop();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		mapView.onPause();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLowMemory()
	{
		super.onLowMemory();
		mapView.onLowMemory();
	}







}

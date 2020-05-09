package com.mandaditos.administrador;

import android.*;
import android.app.*;
import android.content.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import com.google.android.gms.maps.model.*;
import com.google.firebase.database.*;
import com.mandaditos.administrador.*;
import com.mandaditos.administrador.mUtilities.*;
import com.mandaditos.administrador.models.*;
import java.util.*;

import android.Manifest;
import com.mandaditos.administrador.R;
import android.widget.*;
import android.widget.CompoundButton.*;
public class Home extends AppCompatActivity
{

	private ProgressDialog pDialog;
	private DatabaseReference mDataBase;
	private RequestPermissionHandler mRequestPermissionHandler;
	private ToggleButton switchOrders;
	private TextView CuentaTv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mRequestPermissionHandler = new RequestPermissionHandler();
		mCheckPermission();
		switchOrders = findViewById(R.id.ordenesEntregadasSinEntregar);
		CuentaTv = findViewById(R.id.countmainTextView1);
		switchOrders.setChecked(true);
		
		
		
		
		
		
		
		
		
		
//Switch button
		//TODO optimize to load data once
		switchOrders.setOnCheckedChangeListener(new OnCheckedChangeListener(){

				@Override
				public void onCheckedChanged(CompoundButton p1, boolean p2)
				{
					if(p2==true){
						//dialog 
						pDialog = new ProgressDialog(Home.this);
						pDialog.setMessage("Cargando datos de los servidores..");
						pDialog.show();


						mDataBase = FirebaseDatabase.getInstance().getReference("Ordenes");
						mDataBase.addValueEventListener(new ValueEventListener(){


								@Override
								public void onDataChange(DataSnapshot p1)
								{
									try {
										Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
										Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
										r.play();
									} catch (Exception e) {
										e.printStackTrace();
									}
									pDialog.dismiss();
									if(p1.exists()){

										List<mandaditosModel> ordersList = new ArrayList<mandaditosModel>();
										for (DataSnapshot postSnapshot : p1.getChildren()) {
											double latA = postSnapshot.child("latLngA/latitude").getValue();
											double lngA = postSnapshot.child("latLngA/longitude").getValue();
											double latB = postSnapshot.child("latLngB/latitude").getValue();
											double lngB = postSnapshot.child("latLngB/longitude").getValue();

											mandaditosModel m = new mandaditosModel();
											m.setUserId(postSnapshot.child("userId").getValue().toString());
											m.setUsuario(postSnapshot.child("usuario").getValue().toString());
											m.setPartida(postSnapshot.child("partida").getValue().toString());
											m.setDestino(postSnapshot.child("destino").getValue().toString());
											m.setDistancia(postSnapshot.child("distancia").getValue().toString());
											m.setFecha(postSnapshot.child("fecha").getValue().toString());
											m.setETA(postSnapshot.child("eta").getValue().toString());
											m.setRecogerDineroEn(postSnapshot.child("recogerDineroEn").getValue().toString());
											m.setCosto(postSnapshot.child("costo").getValue().toString());
											m.setEstadoDeOrden(postSnapshot.child("estadoDeOrden").getValue().toString());
											m.setLatLngA(new LatLng(latA,lngA));
											m.setLatLngB(new LatLng(latB,lngB));
											m.setNumeroDeOrden(postSnapshot.getKey().toString());
											m.setDriverAsignado(postSnapshot.child("driverAsignado").getValue().toString());
											if(m.getEstadoDeOrden().toString().toLowerCase().matches("Sin completar".toLowerCase())){
												ordersList.add(m);
											}
										}


										mAdapter adapter = new mAdapter(Home.this,ordersList);
										RecyclerView mRecyclerView = findViewById(R.id.recycler_orders);
										mRecyclerView.setHasFixedSize(true);
										LinearLayoutManager layoutManager = new LinearLayoutManager(Home.this);
										layoutManager.setReverseLayout(false);
										layoutManager.setStackFromEnd(false);
										mRecyclerView.setLayoutManager(layoutManager);
										mRecyclerView.setAdapter(adapter);
										int count = 0;
										if (adapter != null) {
											count = adapter.getItemCount();
										}
										CuentaTv.setText(String.valueOf(count));
									}
									else{}
								}
								@Override
								public void onCancelled(DatabaseError p1){
								}
							});

						
					}
					if(!p2==true){
						//dialog 
						pDialog = new ProgressDialog(Home.this);
						pDialog.setMessage("Cargando datos de los servidores..");
						pDialog.show();

						mDataBase = FirebaseDatabase.getInstance().getReference("Ordenes");
						mDataBase.addValueEventListener(new ValueEventListener(){


								@Override
								public void onDataChange(DataSnapshot p1)
								{
									try {
										Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
										Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
										r.play();
									} catch (Exception e) {
										e.printStackTrace();
									}
									pDialog.dismiss();
									if(p1.exists()){

										List<mandaditosModel> ordersList = new ArrayList<mandaditosModel>();
										for (DataSnapshot postSnapshot : p1.getChildren()) {
											double latA = postSnapshot.child("latLngA/latitude").getValue();
											double lngA = postSnapshot.child("latLngA/longitude").getValue();
											double latB = postSnapshot.child("latLngB/latitude").getValue();
											double lngB = postSnapshot.child("latLngB/longitude").getValue();

											mandaditosModel m = new mandaditosModel();
											m.setUserId(postSnapshot.child("userId").getValue().toString());
											m.setUsuario(postSnapshot.child("usuario").getValue().toString());
											m.setPartida(postSnapshot.child("partida").getValue().toString());
											m.setDestino(postSnapshot.child("destino").getValue().toString());
											m.setDistancia(postSnapshot.child("distancia").getValue().toString());
											m.setFecha(postSnapshot.child("fecha").getValue().toString());
											m.setETA(postSnapshot.child("eta").getValue().toString());
											m.setRecogerDineroEn(postSnapshot.child("recogerDineroEn").getValue().toString());
											m.setCosto(postSnapshot.child("costo").getValue().toString());
											m.setEstadoDeOrden(postSnapshot.child("estadoDeOrden").getValue().toString());
											m.setLatLngA(new LatLng(latA,lngA));
											m.setLatLngB(new LatLng(latB,lngB));
											m.setNumeroDeOrden(postSnapshot.getKey().toString());
											m.setDriverAsignado(postSnapshot.child("driverAsignado").getValue().toString());
											if(m.getEstadoDeOrden().toString().toLowerCase().matches("Completada".toLowerCase())){
												ordersList.add(m);
											}
										}


										mAdapter adapter = new mAdapter(Home.this,ordersList);
										RecyclerView mRecyclerView = findViewById(R.id.recycler_orders);
										mRecyclerView.setHasFixedSize(true);
										LinearLayoutManager layoutManager = new LinearLayoutManager(Home.this);
										layoutManager.setReverseLayout(false);
										layoutManager.setStackFromEnd(false);
										mRecyclerView.setLayoutManager(layoutManager);
										mRecyclerView.setAdapter(adapter);
										int count = 0;
										if (adapter != null) {
											count = adapter.getItemCount();
										}
										CuentaTv.setText(String.valueOf(count));
										}
									else{}
								}
								@Override
								public void onCancelled(DatabaseError p1){
								}
							});

						
					}
				}
			});
		







		

	}
	
	

	@Override
	public void onBackPressed() {
		finishAffinity();
        startActivity(new Intent(Home.this,Home.class));
    } 
	
	//Permissions
	private void mCheckPermission(){
		mRequestPermissionHandler.requestPermission(this, new String[] {
				Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
			}, 123, new RequestPermissionHandler.RequestPermissionListener() {
				@Override
				public void onSuccess() {
				}

				@Override
				public void onFailed() {
				}
			});

	}

	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mRequestPermissionHandler.onRequestPermissionsResult(requestCode, permissions,
															 grantResults);
    }
	
	
}

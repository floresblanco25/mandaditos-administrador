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
import android.view.*;
import android.widget.*;
import com.google.android.gms.maps.model.*;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import com.mandaditos.administrador.*;
import com.mandaditos.administrador.mUtilities.*;
import com.mandaditos.administrador.models.*;
import java.util.*;

import android.Manifest;
import android.app.AlertDialog;
import com.mandaditos.administrador.R;
public class Home extends AppCompatActivity
{

	private ProgressDialog pDialog;
	private DatabaseReference mDataBaseOrders;
	private DatabaseReference mDataBase;
	private RequestPermissionHandler mRequestPermissionHandler;
	private TextView CuentaTv;

	private mAdapter adapter;
	private RecyclerView mRecyclerView;
	FirebaseAuth mFirebaseAuth;
	private ArrayList<String> DriversList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mRequestPermissionHandler = new RequestPermissionHandler();
		mCheckPermission();
		CuentaTv = findViewById(R.id.countmainTextView1);
		mRecyclerView = findViewById(R.id.recycler_orders);
		mFirebaseAuth = FirebaseAuth.getInstance();

		
		
		
		
		
		

//load drivers
		mDataBase = FirebaseDatabase.getInstance().getReference("Drivers");
		mDataBase.addListenerForSingleValueEvent(new ValueEventListener(){


				@Override
				public void onDataChange(DataSnapshot p1)
				{
					if (p1.exists())
					{
						DriversList = new ArrayList<String>();
						for (DataSnapshot postSnapshot : p1.getChildren())
						{
							String driver = postSnapshot.getKey().toString();
							DriversList.add(driver);

						}


					}
					else
					{}
				}
				@Override
				public void onCancelled(DatabaseError p1)
				{
				}
			});



			
			



		//dialog 
		pDialog = new ProgressDialog(Home.this);
		pDialog.setMessage("Cargando datos de los servidores..");
		pDialog.show();







//get all orders

		mDataBaseOrders = FirebaseDatabase.getInstance().getReference("Ordenes");
		mDataBaseOrders.addListenerForSingleValueEvent(new ValueEventListener(){


				@Override
				public void onDataChange(DataSnapshot p1)
				{
//						try {
//							Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//							Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//							r.play();
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
					pDialog.dismiss();
					if (p1.exists())
					{

						List<mandaditosModel> ordersList = new ArrayList<mandaditosModel>();
						for (DataSnapshot postSnapshot : p1.getChildren())
						{
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
							try
							{
								m.setCosto(postSnapshot.child("costo").getValue().toString());
							}
							catch (Exception e)
							{}
							m.setEstadoDeOrden(postSnapshot.child("estadoDeOrden").getValue().toString());
							m.setLatLngA(new LatLng(latA, lngA));
							m.setLatLngB(new LatLng(latB, lngB));
							m.setNumeroDeOrden(postSnapshot.getKey().toString());
							m.setDriverAsignado(postSnapshot.child("driverAsignado").getValue().toString());
							m.setTelefono(postSnapshot.child("telefono").getValue().toString());
							if (m.getEstadoDeOrden().toString().toLowerCase().matches("Sin completar".toLowerCase()))
							{
								if (m.getDriverAsignado().toString().toLowerCase().matches("Sin asignar".toLowerCase()))
								{
									ordersList.add(m);
								}

							}
						}


						adapter = new mAdapter(Home.this, ordersList);
						mRecyclerView.setHasFixedSize(true);
						LinearLayoutManager layoutManager = new LinearLayoutManager(Home.this);
						layoutManager.setReverseLayout(true);
						layoutManager.setStackFromEnd(true);
						mRecyclerView.setLayoutManager(layoutManager);
						mRecyclerView.setAdapter(adapter);
						int count = 0;
						if (adapter != null)
						{
							count = adapter.getItemCount();
						}
						CuentaTv.setText(String.valueOf(count));
					}

					else
					{}
				}
				@Override
				public void onCancelled(DatabaseError p1)
				{
				}
			});







			
//add more

	}






	
	
	


//Añadir
	public void addOrder(View v)
	{
		FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
		String UserId = mFirebaseUser.getUid().toString();
		FirebaseDatabase.getInstance().getReference("Ordenes")
			.push()
			.setValue(new mandaditosModel(UserId, "", "", "", "", "", 
										  "", "", "", "Sin completar", new LatLng(13.0484747474, -13.04948474), new LatLng(13.0484747474, -13.04948474), "Sin asignar",""));
	}
	
	
	
	
	
	
	
	
	
	
	
//sin comoletar boton
	public void mostrarSinCompletar(View v)
	{
		//dialog 
		pDialog = new ProgressDialog(Home.this);
		pDialog.setMessage("Cargando datos de los servidores..");
		pDialog.show();


		mDataBaseOrders = FirebaseDatabase.getInstance().getReference("Ordenes");
		mDataBaseOrders.addListenerForSingleValueEvent(new ValueEventListener(){


				@Override
				public void onDataChange(DataSnapshot p1)
				{
					pDialog.dismiss();
					if (p1.exists())
					{

						List<mandaditosModel> ordersList = new ArrayList<mandaditosModel>();
						for (DataSnapshot postSnapshot : p1.getChildren())
						{
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
							try
							{
								m.setCosto(postSnapshot.child("costo").getValue().toString());
							}
							catch (Exception e)
							{}
							m.setEstadoDeOrden(postSnapshot.child("estadoDeOrden").getValue().toString());
							m.setLatLngA(new LatLng(latA, lngA));
							m.setLatLngB(new LatLng(latB, lngB));
							m.setNumeroDeOrden(postSnapshot.getKey().toString());
							m.setDriverAsignado(postSnapshot.child("driverAsignado").getValue().toString());
							m.setTelefono(postSnapshot.child("telefono").getValue().toString());
							if (m.getEstadoDeOrden().toString().toLowerCase().matches("Sin completar".toLowerCase()))
							{
								if (!(m.getDriverAsignado().toString().toLowerCase().matches("Sin asignar".toLowerCase())))
								{
									ordersList.add(m);
								}
							}
						}


						adapter = new mAdapter(Home.this, ordersList);
						mRecyclerView.setHasFixedSize(true);
						LinearLayoutManager layoutManager = new LinearLayoutManager(Home.this);
						layoutManager.setReverseLayout(true);
						layoutManager.setStackFromEnd(true);
						mRecyclerView.setLayoutManager(layoutManager);
						mRecyclerView.setAdapter(adapter);
						int count = 0;
						if (adapter != null)
						{
							count = adapter.getItemCount();
						}
						CuentaTv.setText(String.valueOf(count));
					}

					else
					{}
				}
				@Override
				public void onCancelled(DatabaseError p1)
				{
				}
			});


	}






	
	
//comoletadas boton
	public void mostrarCompletas(View v)
	{
		//dialog 
		pDialog = new ProgressDialog(Home.this);
		pDialog.setMessage("Cargando datos de los servidores..");
		pDialog.show();

		mDataBaseOrders = FirebaseDatabase.getInstance().getReference("Ordenes");
		mDataBaseOrders.addListenerForSingleValueEvent(new ValueEventListener(){


				@Override
				public void onDataChange(DataSnapshot p1)
				{
					pDialog.dismiss();
					if (p1.exists())
					{

						List<mandaditosModel> ordersList = new ArrayList<mandaditosModel>();
						for (DataSnapshot postSnapshot : p1.getChildren())
						{
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
							m.setLatLngA(new LatLng(latA, lngA));
							m.setLatLngB(new LatLng(latB, lngB));
							m.setNumeroDeOrden(postSnapshot.getKey().toString());
							m.setDriverAsignado(postSnapshot.child("driverAsignado").getValue().toString());
							m.setTelefono(postSnapshot.child("telefono").getValue().toString());
							if (m.getEstadoDeOrden().toString().toLowerCase().matches("Completada".toLowerCase()))
							{
								ordersList.add(m);
							}
						}


						mAdapter adapter = new mAdapter(Home.this, ordersList);
						mRecyclerView.setHasFixedSize(true);
						LinearLayoutManager layoutManager = new LinearLayoutManager(Home.this);
						layoutManager.setReverseLayout(true);
						layoutManager.setStackFromEnd(true);
						mRecyclerView.setLayoutManager(layoutManager);
						mRecyclerView.setAdapter(adapter);
						int count = 0;
						if (adapter != null)
						{
							count = adapter.getItemCount();
						}
						CuentaTv.setText(String.valueOf(count));
					}
					else
					{}
				}
				@Override
				public void onCancelled(DatabaseError p1)
				{
				}
			});


	}













//sin asignar boton
	public void mostrarSinAsignar(View v)
	{
		//dialog 
		pDialog = new ProgressDialog(Home.this);
		pDialog.setMessage("Cargando datos de los servidores..");
		pDialog.show();


		mDataBaseOrders = FirebaseDatabase.getInstance().getReference("Ordenes");
		mDataBaseOrders.addListenerForSingleValueEvent(new ValueEventListener(){


				@Override
				public void onDataChange(DataSnapshot p1)
				{
					pDialog.dismiss();
					if (p1.exists())
					{

						List<mandaditosModel> ordersList = new ArrayList<mandaditosModel>();
						for (DataSnapshot postSnapshot : p1.getChildren())
						{
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
							try
							{
								m.setCosto(postSnapshot.child("costo").getValue().toString());
							}
							catch (Exception e)
							{}
							m.setEstadoDeOrden(postSnapshot.child("estadoDeOrden").getValue().toString());
							m.setLatLngA(new LatLng(latA, lngA));
							m.setLatLngB(new LatLng(latB, lngB));
							m.setNumeroDeOrden(postSnapshot.getKey().toString());
							m.setDriverAsignado(postSnapshot.child("driverAsignado").getValue().toString());
							m.setTelefono(postSnapshot.child("telefono").getValue().toString());
							if (m.getEstadoDeOrden().toString().toLowerCase().matches("Sin completar".toLowerCase()))
							{
								if (m.getDriverAsignado().toString().toLowerCase().matches("Sin asignar".toLowerCase()))
								{
									ordersList.add(m);
								}

							}
						}


						adapter = new mAdapter(Home.this, ordersList);
						mRecyclerView.setHasFixedSize(true);
						LinearLayoutManager layoutManager = new LinearLayoutManager(Home.this);
						layoutManager.setReverseLayout(true);
						layoutManager.setStackFromEnd(true);
						mRecyclerView.setLayoutManager(layoutManager);
						mRecyclerView.setAdapter(adapter);
						int count = 0;
						if (adapter != null)
						{
							count = adapter.getItemCount();
						}
						CuentaTv.setText(String.valueOf(count));
					}

					else
					{}
				}
				@Override
				public void onCancelled(DatabaseError p1)
				{
				}
			});
	}












	


//Ver drivers boton
	public void verDrivers(View v)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
		builder.setTitle("Elige un Driver");
		String[] drivers = GetStringArray(DriversList);
		builder.setItems(drivers, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					
					
					
					
					
					
					final String selected = DriversList.get(which);
					//aqui obtenemos el nombre del usuario
					DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Drivers/" + selected + "/Perfil").child("nombre");
					ref.addListenerForSingleValueEvent(new ValueEventListener() {
							@Override
							public void onDataChange(DataSnapshot dataSnapshot)
							{
								String t = dataSnapshot.getValue(String.class);
								mDataBaseOrders.addListenerForSingleValueEvent(new ValueEventListener(){


										@Override
										public void onDataChange(DataSnapshot p1)
										{
											if (p1.exists())
											{

												List<mandaditosModel> ordersList = new ArrayList<mandaditosModel>();
												for (DataSnapshot postSnapshot : p1.getChildren())
												{
													
													double latA = postSnapshot.child("latLngA/latitude").getValue();
													double lngA = postSnapshot.child("latLngA/longitude").getValue();
													double latB = postSnapshot.child("latLngB/latitude").getValue();
													double lngB = postSnapshot.child("latLngB/longitude").getValue();

													mandaditosModel model = new mandaditosModel();
													model.setNumeroDeOrden(postSnapshot.getKey().toString());
													model.setUserId(postSnapshot.child("userId").getValue().toString());
													model.setUsuario(postSnapshot.child("usuario").getValue().toString());
													model.setPartida(postSnapshot.child("partida").getValue().toString());
													model.setDestino(postSnapshot.child("destino").getValue().toString());
													model.setDistancia(postSnapshot.child("distancia").getValue().toString());
													model.setFecha(postSnapshot.child("fecha").getValue().toString());
													model.setETA(postSnapshot.child("eta").getValue().toString());
													model.setRecogerDineroEn(postSnapshot.child("recogerDineroEn").getValue().toString());
													model.setCosto(postSnapshot.child("costo").getValue().toString());
													model.setEstadoDeOrden(postSnapshot.child("estadoDeOrden").getValue().toString());
													model.setLatLngA(new LatLng(latA, lngA));
													model.setLatLngB(new LatLng(latB, lngB));
													model.setNumeroDeOrden(postSnapshot.getKey().toString());
													model.setDriverAsignado(postSnapshot.child("driverAsignado").getValue().toString());
													model.setDriverAsignado(postSnapshot.child("driverAsignado").getValue().toString());
													model.setTelefono(postSnapshot.child("telefono").getValue().toString());
													if (model.getDriverAsignado().toString().matches(selected))
													{
														ordersList.add(model);
													}
												}


												adapter = new mAdapter(Home.this, ordersList);
												mRecyclerView.setHasFixedSize(true);
												LinearLayoutManager layoutManager = new LinearLayoutManager(Home.this);
												layoutManager.setReverseLayout(true);
												layoutManager.setStackFromEnd(true);
												mRecyclerView.setLayoutManager(layoutManager);
												mRecyclerView.setAdapter(adapter);
												int count = 0;
												if (adapter != null)
												{
													count = adapter.getItemCount();
												}
												CuentaTv.setText(String.valueOf(count));
											}
											else
											{}
										}
										@Override
										public void onCancelled(DatabaseError p1)
										{
										}
									});

							}

							@Override
							public void onCancelled(DatabaseError databaseError)
							{

							}
						});



				}
			});

		AlertDialog dialog = builder.create();
		dialog.show();
    } 
















	
	@Override
	public void onBackPressed()
	{
		finishAffinity();
        startActivity(new Intent(Home.this, Home.class));
    } 







	//Permissions
	private void mCheckPermission()
	{
		mRequestPermissionHandler.requestPermission(this, new String[] {
				Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
			}, 123, new RequestPermissionHandler.RequestPermissionListener() {
				@Override
				public void onSuccess()
				{
				}

				@Override
				public void onFailed()
				{
				}
			});

	}






	

	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults)
	{
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mRequestPermissionHandler.onRequestPermissionsResult(requestCode, permissions,
															 grantResults);
    }








	
//Get stringarray
	public static String[] GetStringArray(ArrayList<String> arr) 
    { 
        String str[] = new String[arr.size()]; 
        for (int j = 0; j < arr.size(); j++)
		{ 
            str[j] = arr.get(j); 
        } 

        return str; 

    } 



}

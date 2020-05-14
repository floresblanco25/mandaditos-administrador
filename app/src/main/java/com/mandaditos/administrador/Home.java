package com.mandaditos.administrador;

import android.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.view.View.*;
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
	private TextView CuentaTv,totalAliquidar;

	private mAdapter adapter;
	private RecyclerView mRecyclerView;
	FirebaseAuth mFirebaseAuth;
	private ArrayList<String> DriversList;
	private ArrayList<String> namesList;
	private String Empresa = "Pon aqui tu empresa";
	private String Direccion = "Tu direccion de empresa";
	
	
	


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
		totalAliquidar = findViewById(R.id.totalAliquidar);

		
		
		
		
		
		
		
		

//load drivers list for picker
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
							mDataBase = FirebaseDatabase.getInstance().getReference("Drivers/"+driver);
							mDataBase.addListenerForSingleValueEvent(new ValueEventListener(){

									@Override
									public void onDataChange(DataSnapshot p1)
									{
//										Toast.makeText(Home.this,p1.toString(),500).show();
//										String name = p1.child("nombre").getValue().toString();
//										namesList.add(name);
									}

									@Override
									public void onCancelled(DatabaseError p1)
									{
										// TODO: Implement this method
									}
								});
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
		mDataBase = FirebaseDatabase.getInstance().getReference("Drivers");
		//Obtenemos nombre por uid
			
			
			
			
			
			



			
			



		//dialog 
		pDialog = new ProgressDialog(Home.this);
		pDialog.setMessage("Cargando datos de los servidores..");
		pDialog.show();







//get all orders

		mDataBaseOrders = FirebaseDatabase.getInstance().getReference("Ordenes");
		mDataBaseOrders.addListenerForSingleValueEvent(new ValueEventListener(){

				private Float numbers;


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
						List<CostoPorOrden> items = new ArrayList<CostoPorOrden>();
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
							
							m.setCostoDelProducto(postSnapshot.child("costoDelProducto").getValue().toString());
							m.setCostoDelEnvio(postSnapshot.child("costoDelEnvio").getValue().toString());
								m.setEmpresa(postSnapshot.child("empresa").getValue().toString());
								m.setDireccionEmpresa(postSnapshot.child("direccionEmpresa").getValue().toString());
								m.setInstruccionesDeLlegada(postSnapshot.child("instruccionesDeLlegada").getValue().toString());
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
									CostoPorOrden precioModel = new CostoPorOrden();
									numbers = Float.valueOf(postSnapshot.child("costoDelProducto").getValue().toString());
									precioModel.setPrecioDeOrden(numbers);
									items.add(precioModel);
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
						totalAliquidar.setText(String.valueOf(grandTotal(items)));
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
	
	class MyAdapter extends BaseAdapter
	{

		private CharSequence[] titles;

		private CharSequence[] susbtitles;
		
		private Context mContext;
		
		MyAdapter(Context mContext, String[] titles, String[] subtitles)
		{
			this.mContext = mContext;
			this.titles = titles;
			this.susbtitles=subtitles;
		}
		
		

		@Override
		public int getCount() 
		{
			return titles.length;
		}

		@Override
		public Object getItem(int position) 
		{
			//this isn't great
			return titles[position];
		}

		@Override
		public long getItemId(int position) 
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			if(convertView == null)
			{
				convertView = getLayoutInflater().inflate(R.layout.drivers_list_row, null);
			}

			((TextView)convertView.findViewById(R.id.text1)).setText( titles[position]);
			((TextView)convertView.findViewById(R.id.text2)).setText( susbtitles[position]);

			return convertView;
		}

		}





	
	
	


//Añadir
	public void addOrder(View v)
	{
		FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
		String UserId = mFirebaseUser.getUid().toString();
		FirebaseDatabase.getInstance().getReference("Ordenes")
			.push()
			.setValue(new mandaditosModel("0",Empresa,Direccion,"",UserId, "", "", "", "", "", 
										  "", "", "0", "Sin completar", new LatLng(13.67694, -89.27972), new LatLng(13.67694, -89.27972), "Sin asignar",""));
		finishAffinity();
		startActivity(new Intent(Home.this, Home.class));
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

						List<CostoPorOrden> items = new ArrayList<CostoPorOrden>();
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
								m.setCostoDelProducto(postSnapshot.child("costoDelProducto").getValue().toString());
							m.setCostoDelEnvio(postSnapshot.child("costoDelEnvio").getValue().toString());
							m.setEmpresa(postSnapshot.child("empresa").getValue().toString());
							m.setDireccionEmpresa(postSnapshot.child("direccionEmpresa").getValue().toString());
							m.setInstruccionesDeLlegada(postSnapshot.child("instruccionesDeLlegada").getValue().toString());
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
									CostoPorOrden precioModel = new CostoPorOrden();
									float numbers = Float.valueOf(postSnapshot.child("costoDelProducto").getValue().toString());
									precioModel.setPrecioDeOrden(numbers);
									items.add(precioModel);
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
						totalAliquidar.setText(String.valueOf(grandTotal(items)));
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
						List<CostoPorOrden> items = new ArrayList<CostoPorOrden>();
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
							m.setCostoDelProducto(postSnapshot.child("costoDelProducto").getValue().toString());
							m.setCostoDelEnvio(postSnapshot.child("costoDelEnvio").getValue().toString());
							m.setEmpresa(postSnapshot.child("empresa").getValue().toString());
							m.setDireccionEmpresa(postSnapshot.child("direccionEmpresa").getValue().toString());
							m.setInstruccionesDeLlegada(postSnapshot.child("instruccionesDeLlegada").getValue().toString());
							m.setEstadoDeOrden(postSnapshot.child("estadoDeOrden").getValue().toString());
							m.setLatLngA(new LatLng(latA, lngA));
							m.setLatLngB(new LatLng(latB, lngB));
							m.setNumeroDeOrden(postSnapshot.getKey().toString());
							m.setDriverAsignado(postSnapshot.child("driverAsignado").getValue().toString());
							m.setTelefono(postSnapshot.child("telefono").getValue().toString());
							if (m.getEstadoDeOrden().toString().toLowerCase().matches("Completada".toLowerCase()))
							{
								ordersList.add(m);
								CostoPorOrden precioModel = new CostoPorOrden();
								float numbers = Float.valueOf(postSnapshot.child("costoDelProducto").getValue().toString());
								precioModel.setPrecioDeOrden(numbers);
								items.add(precioModel);
							}
						}


						mAdapter adapter = new mAdapter(Home.this, ordersList);
						mRecyclerView.setHasFixedSize(true);
						LinearLayoutManager layoutManager = new LinearLayoutManager(Home.this);
						layoutManager.setReverseLayout(true);
						layoutManager.setStackFromEnd(true);
						mRecyclerView.setLayoutManager(layoutManager);
						mRecyclerView.setAdapter(adapter);
						totalAliquidar.setText(String.valueOf(grandTotal(items)));
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
						List<CostoPorOrden> items = new ArrayList<CostoPorOrden>();
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
								m.setCostoDelProducto(postSnapshot.child("costoDelProducto").getValue().toString());
							m.setCostoDelEnvio(postSnapshot.child("costoDelEnvio").getValue().toString());
							m.setEmpresa(postSnapshot.child("empresa").getValue().toString());
							m.setDireccionEmpresa(postSnapshot.child("direccionEmpresa").getValue().toString());
							m.setInstruccionesDeLlegada(postSnapshot.child("instruccionesDeLlegada").getValue().toString());
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
									CostoPorOrden precioModel = new CostoPorOrden();
									float numbers = Float.valueOf(postSnapshot.child("costoDelProducto").getValue().toString());
									precioModel.setPrecioDeOrden(numbers);
									items.add(precioModel);
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
						totalAliquidar.setText(String.valueOf(grandTotal(items)));
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
	public void verDrivers(View v){
		String[] drivers = GetStringArray(DriversList);
//		String[] names = GetStringArray(namesList);
//		solo necesuto arreglar la lista de nlmbres 
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(new MyAdapter(Home.this,drivers,drivers), null);
        builder.setTitle("Asignar A?");
		builder.setItems(drivers, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					final String selectedUid = DriversList.get(p2);
					//aqui obtenemos el nombre del usuario
					DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Drivers/" + selectedUid + "/Perfil").child("nombre");
					ref.addListenerForSingleValueEvent(new ValueEventListener() {
							@Override
							public void onDataChange(DataSnapshot dataSnapshot)
							{
								String nombre = dataSnapshot.getValue(String.class);
								mDataBaseOrders.addListenerForSingleValueEvent(new ValueEventListener(){


										@Override
										public void onDataChange(DataSnapshot p1)
										{
											if (p1.exists())
											{

												List<mandaditosModel> ordersList = new ArrayList<mandaditosModel>();
												List<CostoPorOrden> items = new ArrayList<CostoPorOrden>();
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
													model.setCostoDelProducto(postSnapshot.child("costoDelProducto").getValue().toString());
													model.setCostoDelEnvio(postSnapshot.child("costoDelEnvio").getValue().toString());
													model.setEmpresa(postSnapshot.child("empresa").getValue().toString());
													model.setDireccionEmpresa(postSnapshot.child("direccionEmpresa").getValue().toString());
													model.setInstruccionesDeLlegada(postSnapshot.child("instruccionesDeLlegada").getValue().toString());
													model.setEstadoDeOrden(postSnapshot.child("estadoDeOrden").getValue().toString());
													model.setLatLngA(new LatLng(latA, lngA));
													model.setLatLngB(new LatLng(latB, lngB));
													model.setNumeroDeOrden(postSnapshot.getKey().toString());
													model.setDriverAsignado(postSnapshot.child("driverAsignado").getValue().toString());
													model.setDriverUid(postSnapshot.child("driverAsignado").getValue().toString());
													model.setTelefono(postSnapshot.child("telefono").getValue().toString());
													if (model.getDriverAsignado().toString().matches(selectedUid))
													{
														ordersList.add(model);
													}
														if(model.getDriverUid().toString().matches(selectedUid)){
															CostoPorOrden precioModel = new CostoPorOrden();
															float numbers = Float.valueOf(postSnapshot.child("costoDelProducto").getValue().toString());
															precioModel.setPrecioDeOrden(numbers);
															items.add(precioModel);
														}
												}


												adapter = new mAdapter(Home.this, ordersList);
												mRecyclerView.setHasFixedSize(true);
												LinearLayoutManager layoutManager = new LinearLayoutManager(Home.this);
												layoutManager.setReverseLayout(true);
												layoutManager.setStackFromEnd(true);
												mRecyclerView.setLayoutManager(layoutManager);
												mRecyclerView.setAdapter(adapter);
												totalAliquidar.setText(String.valueOf(grandTotal(items)));
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
					// TODO: Implement this method
				}
			});


        builder.show();
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
//total calc
	private float grandTotal(List<CostoPorOrden> items){
		float totalPrice = 0;
		for(int i = 0 ; i < items.size(); i++) {
			totalPrice += items.get(i).getPrecioDeOrden(); 
		}

		return totalPrice;
		}
	
		
		
		
		
		
		
		
		
		
		
		
		
		
//refresh
		public void refresh(View v){
			finishAffinity();
			startActivity(new Intent(Home.this, Home.class));
		}
	 
















	
	@Override
	public void onBackPressed()
	{
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setTitle("Salir");
		dialog.setMessage("¿Seguro que quieres salir?" );
		dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					dialog.dismiss();
				}
			});
		dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Salir", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					finishAffinity();
				}
			});
		dialog.show();
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

package com.mandaditos.administrador;

import android.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.text.*;
import android.util.*;
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
import android.support.v7.widget.PopupMenu;
import com.mandaditos.administrador.R;
import java.io.*;
public class Home extends AppCompatActivity
{

	private ProgressDialog pDialog;
	private DatabaseReference mDataBaseOrders;
	private DatabaseReference mDataBase;
	private RequestPermissionHandler mRequestPermissionHandler;
	private TextView contarOrdenes,totalAliquidar,pagoDriverTv,netTv;
	private mAdapter adapter;
	private RecyclerView mRecyclerView;
	FirebaseAuth mFirebaseAuth;
	private ArrayList<String> DriversListUid,DriversListNames;
	private String Empresa;
	private Button entregadas,sinentregar,nuevas,drivers; 
	private EditText buscarEmpresaEd,buscarPersona,buscarDestino,liquidadoEd;
	private String uId;
	String datosFirebase = "";
	String datosFirebaseCopia = "";




	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mRequestPermissionHandler = new RequestPermissionHandler();
		mCheckPermission();
		contarOrdenes = findViewById(R.id.countmainTextView1);
		mRecyclerView = findViewById(R.id.recycler_orders);
		mFirebaseAuth = FirebaseAuth.getInstance();
		totalAliquidar = findViewById(R.id.totalAliquidar);
		pagoDriverTv = findViewById(R.id.pagoDrivermainTextView1);
		
		
		entregadas = findViewById(R.id.entregadasmainButton1);
		sinentregar = findViewById(R.id.sinentregarmainButton1);
		drivers = findViewById(R.id.driversmainButton1);
		nuevas = findViewById(R.id.nuevasmainButton1);
		buscarEmpresaEd = findViewById(R.id.searchEdmainEditText1);
		buscarPersona = findViewById(R.id.BuscarPersonamainEditText1);
		buscarDestino = findViewById(R.id.lugarmainEditText1);
		netTv = findViewById(R.id.network);
		liquidadoEd = findViewById(R.id.liquidadoEd);
		
		
		
		
		
		

		
		
		try{
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); 
		}catch(Exception e){}
		
		
		
		
		
//servicio notificaciones
		startService(new Intent(this, com.mandaditos.administrador.mUtilities.ChildEventListener.class));
		
		
		
		
		
		



//red
DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
		connectedRef.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot snapshot) {
					boolean connected = snapshot.getValue(Boolean.class);
					if (connected) {
						netTv.setVisibility(View.GONE);
					} else {
						netTv.setVisibility(View.VISIBLE);
					}
				}

				@Override
				public void onCancelled(DatabaseError error) {
				}
			});

			
			
		
		
		
		
		
		
		
		
		
		getWindow().setSoftInputMode(
			WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
		);
		
		
		
		
		
		
		
		
		

//aqui obtenemos el uid
		mFirebaseAuth = FirebaseAuth.getInstance();
		FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
		uId = mFirebaseUser.getUid().toString();


//aqui obtenemos el nombre del usuario
		mFirebaseAuth = FirebaseAuth.getInstance();
		String userId = mFirebaseUser.getUid().toString();
		DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Usuarios/" + userId + "/Perfil").child("nombre");
		ref.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					String t = dataSnapshot.getValue(String.class);
					setNombreUsuario(t);

				}
				private void setNombreUsuario(String t)
				{

					String usuario = t;
					try{
						int index = usuario.indexOf(' ');
						usuario = usuario.substring(0, index);
					}catch(Exception e){Log.e("Couldent split name",e.toString());}
					
					Empresa=usuario;
					


				}

				@Override
				public void onCancelled(DatabaseError databaseError) {

				}
			});


			
			
			
			
			
			
			
			
//Hide views for cliemt
		if(!userId.toString().matches("bTn7vklJZGhVYa2tnPlDZKStwEi2")){
			entregadas.setVisibility(View.GONE);
			sinentregar.setVisibility(View.GONE);
			drivers.setVisibility(View.GONE);
			nuevas.setVisibility(View.GONE);
		}







//load drivers list for picker
		mDataBase = FirebaseDatabase.getInstance().getReference("Drivers");
		mDataBase.addListenerForSingleValueEvent(new ValueEventListener(){


				@Override
				public void onDataChange(DataSnapshot p1)
				{
					if (p1.exists())
					{
						DriversListUid = new ArrayList<String>();
						DriversListNames = new ArrayList<String>();
						for (DataSnapshot postSnapshot : p1.getChildren())
						{
							String driver = postSnapshot.getKey().toString();
							String DriverName = postSnapshot.child("Perfil/nombre").getValue().toString();
							DriversListUid.add(driver);
							DriversListNames.add(DriverName.toString());

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

				private Float costosDeOrden;


				@Override
				public void onDataChange(DataSnapshot p1)
				{
					pDialog.dismiss();
					if (p1.exists())
					{
						List<CostoPorOrden> costoPorOrdenList = new ArrayList<CostoPorOrden>();
						List<mandaditosModel> ordersList = new ArrayList<mandaditosModel>();
						for (DataSnapshot postSnapshot : p1.getChildren())
						{
							double latA = postSnapshot.child("latLngA/latitude").getValue();
							double lngA = postSnapshot.child("latLngA/longitude").getValue();
							double latB = postSnapshot.child("latLngB/latitude").getValue();
							double lngB = postSnapshot.child("latLngB/longitude").getValue();

							mandaditosModel m = new mandaditosModel();
							m.setEmpresaUserId(postSnapshot.child("empresaUserId").getValue().toString());
							m.setUsuario(postSnapshot.child("usuario").getValue().toString());
							m.setClienteDeDestino(postSnapshot.child("clienteDeDestino").getValue().toString());
							m.setDireccionDeDestino(postSnapshot.child("direccionDeDestino").getValue().toString());

							m.setCostoDelProducto(postSnapshot.child("costoDelProducto").getValue().toString());
							m.setCostoDelEnvio(postSnapshot.child("costoDelEnvio").getValue().toString());
							m.setEmpresaDePartida(postSnapshot.child("empresaDePartida").getValue().toString());
							m.setDireccionEmpresaDePartida(postSnapshot.child("direccionEmpresaDePartida").getValue().toString());
							m.setInstrucciones(postSnapshot.child("instrucciones").getValue().toString());
							m.setEstadoDeOrden(postSnapshot.child("estadoDeOrden").getValue().toString());
							m.setLatLngA(new LatLng(latA, lngA));
							m.setLatLngB(new LatLng(latB, lngB));
							m.setNumeroDeOrden(postSnapshot.getKey().toString());
							m.setDriverAsignado(postSnapshot.child("driverAsignado").getValue().toString());
							m.setTelefonoDeClienteDeDestino(postSnapshot.child("telefonoDeClienteDeDestino").getValue().toString());
							m.setCostoOrden(postSnapshot.child("costoOrden").getValue().toString());
							if (m.getEstadoDeOrden().toString().toLowerCase().matches("Sin completar".toLowerCase()))
							{
								if (m.getDriverAsignado().toString().toLowerCase().matches("Sin asignar".toLowerCase()))
								{
									ordersList.add(m);
									CostoPorOrden precioModel = new CostoPorOrden();
									costosDeOrden = Float.valueOf(postSnapshot.child("costoOrden").getValue().toString());
									precioModel.setPrecioDeOrden(costosDeOrden);
									costoPorOrdenList.add(precioModel);
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
						totalAliquidar.setText(String.valueOf(sumarItems(costoPorOrdenList)));
						int count = 0;
						if (adapter != null)
						{
							count = adapter.getItemCount();
						}
						contarOrdenes.setText(String.valueOf(count));
					}

					else
					{}
				}
				@Override
				public void onCancelled(DatabaseError p1)
				{
				}
			});
			
			
			
			
			
			
			
			
			
//Search edittext
		buscarEmpresaEd.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void afterTextChanged(Editable editable) {
					filterEmpresa(editable.toString());
				}
			});
		buscarPersona.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void afterTextChanged(Editable editable) {
					filterPersonas(editable.toString());
				}
			});
		buscarDestino.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void afterTextChanged(Editable editable) {
					filterLugar(editable.toString());
				}
			});
		







//add more

	}

	
	
	
	
	
	
	
	
//Show more
	public void showMore(View v){
		PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
				public boolean onMenuItemClick(MenuItem item) {
					switch(item.getItemId()){
						case R.id.borrcompl:
								final AlertDialog dialog = new AlertDialog.Builder(Home.this).create();
								dialog.setTitle("Borrar todas las ordenes completadas!");
								dialog.setMessage("Borraras todas las completadas, ingresa contraseña");
							final EditText input = new EditText(Home.this);  
							LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT,
								LinearLayout.LayoutParams.MATCH_PARENT);
							input.setLayoutParams(lp);
							dialog.setView(input);
								dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener(){

										@Override
										public void onClick(DialogInterface p1, int p2)
										{
											dialog.dismiss();
										}
									});
								dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Eliminar", new DialogInterface.OnClickListener(){

										@Override
										public void onClick(DialogInterface p1, int p2)
										{
											if(input.getText().toString().matches("15151515")){
											DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
											Query applesQuery = ref.child("Ordenes").orderByChild("estadoDeOrden").equalTo("Completada");

											applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
													@Override
													public void onDataChange(DataSnapshot dataSnapshot)
													{
														for (DataSnapshot appleSnapshot: dataSnapshot.getChildren())
														{
															appleSnapshot.getRef().removeValue();

														}
													}

													@Override
													public void onCancelled(DatabaseError databaseError)
													{
														Toast.makeText(Home.this, databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
													}
												});
											}else{
												Toast.makeText(Home.this,"Password incorrecto",500).show();
											}
										}
									});
								dialog.show();

							
					}
					return true;
				}
			});

        popup.show();
	}









//Añadir
	public void addOrder(View v)
	{
		FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
		String UserId = mFirebaseUser.getUid().toString();
		FirebaseDatabase.getInstance().getReference("Ordenes")
			.push()
			.setValue(new mandaditosModel("0", "", "", "", UserId, "", "", "", "0", "Sin completar", new LatLng(13.67694, -89.27972), new LatLng(13.67694, -89.27972), "Sin asignar", "", "0","0"));
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
							m.setEmpresaUserId(postSnapshot.child("empresaUserId").getValue().toString());
							m.setUsuario(postSnapshot.child("usuario").getValue().toString());
							m.setClienteDeDestino(postSnapshot.child("clienteDeDestino").getValue().toString());
							m.setDireccionDeDestino(postSnapshot.child("direccionDeDestino").getValue().toString());
							m.setCostoDelProducto(postSnapshot.child("costoDelProducto").getValue().toString());
							m.setCostoDelEnvio(postSnapshot.child("costoDelEnvio").getValue().toString());
							m.setEmpresaDePartida(postSnapshot.child("empresaDePartida").getValue().toString());
							m.setDireccionEmpresaDePartida(postSnapshot.child("direccionEmpresaDePartida").getValue().toString());
							m.setInstrucciones(postSnapshot.child("instrucciones").getValue().toString());
							m.setEstadoDeOrden(postSnapshot.child("estadoDeOrden").getValue().toString());
							m.setLatLngA(new LatLng(latA, lngA));
							m.setLatLngB(new LatLng(latB, lngB));
							m.setNumeroDeOrden(postSnapshot.getKey().toString());
							m.setCostoOrden(postSnapshot.child("costoOrden").getValue().toString());
							m.setDriverAsignado(postSnapshot.child("driverAsignado").getValue().toString());
							m.setTelefonoDeClienteDeDestino(postSnapshot.child("telefonoDeClienteDeDestino").getValue().toString());
							if (m.getEstadoDeOrden().toString().toLowerCase().matches("Sin completar".toLowerCase()))
							{
								if (!(m.getDriverAsignado().toString().toLowerCase().matches("Sin asignar".toLowerCase())))
								{
									ordersList.add(m);
									CostoPorOrden precioModel = new CostoPorOrden();
									float numbers = Float.valueOf(postSnapshot.child("costoOrden").getValue().toString());
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
						totalAliquidar.setText(String.valueOf(sumarItems(items)));
						int count = 0;
						if (adapter != null)
						{
							count = adapter.getItemCount();
						}
						contarOrdenes.setText(String.valueOf(count));
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
							m.setEmpresaUserId(postSnapshot.child("empresaUserId").getValue().toString());
							m.setUsuario(postSnapshot.child("usuario").getValue().toString());
							m.setClienteDeDestino(postSnapshot.child("clienteDeDestino").getValue().toString());
							m.setDireccionDeDestino(postSnapshot.child("direccionDeDestino").getValue().toString());
							m.setCostoDelProducto(postSnapshot.child("costoDelProducto").getValue().toString());
							m.setCostoDelEnvio(postSnapshot.child("costoDelEnvio").getValue().toString());
							m.setEmpresaDePartida(postSnapshot.child("empresaDePartida").getValue().toString());
							m.setDireccionEmpresaDePartida(postSnapshot.child("direccionEmpresaDePartida").getValue().toString());
							m.setInstrucciones(postSnapshot.child("instrucciones").getValue().toString());
							m.setEstadoDeOrden(postSnapshot.child("estadoDeOrden").getValue().toString());
							m.setLatLngA(new LatLng(latA, lngA));
							m.setLatLngB(new LatLng(latB, lngB));
							m.setNumeroDeOrden(postSnapshot.getKey().toString());
							m.setDriverAsignado(postSnapshot.child("driverAsignado").getValue().toString());
							m.setTelefonoDeClienteDeDestino(postSnapshot.child("telefonoDeClienteDeDestino").getValue().toString());
							m.setCostoOrden(postSnapshot.child("costoOrden").getValue().toString());
							if (m.getEstadoDeOrden().toString().toLowerCase().matches("Completada".toLowerCase()))
							{
								ordersList.add(m);
								CostoPorOrden precioModel = new CostoPorOrden();
								float numbers = Float.valueOf(postSnapshot.child("costoOrden").getValue().toString());
								precioModel.setPrecioDeOrden(numbers);
								items.add(precioModel);
							}
						}

//edited
						adapter = new mAdapter(Home.this, ordersList);
						mRecyclerView.setHasFixedSize(true);
						LinearLayoutManager layoutManager = new LinearLayoutManager(Home.this);
						layoutManager.setReverseLayout(false);
						layoutManager.setStackFromEnd(true);
						mRecyclerView.setLayoutManager(layoutManager);
						mRecyclerView.setAdapter(adapter);
						totalAliquidar.setText(String.valueOf(sumarItems(items)));
						int count = 0;
						if (adapter != null)
						{
							count = adapter.getItemCount();
						}
						contarOrdenes.setText(String.valueOf(count));
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
							m.setEmpresaUserId(postSnapshot.child("empresaUserId").getValue().toString());
							m.setUsuario(postSnapshot.child("usuario").getValue().toString());
							m.setClienteDeDestino(postSnapshot.child("clienteDeDestino").getValue().toString());
							m.setDireccionDeDestino(postSnapshot.child("direccionDeDestino").getValue().toString());
							m.setCostoDelProducto(postSnapshot.child("costoDelProducto").getValue().toString());
							m.setCostoDelEnvio(postSnapshot.child("costoDelEnvio").getValue().toString());
							m.setEmpresaDePartida(postSnapshot.child("empresaDePartida").getValue().toString());
							m.setDireccionEmpresaDePartida(postSnapshot.child("direccionEmpresaDePartida").getValue().toString());
							m.setInstrucciones(postSnapshot.child("instrucciones").getValue().toString());
							m.setEstadoDeOrden(postSnapshot.child("estadoDeOrden").getValue().toString());
							m.setLatLngA(new LatLng(latA, lngA));
							m.setLatLngB(new LatLng(latB, lngB));
							m.setNumeroDeOrden(postSnapshot.getKey().toString());
							m.setDriverAsignado(postSnapshot.child("driverAsignado").getValue().toString());
							m.setTelefonoDeClienteDeDestino(postSnapshot.child("telefonoDeClienteDeDestino").getValue().toString());
							m.setCostoOrden(postSnapshot.child("costoOrden").getValue().toString());
							if (m.getEstadoDeOrden().toString().toLowerCase().matches("Sin completar".toLowerCase()))
							{
								if (m.getDriverAsignado().toString().toLowerCase().matches("Sin asignar".toLowerCase()))
								{
									ordersList.add(m);
									CostoPorOrden precioModel = new CostoPorOrden();
									float numbers = Float.valueOf(postSnapshot.child("costoOrden").getValue().toString());
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
						totalAliquidar.setText(String.valueOf(sumarItems(items)));
						int count = 0;
						if (adapter != null)
						{
							count = adapter.getItemCount();
						}
						contarOrdenes.setText(String.valueOf(count));
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















//Ver drivers boton//Ver drivers boton//Ver drivers boton//Ver drivers boton//Ver drivers boton//Ver drivers boton//Ver drivers boton//Ver drivers boton//Ver drivers boton//Ver drivers boton//Ver drivers boton//Ver drivers boton//Ver drivers boton//Ver drivers boton//Ver drivers boton//Ver drivers boton
	public void verDrivers(View v)
	{
		String[] drivers = GetStringArray(DriversListUid);
		String[] names = GetStringArray(DriversListNames);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(new mSpinnerAdapter(Home.this, names, drivers), null);
        builder.setTitle("Lista de Drivers");
		builder.setItems(drivers, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					final String selectedUid = DriversListUid.get(p2);
					final String selectedName = DriversListNames.get(p2);
					Intent i = new Intent(Home.this,DriverDashboard.class);
					Bundle b = new Bundle();
					b.putString("driverName",selectedName);
					b.putString("uid",selectedUid);
					i.putExtras(b);
					startActivity(i);
					
					
					
								mDataBaseOrders.addListenerForSingleValueEvent(new ValueEventListener(){

										@Override
										public void onDataChange(DataSnapshot p1)
										{
											if (p1.exists())
											{

												List<mandaditosModel> ordersList = new ArrayList<mandaditosModel>();
												List<CostoPorOrden> costosPorOrdenList = new ArrayList<CostoPorOrden>();
												List<CostoPorOrden> costosEnvioList = new ArrayList<CostoPorOrden>();
												
												for (DataSnapshot postSnapshot : p1.getChildren())
												{

													double latA = postSnapshot.child("latLngA/latitude").getValue();
													double lngA = postSnapshot.child("latLngA/longitude").getValue();
													double latB = postSnapshot.child("latLngB/latitude").getValue();
													double lngB = postSnapshot.child("latLngB/longitude").getValue();

													mandaditosModel model = new mandaditosModel();
													model.setNumeroDeOrden(postSnapshot.getKey().toString());
													model.setEmpresaUserId(postSnapshot.child("empresaUserId").getValue().toString());
													model.setUsuario(postSnapshot.child("usuario").getValue().toString());
													model.setClienteDeDestino(postSnapshot.child("clienteDeDestino").getValue().toString());
													model.setDireccionDeDestino(postSnapshot.child("direccionDeDestino").getValue().toString());
													model.setCostoDelProducto(postSnapshot.child("costoDelProducto").getValue().toString());
													model.setCostoDelEnvio(postSnapshot.child("costoDelEnvio").getValue().toString());
													model.setEmpresaDePartida(postSnapshot.child("empresaDePartida").getValue().toString());
													model.setDireccionEmpresaDePartida(postSnapshot.child("direccionEmpresaDePartida").getValue().toString());
													model.setInstrucciones(postSnapshot.child("instrucciones").getValue().toString());
													model.setEstadoDeOrden(postSnapshot.child("estadoDeOrden").getValue().toString());
													model.setLatLngA(new LatLng(latA, lngA));
													model.setLatLngB(new LatLng(latB, lngB));
													model.setNumeroDeOrden(postSnapshot.getKey().toString());
													model.setDriverAsignado(postSnapshot.child("driverAsignado").getValue().toString());
													model.setDriverUid(postSnapshot.child("driverAsignado").getValue().toString());
													model.setTelefonoDeClienteDeDestino(postSnapshot.child("telefonoDeClienteDeDestino").getValue().toString());
													model.setCostoOrden(postSnapshot.child("costoOrden").getValue().toString());
													if (model.getDriverAsignado().toString().matches(selectedUid))
													{
														ordersList.add(model);
													}
													if (model.getDriverUid().toString().matches(selectedUid))
													{
														CostoPorOrden precioPorOrdenModel = new CostoPorOrden();
														float numbers = Float.valueOf(postSnapshot.child("costoOrden").getValue().toString());
														precioPorOrdenModel.setPrecioDeOrden(numbers);
														costosPorOrdenList.add(precioPorOrdenModel);
														
														//costo envio suma
														CostoPorOrden precioModelEnvio = new CostoPorOrden();
														float numbersEnvio = Float.valueOf(postSnapshot.child("costoDelEnvio").getValue().toString());
														if(Float.parseFloat(postSnapshot.child("costoDelEnvio").getValue().toString())>0.0f)
															{
															precioModelEnvio.setPrecioDeOrden(numbersEnvio-1);
															costosEnvioList.add(precioModelEnvio);
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
												float liquidado = 0;
												totalAliquidar.setText(String.valueOf(sumarItems(costosPorOrdenList) - liquidado));
												pagoDriverTv.setText(String.valueOf(sumarItems(costosEnvioList)));
												int count = 0;
												if (adapter != null)
												{
													count = adapter.getItemCount();
												}
												contarOrdenes.setText(String.valueOf(count));
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

			});


        builder.show();

	}
	
	
	
	
	
	
	
	
//Crear un nuevo usuario 
	public void nuevoCliente(View v){
		Intent i = new Intent(Home.this,CreateAccount.class);
		startActivity(i);
	}













//total calc
	private float sumarItems(List<CostoPorOrden> items)
	{
		float totalPrice = 0;
		for (int i = 0 ; i < items.size(); i++)
		{
			totalPrice += items.get(i).getPrecioDeOrden(); 
		}

		return totalPrice;
	}














//refresh
	public void refresh(View v)
	{
		finishAffinity();
		startActivity(new Intent(Home.this, Home.class));
	}


















	@Override
	public void onBackPressed()
	{
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setTitle("Salir");
		dialog.setMessage("¿Seguro que quieres salir?");
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
	
	
	
	
	
	
	
	
	
	
//FILTER
	//Filter
	private void filterEmpresa(final String text) {
		mDataBaseOrders = FirebaseDatabase.getInstance().getReference("Ordenes");
		mDataBaseOrders.addListenerForSingleValueEvent(new ValueEventListener(){

				private Float costosDeOrden;


				@Override
				public void onDataChange(DataSnapshot p1)
				{
					pDialog.dismiss();
					if (p1.exists())
					{
						List<CostoPorOrden> costoPorOrdenList = new ArrayList<CostoPorOrden>();
						List<mandaditosModel> ordersList = new ArrayList<mandaditosModel>();
						for (DataSnapshot postSnapshot : p1.getChildren())
						{
							double latA = postSnapshot.child("latLngA/latitude").getValue();
							double lngA = postSnapshot.child("latLngA/longitude").getValue();
							double latB = postSnapshot.child("latLngB/latitude").getValue();
							double lngB = postSnapshot.child("latLngB/longitude").getValue();

							mandaditosModel m = new mandaditosModel();
							m.setEmpresaUserId(postSnapshot.child("empresaUserId").getValue().toString());
							m.setUsuario(postSnapshot.child("usuario").getValue().toString());
							m.setClienteDeDestino(postSnapshot.child("clienteDeDestino").getValue().toString());
							m.setDireccionDeDestino(postSnapshot.child("direccionDeDestino").getValue().toString());

							m.setCostoDelProducto(postSnapshot.child("costoDelProducto").getValue().toString());
							m.setCostoDelEnvio(postSnapshot.child("costoDelEnvio").getValue().toString());
							m.setEmpresaDePartida(postSnapshot.child("empresaDePartida").getValue().toString());
							m.setDireccionEmpresaDePartida(postSnapshot.child("direccionEmpresaDePartida").getValue().toString());
							m.setInstrucciones(postSnapshot.child("instrucciones").getValue().toString());
							m.setEstadoDeOrden(postSnapshot.child("estadoDeOrden").getValue().toString());
							m.setLatLngA(new LatLng(latA, lngA));
							m.setLatLngB(new LatLng(latB, lngB));
							m.setNumeroDeOrden(postSnapshot.getKey().toString());
							m.setDriverAsignado(postSnapshot.child("driverAsignado").getValue().toString());
							m.setTelefonoDeClienteDeDestino(postSnapshot.child("telefonoDeClienteDeDestino").getValue().toString());
							m.setCostoOrden(postSnapshot.child("costoOrden").getValue().toString());
							if (m.getEmpresaDePartida().toString().toLowerCase().contains(text.toLowerCase()))
										
								{
									ordersList.add(m);
								}
							if (m.getEmpresaDePartida().toString().toLowerCase().contains(text.toLowerCase()))
								if(m.getEstadoDeOrden().toString().matches("Completada")){

									{
										CostoPorOrden precioModel = new CostoPorOrden();
										costosDeOrden = Float.valueOf(postSnapshot.child("costoDelProducto").getValue().toString());
										precioModel.setPrecioDeOrden(costosDeOrden);
										costoPorOrdenList.add(precioModel);
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
						totalAliquidar.setText(String.valueOf(sumarItems(costoPorOrdenList)));
						int count = 0;
						if (adapter != null)
						{
							count = adapter.getItemCount();
						}
						contarOrdenes.setText(String.valueOf(count));
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
	private void filterPersonas(final String text) {
		mDataBaseOrders = FirebaseDatabase.getInstance().getReference("Ordenes");
		mDataBaseOrders.addListenerForSingleValueEvent(new ValueEventListener(){

				private Float costosDeOrden;


				@Override
				public void onDataChange(DataSnapshot p1)
				{
					pDialog.dismiss();
					if (p1.exists())
					{
						List<CostoPorOrden> costoPorOrdenList = new ArrayList<CostoPorOrden>();
						List<mandaditosModel> ordersList = new ArrayList<mandaditosModel>();
						for (DataSnapshot postSnapshot : p1.getChildren())
						{
							double latA = postSnapshot.child("latLngA/latitude").getValue();
							double lngA = postSnapshot.child("latLngA/longitude").getValue();
							double latB = postSnapshot.child("latLngB/latitude").getValue();
							double lngB = postSnapshot.child("latLngB/longitude").getValue();

							mandaditosModel m = new mandaditosModel();
							m.setEmpresaUserId(postSnapshot.child("empresaUserId").getValue().toString());
							m.setUsuario(postSnapshot.child("usuario").getValue().toString());
							m.setClienteDeDestino(postSnapshot.child("clienteDeDestino").getValue().toString());
							m.setDireccionDeDestino(postSnapshot.child("direccionDeDestino").getValue().toString());

							m.setCostoDelProducto(postSnapshot.child("costoDelProducto").getValue().toString());
							m.setCostoDelEnvio(postSnapshot.child("costoDelEnvio").getValue().toString());
							m.setEmpresaDePartida(postSnapshot.child("empresaDePartida").getValue().toString());
							m.setDireccionEmpresaDePartida(postSnapshot.child("direccionEmpresaDePartida").getValue().toString());
							m.setInstrucciones(postSnapshot.child("instrucciones").getValue().toString());
							m.setEstadoDeOrden(postSnapshot.child("estadoDeOrden").getValue().toString());
							m.setLatLngA(new LatLng(latA, lngA));
							m.setLatLngB(new LatLng(latB, lngB));
							m.setNumeroDeOrden(postSnapshot.getKey().toString());
							m.setDriverAsignado(postSnapshot.child("driverAsignado").getValue().toString());
							m.setTelefonoDeClienteDeDestino(postSnapshot.child("telefonoDeClienteDeDestino").getValue().toString());
							m.setCostoOrden(postSnapshot.child("costoOrden").getValue().toString());
							if (m.getClienteDeDestino().toString().toLowerCase().contains(text.toLowerCase()))
							{
								ordersList.add(m);
								CostoPorOrden precioModel = new CostoPorOrden();
								costosDeOrden = Float.valueOf(postSnapshot.child("costoDelProducto").getValue().toString());
								precioModel.setPrecioDeOrden(costosDeOrden);
								costoPorOrdenList.add(precioModel);
							}

						}


						adapter = new mAdapter(Home.this, ordersList);
						mRecyclerView.setHasFixedSize(true);
						LinearLayoutManager layoutManager = new LinearLayoutManager(Home.this);
						layoutManager.setReverseLayout(true);
						layoutManager.setStackFromEnd(true);
						mRecyclerView.setLayoutManager(layoutManager);
						mRecyclerView.setAdapter(adapter);
						totalAliquidar.setText(String.valueOf(sumarItems(costoPorOrdenList)));
						int count = 0;
						if (adapter != null)
						{
							count = adapter.getItemCount();
						}
						contarOrdenes.setText(String.valueOf(count));
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
	
	private void filterLugar(final String text) {
		mDataBaseOrders = FirebaseDatabase.getInstance().getReference("Ordenes");
		mDataBaseOrders.addListenerForSingleValueEvent(new ValueEventListener(){

				private Float costosDeOrden;


				@Override
				public void onDataChange(DataSnapshot p1)
				{
					pDialog.dismiss();
					if (p1.exists())
					{
						List<CostoPorOrden> costoPorOrdenList = new ArrayList<CostoPorOrden>();
						List<mandaditosModel> ordersList = new ArrayList<mandaditosModel>();
						for (DataSnapshot postSnapshot : p1.getChildren())
						{
							double latA = postSnapshot.child("latLngA/latitude").getValue();
							double lngA = postSnapshot.child("latLngA/longitude").getValue();
							double latB = postSnapshot.child("latLngB/latitude").getValue();
							double lngB = postSnapshot.child("latLngB/longitude").getValue();

							mandaditosModel m = new mandaditosModel();
							m.setEmpresaUserId(postSnapshot.child("empresaUserId").getValue().toString());
							m.setUsuario(postSnapshot.child("usuario").getValue().toString());
							m.setClienteDeDestino(postSnapshot.child("clienteDeDestino").getValue().toString());
							m.setDireccionDeDestino(postSnapshot.child("direccionDeDestino").getValue().toString());

							m.setCostoDelProducto(postSnapshot.child("costoDelProducto").getValue().toString());
							m.setCostoDelEnvio(postSnapshot.child("costoDelEnvio").getValue().toString());
							m.setEmpresaDePartida(postSnapshot.child("empresaDePartida").getValue().toString());
							m.setDireccionEmpresaDePartida(postSnapshot.child("direccionEmpresaDePartida").getValue().toString());
							m.setInstrucciones(postSnapshot.child("instrucciones").getValue().toString());
							m.setEstadoDeOrden(postSnapshot.child("estadoDeOrden").getValue().toString());
							m.setLatLngA(new LatLng(latA, lngA));
							m.setLatLngB(new LatLng(latB, lngB));
							m.setNumeroDeOrden(postSnapshot.getKey().toString());
							m.setDriverAsignado(postSnapshot.child("driverAsignado").getValue().toString());
							m.setTelefonoDeClienteDeDestino(postSnapshot.child("telefonoDeClienteDeDestino").getValue().toString());
							m.setCostoOrden(postSnapshot.child("costoOrden").getValue().toString());
							if (m.getDireccionDeDestino().toString().toLowerCase().contains(text.toLowerCase()))

							{
								ordersList.add(m);
							}
							if (m.getDireccionDeDestino().toString().toLowerCase().contains(text.toLowerCase()))
								if(m.getEstadoDeOrden().toString().matches("Completada")){

									{
										CostoPorOrden precioModel = new CostoPorOrden();
										costosDeOrden = Float.valueOf(postSnapshot.child("costoDelProducto").getValue().toString());
										precioModel.setPrecioDeOrden(costosDeOrden);
										costoPorOrdenList.add(precioModel);
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
						totalAliquidar.setText(String.valueOf(sumarItems(costoPorOrdenList)));
						int count = 0;
						if (adapter != null)
						{
							count = adapter.getItemCount();
						}
						contarOrdenes.setText(String.valueOf(count));
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


	

}

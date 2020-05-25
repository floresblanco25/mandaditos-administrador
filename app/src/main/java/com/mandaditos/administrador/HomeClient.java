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
import com.mandaditos.administrador.R;
public class HomeClient extends AppCompatActivity
{

	private ProgressDialog pDialog;
	private DatabaseReference mDataBaseOrders;
	private RequestPermissionHandler mRequestPermissionHandler;
	private TextView contarOrdenes,totalAliquidar;
	private mAdapter adapter;
	private RecyclerView mRecyclerView;
	FirebaseAuth mFirebaseAuth;
	private String Empresa;
	private String Direccion = "";
	private Button nuevas; 
	private EditText buscarNombre;
	private String uId;





	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_client);
		mRequestPermissionHandler = new RequestPermissionHandler();
		mCheckPermission();
		contarOrdenes = findViewById(R.id.countmainTextView1Client);
		mRecyclerView = findViewById(R.id.recycler_ordersClient);
		mFirebaseAuth = FirebaseAuth.getInstance();
		totalAliquidar = findViewById(R.id.totalAliquidarClient);
		buscarNombre = findViewById(R.id.searchEdmainEditText1Client);
		
		
		
		
		
		
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
					final String nombreDeCliente = dataSnapshot.getValue(String.class);
					
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
										m.setCostoOrden(postSnapshot.child("costoOrden").getValue().toString());
										if(postSnapshot.child("empresa").getValue().toString().toLowerCase().matches(nombreDeCliente.toString().toLowerCase())){
											ordersList.add(m);
											CostoPorOrden precioModel = new CostoPorOrden();
											costosDeOrden = Float.valueOf(postSnapshot.child("costoDelProducto").getValue().toString());
											precioModel.setPrecioDeOrden(costosDeOrden);
											costoPorOrdenList.add(precioModel);
										}

									}


									adapter = new mAdapter(HomeClient.this, ordersList);
									mRecyclerView.setHasFixedSize(true);
									LinearLayoutManager layoutManager = new LinearLayoutManager(HomeClient.this);
									layoutManager.setReverseLayout(true);
									layoutManager.setStackFromEnd(true);
									mRecyclerView.setLayoutManager(layoutManager);
									mRecyclerView.setAdapter(adapter);
									totalAliquidar.setText(String.valueOf(grandTotal(costoPorOrdenList)));
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


					
						
					Empresa=nombreDeCliente;
				}

				@Override
				public void onCancelled(DatabaseError databaseError) {

				}
			});

		DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Usuarios/" + userId + "/Perfil").child("address");
		ref2.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					Direccion = dataSnapshot.getValue(String.class);

				}

				@Override
				public void onCancelled(DatabaseError databaseError) {

				}
			});
			














		//dialog 
		pDialog = new ProgressDialog(HomeClient.this);
		pDialog.setMessage("Cargando datos de los servidores..");
		pDialog.show();














//Search edittext
		buscarNombre.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void afterTextChanged(Editable editable) {
					filter(editable.toString());
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
			this.susbtitles = subtitles;
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
			if (convertView == null)
			{
				convertView = getLayoutInflater().inflate(R.layout.drivers_list_row, null);
			}

			((TextView)convertView.findViewById(R.id.text1)).setText(titles[position]);
			((TextView)convertView.findViewById(R.id.text2)).setText(susbtitles[position]);

			return convertView;
		}

	}










//Añadir
	public void addOrderClient(View v)
	{
		FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
		String UserId = mFirebaseUser.getUid().toString();
		FirebaseDatabase.getInstance().getReference("Ordenes")
			.push()
			.setValue(new mandaditosModel("0", Empresa, Direccion, "", UserId, "", "", "", "", "", 
										  "", "", "0", "Sin completar", new LatLng(13.67694, -89.27972), new LatLng(13.67694, -89.27972), "Sin asignar", "", "0"));
		finishAffinity();
		startActivity(new Intent(HomeClient.this, HomeClient.class));
	}












































//total calc
	private float grandTotal(List<CostoPorOrden> items)
	{
		float totalPrice = 0;
		for (int i = 0 ; i < items.size(); i++)
		{
			totalPrice += items.get(i).getPrecioDeOrden(); 
		}

		return totalPrice;
	}














//refresh
	public void refreshClient(View v)
	{
		finishAffinity();
		startActivity(new Intent(HomeClient.this, HomeClient.class));
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
	private void filter(final String text) {
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
					final String nombreDeCliente = dataSnapshot.getValue(String.class);
			
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
							m.setCostoOrden(postSnapshot.child("costoOrden").getValue().toString());
							if (m.getPartida().toString().toLowerCase().contains(text.toLowerCase()))
							{
								if(postSnapshot.child("empresa").getValue().toString().toLowerCase().matches(nombreDeCliente.toString().toLowerCase())){
								ordersList.add(m);
								CostoPorOrden precioModel = new CostoPorOrden();
								costosDeOrden = Float.valueOf(postSnapshot.child("costoDelProducto").getValue().toString());
								precioModel.setPrecioDeOrden(costosDeOrden);
								costoPorOrdenList.add(precioModel);
								}
							}

						}


						adapter = new mAdapter(HomeClient.this, ordersList);
						mRecyclerView.setHasFixedSize(true);
						LinearLayoutManager layoutManager = new LinearLayoutManager(HomeClient.this);
						layoutManager.setReverseLayout(true);
						layoutManager.setStackFromEnd(true);
						mRecyclerView.setLayoutManager(layoutManager);
						mRecyclerView.setAdapter(adapter);
						totalAliquidar.setText(String.valueOf(grandTotal(costoPorOrdenList)));
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

				@Override
				public void onCancelled(DatabaseError databaseError) {

				}
			});

    }
	
	
	
	




}
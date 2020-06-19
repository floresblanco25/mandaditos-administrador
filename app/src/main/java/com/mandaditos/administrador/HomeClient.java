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

	private FireDataDb fireData;
	private List<mandaditosModel> ordersList;





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
		
		
		
		
		

//Initialize orders 
		fireData = new FireDataDb();
		
		
		
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
					//dialog 
					pDialog = new ProgressDialog(HomeClient.this);
					pDialog.setMessage("Cargando datos de los servidores..");
					pDialog.setCancelable(false);
					pDialog.show();
					final List<CostoPorOrden> items = new ArrayList<CostoPorOrden>();
					final List<mandaditosModel> filteredOrdersList = new ArrayList<mandaditosModel>();
					DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Ordenes");
					mRef.addListenerForSingleValueEvent(new ValueEventListener(){

							@Override
							public void onDataChange(DataSnapshot rerSnapshot)
							{
								pDialog.dismiss();
								ordersList = fireData.getFireDataList(rerSnapshot);
								for (mandaditosModel order : ordersList) {
									if (order.getEmpresaDePartida().equalsIgnoreCase(nombreDeCliente)) {
										filteredOrdersList.add(order);
										if(order.getEstadoDeOrden().equalsIgnoreCase("Completada")){
										CostoPorOrden precioModel = new CostoPorOrden();
										float numbers = Float.valueOf(order.getCostoDelProducto().toString());
										precioModel.setPrecioDeOrden(numbers);
										items.add(precioModel);
										}
									} 

								}
								mAdapter adapter = new mAdapter(HomeClient.this, filteredOrdersList);
								mRecyclerView.setVisibility(View.VISIBLE);
								mRecyclerView.setHasFixedSize(true);
								LinearLayoutManager layoutManager = new LinearLayoutManager(HomeClient.this);
								layoutManager.setReverseLayout(true);
								layoutManager.setStackFromEnd(true);
								mRecyclerView.setLayoutManager(layoutManager);
								mRecyclerView.setAdapter(adapter);
								int count = 0;
								if (adapter != null)
								{
									count = adapter.getItemCount();
								}
								contarOrdenes.setText(count+"");
								totalAliquidar.setText(String.valueOf(grandTotal(items)));
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
			.setValue(new mandaditosModel("0", Empresa, Direccion, "", UserId, "", "", "", "0", "Sin completar", new LatLng(13.67694, -89.27972), new LatLng(13.67694, -89.27972), "Sin asignar", "", "0","0"));
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
						
						
						
					final List<CostoPorOrden> items = new ArrayList<CostoPorOrden>();
					final List<mandaditosModel> filteredOrdersList = new ArrayList<mandaditosModel>();
					DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Ordenes");
					mRef.addListenerForSingleValueEvent(new ValueEventListener(){

							@Override
							public void onDataChange(DataSnapshot rerSnapshot)
							{
								ordersList = fireData.getFireDataList(rerSnapshot);
								for (mandaditosModel order : ordersList) {
									if (order.getEmpresaDePartida().toLowerCase().matches(nombreDeCliente.toString().toLowerCase())) {
										if(order.getClienteDeDestino().toString().toLowerCase().contains(text.toString().toLowerCase())){
											filteredOrdersList.add(order);
											CostoPorOrden precioModel = new CostoPorOrden();
											costosDeOrden = Float.valueOf(order.getCostoDelProducto().toString());
											precioModel.setPrecioDeOrden(costosDeOrden);
											items.add(precioModel);
										}
										
									}

								}
								adapter = new mAdapter(HomeClient.this, filteredOrdersList);
								mRecyclerView.setHasFixedSize(true);
								LinearLayoutManager layoutManager = new LinearLayoutManager(HomeClient.this);
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
								contarOrdenes.setText(String.valueOf(count));
							}

							@Override
							public void onCancelled(DatabaseError p1)
							{
							}
						});

				
				
				
				
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

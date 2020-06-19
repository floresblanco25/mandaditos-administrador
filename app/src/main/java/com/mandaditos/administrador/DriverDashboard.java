package com.mandaditos.administrador;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.google.firebase.database.*;
import com.mandaditos.administrador.mUtilities.*;
import com.mandaditos.administrador.models.*;
import java.util.*;
import com.mandaditos.administrador.adapters.*;

public class DriverDashboard extends AppCompatActivity
{

	private String driverTxt;
	private String uid;
	private RecyclerView mRecyclerView;
	private TextView total,allCountTv,driverTitle,pagadoTv,completadoDeMuchas;
	private ListView listView;
	private List<mandaditosModel> ordersList;
	LinearLayout drLayout;

	private FireDataDb fireData;


	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_dashboard);
		mRecyclerView = findViewById(R.id.recycler_orders);
		listView = findViewById(R.id.dashboardList);
		total = findViewById(R.id.totalDineroTv);
		allCountTv = findViewById(R.id.countDriverTv);
		driverTitle = findViewById(R.id.driverTitleDashboard);
		drLayout = findViewById(R.id.driverdashboardLinearLayout1);
		pagadoTv = findViewById(R.id.pagadoTv);
		completadoDeMuchas = findViewById(R.id.completedCount);



		
		
		
		
		
		
//On create 
		Bundle b = getIntent().getExtras();		
		driverTxt = b.getString("driverName");
		uid = b.getString("uid");
		TextView driverTv = findViewById(R.id.driverNameDashboardTv);
		driverTv.setText(driverTxt);
		fireData = new FireDataDb();
		showOrders();
		
		
		
		
		
		
		
		
		







	}
	
	
	
	
	
	
	
	
	
//button menu
	public void mostrarHerramientas(View v){
		herramientas();
	}
	
	
	
	
	
	
	
	
	
	
	
	
//Herramientas
	private void herramientas(){
		String[] titles = {"Ordenes a cargo","Total de liquidación","Pago de driver"};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(new mSpinnerAdapter(DriverDashboard.this, titles, titles), null);
        builder.setTitle(driverTxt);
		builder.setCancelable(true);
		builder.setItems(titles, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					switch (p2)
					{

						case 0:
							showOrders();
							break;

						case 1:
							showLiquidacion();
							break;
							
						case 2:
							pagoDeDriver();


					}

				}
			});
		builder.show();

	}
	
	
	
	
	
	
	
	
//Show orders
	private void showOrders(){
		final List<mandaditosModel> filteredOrdersListFinal = new ArrayList<mandaditosModel>();
		final List<CostoPorOrden> costosPorOrdenCompletedList = new ArrayList<CostoPorOrden>();
		final List<CostoPorOrden> costosEnvioList = new ArrayList<CostoPorOrden>();

		
		DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Ordenes");
		mRef.addListenerForSingleValueEvent(new ValueEventListener(){

				@Override
				public void onDataChange(DataSnapshot rerSnapshot)
				{
					ordersList = fireData.getFireDataList(rerSnapshot);
					for (mandaditosModel driver : ordersList) {
						if (driver.getDriverAsignado().equalsIgnoreCase(uid)) {
							filteredOrdersListFinal.add(driver);
						} 

					}
					mAdapter adapter = new mAdapter(DriverDashboard.this, filteredOrdersListFinal);
					driverTitle.setText("ORDENES A CARGO: ");
					mRecyclerView.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
					mRecyclerView.setHasFixedSize(true);
					LinearLayoutManager layoutManager = new LinearLayoutManager(DriverDashboard.this);
					layoutManager.setReverseLayout(true);
					layoutManager.setStackFromEnd(true);
					mRecyclerView.setLayoutManager(layoutManager);
					mRecyclerView.setAdapter(adapter);
					int count = 0;
					if (adapter != null)
					{
						count = adapter.getItemCount();
					}
					allCountTv.setText(count+"");
					drLayout.setVisibility(View.GONE);
					for (int i=0; i<filteredOrdersListFinal.size(); i++) {
						if (filteredOrdersListFinal.get(i).getEstadoDeOrden().matches("Completada"))
						{
							//costo de orden list
							CostoPorOrden precioPorOrdenCompletedModel = new CostoPorOrden();
							float numbersCostoOrden = Float.valueOf(filteredOrdersListFinal.get(i).getCostoOrden());
							precioPorOrdenCompletedModel.setPrecioDeOrden(numbersCostoOrden);
							costosPorOrdenCompletedList.add(precioPorOrdenCompletedModel);


							//costo de envio list - 1$
							CostoPorOrden precioModelEnvio = new CostoPorOrden();
							float numbersEnvio = Float.valueOf(filteredOrdersListFinal.get(i).getCostoDelEnvio());
							if(Float.parseFloat(filteredOrdersListFinal.get(i).getCostoDelEnvio())>0.0f)
							{
								precioModelEnvio.setPrecioDeOrden(numbersEnvio-1);
								costosEnvioList.add(precioModelEnvio);
							}
						}else{
						}
						int countJustCompleted = 0;
						countJustCompleted = costosPorOrdenCompletedList.size();
						completadoDeMuchas.setText(countJustCompleted+"");
					}
				}
				

				@Override
				public void onCancelled(DatabaseError p1)
				{
				}
			});

		
					
				}

	
	
	
	
	
	
	
	
	
	
	
	
	
//Liquidacion
	private void showLiquidacion(){
		driverTitle.setText("LIQUIDACIÓN: ");
		
		//LISTS
		final List<CostoPorOrden> costosPorOrdenCompletedList = new ArrayList<CostoPorOrden>();
		final List<CostoPorOrden> costosEnvioList = new ArrayList<CostoPorOrden>();
		final ArrayList<mandaditosModel> filteredOrdersListFinal = new ArrayList<mandaditosModel>();
		
		
		//Refs
		DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Ordenes");
		mRef.addListenerForSingleValueEvent(new ValueEventListener(){

				@Override
				public void onDataChange(DataSnapshot refSnapshot)
				{
					
					//Mostrar ordenes en vista simple
					ordersList = fireData.getFireDataList(refSnapshot);
					for (mandaditosModel order : ordersList) {
						//filter by driver uid
						if (order.getDriverAsignado().equalsIgnoreCase(uid)) {
							filteredOrdersListFinal.add(order);
						} 

					}

					
					//updateviews
					mRecyclerView.setAdapter(null);
					mRecyclerView.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);
					
					
					//setadapter
					simpleListAdapter adptr = new simpleListAdapter(DriverDashboard.this,filteredOrdersListFinal,false);
					listView.setAdapter(adptr);
					
					//count from adapter
					int countAll = 0;
					if (adptr != null){
						countAll = adptr.getCount();
						
					}
					allCountTv.setText(countAll+"");
					
					
					
					
					//Suma de los costos
					for (int i=0; i<filteredOrdersListFinal.size(); i++) {
						if (filteredOrdersListFinal.get(i).getEstadoDeOrden().matches("Completada"))
						{
							//costo de orden list
							CostoPorOrden precioPorOrdenCompletedModel = new CostoPorOrden();
							float numbersCostoOrden = Float.valueOf(filteredOrdersListFinal.get(i).getCostoOrden());
							precioPorOrdenCompletedModel.setPrecioDeOrden(numbersCostoOrden);
							costosPorOrdenCompletedList.add(precioPorOrdenCompletedModel);


							//costo de envio list - 1$
							CostoPorOrden precioModelEnvio = new CostoPorOrden();
							float numbersEnvio = Float.valueOf(filteredOrdersListFinal.get(i).getCostoDelEnvio());
							if(Float.parseFloat(filteredOrdersListFinal.get(i).getCostoDelEnvio())>0.0f)
							{
								precioModelEnvio.setPrecioDeOrden(numbersEnvio-1);
								costosEnvioList.add(precioModelEnvio);
							}
						}else{
						}
						int countJustCompleted = 0;
						countJustCompleted = costosPorOrdenCompletedList.size();
						completadoDeMuchas.setText(countJustCompleted+"");
					}

					//TOTAL de costo por ordenes
					total.setText(String.valueOf(sumarItems(costosPorOrdenCompletedList)));
					pagadoTv.setText(String.valueOf(sumarItems(costosPorOrdenCompletedList)));
					
					
							//click handle
					listView.setOnItemLongClickListener(new OnItemLongClickListener(){

							@Override
							public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4)
							{
								Toast.makeText(DriverDashboard.this," "+p3,500).show();
								return false;
							}
						});
						
						
					drLayout.setVisibility(View.VISIBLE);
					
					
					}

				@Override
				public void onCancelled(DatabaseError p1)
				{
				}
			});
			
			
		


	}
	
	
	
	
	
	
	
	
//Pago
	private void pagoDeDriver(){
		driverTitle.setText("PAGO DE DRIVER");
			//LISTS
			final List<CostoPorOrden> costosPorOrdenCompletedList = new ArrayList<CostoPorOrden>();
			final List<CostoPorOrden> costosEnvioCompletedList = new ArrayList<CostoPorOrden>();
			final ArrayList<mandaditosModel> filteredOrdersListFinal = new ArrayList<mandaditosModel>();


			//Refs
			DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Ordenes");
			mRef.addListenerForSingleValueEvent(new ValueEventListener(){

					@Override
					public void onDataChange(DataSnapshot refSnapshot)
					{

						//Mostrar ordenes en vista simple
						ordersList = fireData.getFireDataList(refSnapshot);
						for (mandaditosModel order : ordersList) {
							//filter by driver uid
							if (order.getDriverAsignado().equalsIgnoreCase(uid)) {
								filteredOrdersListFinal.add(order);
							} 

						}


						//updateviews
						mRecyclerView.setAdapter(null);
						mRecyclerView.setVisibility(View.GONE);
						listView.setVisibility(View.VISIBLE);


						//setadapter
						simpleListAdapter adptr = new simpleListAdapter(DriverDashboard.this,filteredOrdersListFinal,true);
						listView.setAdapter(adptr);

						//count from adapter
						int countAll = 0;
						if (adptr != null){
							countAll = adptr.getCount();

						}
						allCountTv.setText(countAll+"");




						//Suma de los costos
						for (int i=0; i<filteredOrdersListFinal.size(); i++) {
							if (filteredOrdersListFinal.get(i).getEstadoDeOrden().matches("Completada"))
							{
								//costo de orden list
								CostoPorOrden precioPorOrdenCompletedModel = new CostoPorOrden();
								float numbersCostoOrden = Float.valueOf(filteredOrdersListFinal.get(i).getCostoDelEnvio());
								precioPorOrdenCompletedModel.setPrecioDeOrden(numbersCostoOrden);
								costosPorOrdenCompletedList.add(precioPorOrdenCompletedModel);


								//costo de envio list - 1$
								CostoPorOrden precioModelEnvio = new CostoPorOrden();
								float numbersEnvio = Float.valueOf(filteredOrdersListFinal.get(i).getCostoDelEnvio());
								if(Float.parseFloat(filteredOrdersListFinal.get(i).getCostoDelEnvio())>0.0f)
								{
									precioModelEnvio.setPrecioDeOrden(numbersEnvio-1);
									costosEnvioCompletedList.add(precioModelEnvio);
								}
							}else{
							}
							int countJustCompleted = 0;
							countJustCompleted = costosPorOrdenCompletedList.size();
							completadoDeMuchas.setText(countJustCompleted+"");
						}

						//TOTAL de costo por ordenes
						total.setText(String.valueOf(sumarItems(costosEnvioCompletedList)));
						pagadoTv.setText(String.valueOf(sumarItems(costosPorOrdenCompletedList)));



						//click handle
						listView.setOnItemLongClickListener(new OnItemLongClickListener(){

								@Override
								public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4)
								{
									Toast.makeText(DriverDashboard.this," "+p3,500).show();
									return false;
								}
							});


						drLayout.setVisibility(View.VISIBLE);


					}

					@Override
					public void onCancelled(DatabaseError p1)
					{
					}
				});





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
	
}

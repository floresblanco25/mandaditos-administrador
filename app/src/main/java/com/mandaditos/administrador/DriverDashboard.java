package com.mandaditos.administrador;
import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.mandaditos.administrador.mUtilities.*;
import com.mandaditos.administrador.models.*;
import java.util.*;

import android.support.v7.app.AlertDialog;
import com.google.firebase.database.*;

public class DriverDashboard extends AppCompatActivity
{

	private String driverTxt;
	private String uid;
	private RecyclerView mRecyclerView;
	private TextView total,countDriverTv,driverTitle,pagadoTv;
	private ListView dashboardList;
	private List<mandaditosModel> ordersList;
	LinearLayout drLayout;


	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_dashboard);
		mRecyclerView = findViewById(R.id.recycler_orders);
		dashboardList = findViewById(R.id.dashboardList);
		total = findViewById(R.id.totalDineroTv);
		countDriverTv = findViewById(R.id.countDriverTv);
		driverTitle = findViewById(R.id.driverTitleDashboard);
		drLayout = findViewById(R.id.driverdashboardLinearLayout1);
		pagadoTv = findViewById(R.id.pagadoTv);



		
		
		
		
		
		
//On create 
		Bundle b = getIntent().getExtras();		
		driverTxt = b.getString("driverName");
		uid = b.getString("uid");
		TextView driverTv = findViewById(R.id.driverNameDashboardTv);
		driverTv.setText(driverTxt);
		FireDataDb fireData = new FireDataDb();
		ordersList = fireData.getFireData(DriverDashboard.this,"Ordenes", uid);
		herramientas();
		
		
		
		
		
		
		
		
		







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
		builder.setCancelable(false);
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
		driverTitle.setText("ORDENES A CARGO: ");
		mRecyclerView.setVisibility(View.VISIBLE);
		dashboardList.setVisibility(View.GONE);
		mAdapter adapter = new mAdapter(DriverDashboard.this, ordersList);
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
		countDriverTv.setText(count+"");
		drLayout.setVisibility(View.GONE);
	}
	
	
	
	
	
	
	
//cobrar boton
	public void cobrar(View v){
		final AlertDialog dialog = new AlertDialog.Builder(DriverDashboard.this).create();
		dialog.setTitle("Cobrar ordenes");
		dialog.setMessage("Cobrar "+total.getText().toString());
		final EditText input = new EditText(DriverDashboard.this);  
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
		dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cobrar", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
						DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
						Query applesQuery = ref.child("Ordenes").orderByChild("estadoDeOrden").equalTo("Completada");

						applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
								@Override
								public void onDataChange(DataSnapshot dataSnapshot)
								{
									for (DataSnapshot appleSnapshot: dataSnapshot.getChildren())
									{

									}
								}

								@Override
								public void onCancelled(DatabaseError databaseError)
								{
									Toast.makeText(DriverDashboard.this, databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
								}
							});
				}
			});
		dialog.show();
		
	}
	
	
	
	
	
	
	
	
//Liquidacion
	private void showLiquidacion(){
		driverTitle.setText("LIQUIDACIÓN");
		List<CostoPorOrden> costosPorOrdenList = new ArrayList<CostoPorOrden>();
		List<CostoPorOrden> costosEnvioList = new ArrayList<CostoPorOrden>();
		ArrayList<String> values = new ArrayList<String>();
		mAdapter adapter2 = new mAdapter(DriverDashboard.this, ordersList);
		int count = 0;
		if (adapter2 != null)
		{
			count = adapter2.getItemCount();
		}
		countDriverTv.setText(count+"");
		mRecyclerView.setAdapter(null);
		mRecyclerView.setVisibility(View.GONE);
		dashboardList.setVisibility(View.VISIBLE);
		for (int i=0; i<ordersList.size(); i++) {
			if(ordersList.get(i).getEstadoDeOrden().matches("Completada")){
				
			values.add(ordersList.get(i).getClienteDeDestino()+"\n"+"$"+ordersList.get(i).getCostoOrden());
			CostoPorOrden precioPorOrdenModel = new CostoPorOrden();
			float numbers = Float.valueOf(ordersList.get(i).getCostoOrden());
			precioPorOrdenModel.setPrecioDeOrden(numbers);
			costosPorOrdenList.add(precioPorOrdenModel);

			CostoPorOrden precioModelEnvio = new CostoPorOrden();
			float numbersEnvio = Float.valueOf(ordersList.get(i).getCostoDelEnvio());
			if(Float.parseFloat(ordersList.get(i).getCostoDelEnvio())>0.0f)
			{
				precioModelEnvio.setPrecioDeOrden(numbersEnvio-1);
				costosEnvioList.add(precioModelEnvio);
			}
			}
		}
		float liquidado = 0;
		total.setText(String.valueOf(sumarItems(costosPorOrdenList) - liquidado));


		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(DriverDashboard.this,
																	R.layout.sumar_simple_row, android.R.id.text1, values);
		dashboardList.setAdapter(listAdapter); 
		drLayout.setVisibility(View.VISIBLE);


	}
	
	
	
	
	
	
	
	
//Pago
	private void pagoDeDriver(){
		driverTitle.setText("PAGO DE DRIVER");
		List<CostoPorOrden> costosPorOrdenList = new ArrayList<CostoPorOrden>();
		List<CostoPorOrden> costosEnvioList = new ArrayList<CostoPorOrden>();
		ArrayList<String> values = new ArrayList<String>();
		mAdapter adapter2 = new mAdapter(DriverDashboard.this, ordersList);
		int count = 0;
		if (adapter2 != null)
		{
			count = adapter2.getItemCount();
		}
		countDriverTv.setText(count+"");
		mRecyclerView.setAdapter(null);
		mRecyclerView.setVisibility(View.GONE);
		dashboardList.setVisibility(View.VISIBLE);
		for (int i=0; i<ordersList.size(); i++) {
			if(ordersList.get(i).getEstadoDeOrden().matches("Completada")){
				
			float numbersEnvio = Float.valueOf(ordersList.get(i).getCostoDelEnvio());
			values.add(ordersList.get(i).getClienteDeDestino()+"\n$"+(numbersEnvio-1));
			CostoPorOrden precioPorOrdenModel = new CostoPorOrden();
			float numbers = Float.valueOf(ordersList.get(i).getCostoOrden());
			precioPorOrdenModel.setPrecioDeOrden(numbers);
			costosPorOrdenList.add(precioPorOrdenModel);

			CostoPorOrden precioModelEnvio = new CostoPorOrden();
			if(Float.parseFloat(ordersList.get(i).getCostoDelEnvio())>0.0f)
			{
				precioModelEnvio.setPrecioDeOrden(numbersEnvio-1);
				costosEnvioList.add(precioModelEnvio);
			}
		}
		}
		total.setText(String.valueOf(sumarItems(costosEnvioList)));


		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(DriverDashboard.this,
																	R.layout.sumar_simple_row, android.R.id.text1, values);
		dashboardList.setAdapter(listAdapter); 
		drLayout.setVisibility(View.VISIBLE);
		
		
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

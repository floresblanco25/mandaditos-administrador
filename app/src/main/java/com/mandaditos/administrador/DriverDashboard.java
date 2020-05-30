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
	private TextView LiquidacionTv;
	private ListView dashboardList;

	private List<mandaditosModel> ordersList;

	private ProgressDialog pDialog;

	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_dashboard);
		mRecyclerView = findViewById(R.id.recycler_orders);
		dashboardList = findViewById(R.id.dashboardList);
		LiquidacionTv = findViewById(R.id.totalDineroTv);



		Bundle b = getIntent().getExtras();		
		driverTxt = b.getString("driverName");
		uid = b.getString("uid");
		TextView driverTv = findViewById(R.id.driverNameDashboardTv);
		driverTv.setText(driverTxt);
		FireDataDb fireData = new FireDataDb();
		ordersList = fireData.getFireData(DriverDashboard.this,"Ordenes", uid);


		
		
		
		
		
		herramientas();
		
		
		
		
		
		
		
		
		







	}
	public void mostrarHerramientas(View v){
		herramientas();
	}
	
	
	
	
	
	
	private void showLiquidacion(){
		List<CostoPorOrden> costosPorOrdenList = new ArrayList<CostoPorOrden>();
		List<CostoPorOrden> costosEnvioList = new ArrayList<CostoPorOrden>();
		ArrayList<String> values = new ArrayList<String>();
		mAdapter adapter2 = new mAdapter(DriverDashboard.this, ordersList);
		int count2 = 0;
		if (adapter2 != null)
		{
			count2 = adapter2.getItemCount();
		}
		mRecyclerView.setAdapter(null);
		mRecyclerView.setVisibility(View.GONE);
		dashboardList.setVisibility(View.VISIBLE);
		for (int i=0; i<ordersList.size(); i++) {
			values.add(i+1+"- "+ordersList.get(i).getClienteDeDestino()+"------$"+ordersList.get(i).getCostoOrden());
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
		float liquidado = 0;
		LiquidacionTv.setText(String.valueOf(sumarItems(costosPorOrdenList) - liquidado));


		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(DriverDashboard.this,
																	android.R.layout.simple_list_item_1, android.R.id.text1, values);
		dashboardList.setAdapter(listAdapter); 

		
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

	
	
	private void herramientas(){
		String[] titles = {"Ordenes a cargo","Total de liquidación"};
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


							break;

						case 1:
							showLiquidacion();


					}

				}
			});
		builder.show();

	}
}

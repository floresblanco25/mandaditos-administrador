package com.mandaditos.administrador;
import android.content.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.google.android.gms.maps.model.*;
import com.google.firebase.database.*;
import com.mandaditos.administrador.*;
import com.mandaditos.administrador.models.*;
import java.util.*;
import com.mandaditos.administrador.mUtilities.*;

public class DriverDashboard extends AppCompatActivity
{

	private String driverTxt;
	private DatabaseReference mDataBaseOrders;
	private String uid;
	private RecyclerView mRecyclerView;
	private TextView countTv;

	private List<mandaditosModel> ordersList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_dashboard);
		mRecyclerView = findViewById(R.id.recycler_orders);
		countTv = findViewById(R.id.countmainTextView1);
		
		
		
		Bundle b = getIntent().getExtras();		
		driverTxt = b.getString("driverName");
		uid = b.getString("uid");
		TextView driverTv = findViewById(R.id.driverNameDashboardTv);
		driverTv.setText(driverTxt);
		ordersList = FireDataDb.getFireData("Ordenes",uid);
		
		
		
		
		
		
		
	}
	public void mostrarHerramientas(View v){
		String[] titles = {"Ordenes a cargo","Total de liquidación"};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(new mSpinnerAdapter(DriverDashboard.this, titles, titles), null);
        builder.setTitle(driverTxt);
		builder.setItems(titles, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					
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
					countTv.setText(String.valueOf(count));
					
				}
			});
			builder.show();

	}
	
}

package com.mandaditos.administrador;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.widget.*;
import com.google.firebase.database.*;
import com.mandaditos.administrador.*;
import java.util.*;

import com.mandaditos.administrador.R;
import com.mandaditos.administrador.models.*;
public class Home extends AppCompatActivity 
{

	private ProgressDialog pDialog;

	private DatabaseReference mDataBase;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);







		//dialog 
		pDialog = new ProgressDialog(Home.this);
		pDialog.setMessage("Cargando datos de los servidores..");
		pDialog.show();
		

		mDataBase = FirebaseDatabase.getInstance().getReference("Ordenes");
		mDataBase.addValueEventListener(new ValueEventListener(){


				@Override
				public void onDataChange(DataSnapshot p1)
				{
					pDialog.dismiss();
					if(p1.exists()){

						List<mandaditosModel> ordersList = new ArrayList<mandaditosModel>();
						for (DataSnapshot postSnapshot : p1.getChildren()) {
							mandaditosModel m = new mandaditosModel();
							m.setMUserId(postSnapshot.child("muserId").getValue().toString());
							m.setUsuario(postSnapshot.child("usuario").getValue().toString());
							m.setPartida(postSnapshot.child("partida").getValue().toString());
							m.setDestino(postSnapshot.child("destino").getValue().toString());
							m.setDistancia(postSnapshot.child("distancia").getValue().toString());
							m.setFecha(postSnapshot.child("fecha").getValue().toString());
							m.setETA(postSnapshot.child("eta").getValue().toString());
							m.setRecogerDineroEn(postSnapshot.child("recogerDineroEn").getValue().toString());
							m.setCosto(postSnapshot.child("costo").getValue().toString());
							m.setEstadoDeOrden(postSnapshot.child("estadoDeOrden").getValue().toString());
							m.setNumeroDeOrden(postSnapshot.getKey().toString());
								ordersList.add(m);
						}


						mAdapter adapter = new mAdapter(Home.this,ordersList);
						RecyclerView mRecyclerView = findViewById(R.id.recycler_orders);
						mRecyclerView.setHasFixedSize(true);
						LinearLayoutManager layoutManager = new LinearLayoutManager(Home.this);
						mRecyclerView.setLayoutManager(layoutManager);
						mRecyclerView.setAdapter(adapter);
					}
					else{
					}
				}



				@Override
				public void onCancelled(DatabaseError p1)
				{
				}
			});





	}

	@Override
	public void onBackPressed() {
		finishAffinity();
        startActivity(new Intent(Home.this,Home.class));
    } 
}

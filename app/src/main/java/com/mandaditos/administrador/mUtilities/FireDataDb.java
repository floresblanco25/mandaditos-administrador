package com.mandaditos.administrador.mUtilities;

import android.app.*;
import com.google.android.gms.maps.model.*;
import com.google.firebase.database.*;
import com.mandaditos.administrador.*;
import com.mandaditos.administrador.models.*;
import java.util.*;
import android.content.*;

public  class FireDataDb
{

	public FireDataDb(){}
	
	public List<mandaditosModel>  getFireData(Context context,String path,final String uid){
		final ProgressDialog pDialog = new ProgressDialog(context);
		pDialog.setMessage("Cargando datos de los servidores..");
		pDialog.setCancelable(false);
		pDialog.show();
		
		final List<mandaditosModel> ordersList = new ArrayList<mandaditosModel>();
		DatabaseReference mRef = FirebaseDatabase.getInstance().getReference(path);
		mRef.addListenerForSingleValueEvent(new ValueEventListener(){

				private mAdapter adapter;

				@Override
				public void onDataChange(DataSnapshot p1)
				{
					pDialog.dismiss();
					if (p1.exists())
					{


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
							if (model.getDriverAsignado().toString().matches(uid))
							{
								ordersList.add(model);
							}
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
		return ordersList;
	}
	
}

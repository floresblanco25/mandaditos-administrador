package com.mandaditos.administrador.mUtilities;

import android.app.*;
import com.google.android.gms.maps.model.*;
import com.google.firebase.database.*;
import com.mandaditos.administrador.*;
import com.mandaditos.administrador.models.*;
import java.util.*;
import android.content.*;
import android.widget.*;

public  class FireDataDb
{

	Context context;
	public FireDataDb(){}
	
	
	public List<mandaditosModel> getFireDataList(DataSnapshot referencedSnapshot){
		final List<mandaditosModel> ordersList = new ArrayList<mandaditosModel>();
		if (referencedSnapshot.exists())
		{
			for (DataSnapshot postSnapshot : referencedSnapshot.getChildren())
			{
				
		double latA = postSnapshot.child("latLngA/latitude").getValue();
		double lngA = postSnapshot.child("latLngA/longitude").getValue();
		double latB = postSnapshot.child("latLngB/latitude").getValue();
		double lngB = postSnapshot.child("latLngB/longitude").getValue();

		mandaditosModel model = new mandaditosModel();
		model.setNumeroDeOrden(postSnapshot.getKey().toString());
		model.setEmpresaUserId(tryGetData(postSnapshot,"empresaUserId"));
		model.setUsuario(tryGetData(postSnapshot,"usuario"));
		model.setClienteDeDestino(tryGetData(postSnapshot,"clienteDeDestino"));
		model.setDireccionDeDestino(tryGetData(postSnapshot,"direccionDeDestino"));
		model.setCostoDelProducto(tryGetData(postSnapshot,"costoDelProducto"));
		model.setCostoDelEnvio(tryGetData(postSnapshot,"costoDelEnvio"));
		model.setEmpresaDePartida(tryGetData(postSnapshot,"empresaDePartida"));
		model.setDireccionEmpresaDePartida(tryGetData(postSnapshot,"direccionEmpresaDePartida"));
		model.setInstrucciones(tryGetData(postSnapshot,"instrucciones"));
		model.setEstadoDeOrden(tryGetData(postSnapshot,"estadoDeOrden"));
		model.setLatLngA(new LatLng(latA, lngA));
		model.setLatLngB(new LatLng(latB, lngB));
		model.setDriverAsignado(tryGetData(postSnapshot,"driverAsignado"));
		model.setDriverUid(tryGetData(postSnapshot,"driverAsignado"));
		model.setTelefonoDeClienteDeDestino(tryGetData(postSnapshot,"telefonoDeClienteDeDestino"));
		model.setCostoOrden(tryGetData(postSnapshot,"costoOrden"));
			
			ordersList.add(model);
		}
	}
			
	
		
		return ordersList;
}
	
	
	
	private String tryGetData(DataSnapshot postSnapshot,String child)
	{
		String value = "No existe o corrupto "+child;
		try{
		value = postSnapshot.child(child).getValue().toString();
		}catch(Exception e){
			Toast.makeText(context,e.toString(),1000).show();
		}
		return value;
	}
	
}

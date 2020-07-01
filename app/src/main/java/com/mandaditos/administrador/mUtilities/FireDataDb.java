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
	
	
	public List<OrderModel> getFireDataList(DataSnapshot referencedSnapshot){
		final List<OrderModel> ordersList = new ArrayList<OrderModel>();
		if (referencedSnapshot.exists())
		{
			for (DataSnapshot postSnapshot : referencedSnapshot.getChildren())
			{
				
				double latA = tryGetDataDouble(postSnapshot,"latLngA/latitude");
				double lngA = tryGetDataDouble(postSnapshot,"latLngA/longitude");
				double latB = tryGetDataDouble(postSnapshot,"latLngB/latitude");
				double lngB = tryGetDataDouble(postSnapshot,"latLngB/longitude");

				OrderModel model = new OrderModel();
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
				model.setRecibidoEnBase(tryGetDataBool(postSnapshot,"recibidoEnBase"));
				model.setTelefonoTienda(tryGetData(postSnapshot,"telefonoTienda"));
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
		}
		return value;
	}
	private Boolean tryGetDataBool(DataSnapshot postSnapshot,String child)
	{
		Boolean value = false;
		try{
			value = Boolean.valueOf(postSnapshot.child(child).getValue());
		}catch(Exception e){
		}
		return value;
	}
	private double tryGetDataDouble(DataSnapshot postSnapshot,String child)
	{
		double value = 0.0;
		try{
			value = postSnapshot.child(child).getValue();
		}catch(Exception e){
		}
		return value;
	}
	
}

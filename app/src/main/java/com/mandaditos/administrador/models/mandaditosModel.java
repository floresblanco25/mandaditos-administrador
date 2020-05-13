package com.mandaditos.administrador.models;

import com.google.android.gms.maps.model.*;

public class mandaditosModel
{
	private String Usuario;
	private String Partida;
    private String Destino;
	private String Distancia;
    private String Fecha;
	private String ETA;
    private String RecogerDineroEn;
	private String Costo;
	private String EstadoDeOrden;
	private LatLng LatLngA;
	private LatLng LatLngB;
	private String NumeroDeOrden;
	private String UserId;
	private String DriverUid;
	private String DriverAsignado;
	private String Telefono;

	public mandaditosModel(){}

    public mandaditosModel(String userId,
						   String Usuario,String Partida, String Destino, String Distancia, String Fecha, 
						   String ETA, String RecogerDineroEn, String Costo, String EstadoDeOrden,LatLng LatLngA,
						   LatLng LatLngB, String driverAsignado, String Telefono
						   ) {
        this.Partida = Partida;
        this.Destino = Destino;
		this.Distancia = Distancia;
        this.Fecha = Fecha;
		this.ETA = ETA;
        this.RecogerDineroEn = RecogerDineroEn;
		this.Costo = Costo;
		this.EstadoDeOrden=EstadoDeOrden;
		this.LatLngA=LatLngA;
		this.Usuario=Usuario;
		this.LatLngB=LatLngB;
		this.UserId=userId;
		this.DriverAsignado = driverAsignado;
		this.Telefono=Telefono;
	}

	public void setTelefono(String telefono)
	{
		Telefono = telefono;
	}

	public String getTelefono()
	{
		return Telefono;
	}

	public void setDriverAsignado(String mNombreDriver)
	{
		this.DriverAsignado = mNombreDriver;
	}

	public String getDriverAsignado()
	{
		return DriverAsignado;
	}

	public void setDriverUid(String DriverUid)
	{
		this.DriverUid = DriverUid;
	}

	public String getDriverUid()
	{
		return DriverUid;
	}


	public void setUserId(String UserId)
	{
		this.UserId = UserId;
	}

	public String getUserId()
	{
		return UserId;
	}

	public void setNumeroDeOrden(String numeroDeOrden)
	{
		NumeroDeOrden = numeroDeOrden;
	}

	public String getNumeroDeOrden()
	{
		return NumeroDeOrden;
	}

	public void setLatLngB(LatLng latLngB)
	{
		LatLngB = latLngB;
	}

	public LatLng getLatLngB()
	{
		return LatLngB;
	}


	public String getUsuario()
	{
		return Usuario;
	}

    public String getPartida() {
        return Partida;
    }

    public String getDestino() {
        return Destino;
    }
	public String getDistancia() {
        return Distancia;
    }

    public String getFecha() {
        return Fecha;
    }
	public String getEta() {
        return ETA;
    }

    public String getRecogerDineroEn() {
        return RecogerDineroEn;
    }
	public String getCosto() {
        return Costo;
    }
	public String getEstadoDeOrden(){
		return EstadoDeOrden;
	}
	public LatLng getLatLngA(){
		return LatLngA;
	}





	public void setPartida(String t){
		Partida=t;
	}

	public void setDestino(String t){
		Destino=t;
	}

	public void setDistancia(String t){
		Distancia=t;
	}
	public void setFecha(String t){
		Fecha=t;
	}
	public void setETA(String t){
		ETA=t;
	}
	public void setRecogerDineroEn(String t){
		RecogerDineroEn=t;
	}
	public void setCosto(String t){
		Costo=t;
	}

	public void setEstadoDeOrden(String t){
		EstadoDeOrden=t;
	}
	public void setLatLngA(LatLng t){
		LatLngA=t;
	}

	public void setUsuario(String t)
	{
		this.Usuario = t;
	}









}

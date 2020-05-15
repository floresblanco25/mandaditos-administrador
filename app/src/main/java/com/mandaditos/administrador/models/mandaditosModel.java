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
	private String CostoDelProducto;
	private String costoDelEnvio;
	private String EstadoDeOrden;
	private LatLng LatLngA;
	private LatLng LatLngB;
	private String NumeroDeOrden;
	private String UserId;
	private String DriverUid;
	private String DriverAsignado;
	private String Telefono;
	private String Empresa;
	private String DireccionEmpresa;
	private String InstruccionesDeLlegada;
	private String CostoOrden;
	
	public mandaditosModel(){}

    public mandaditosModel(String CostoDelEnvio, String Empresa, String DireccionDeEmpresa, String InstruccionesDeLlegada, String userId,
						   String Usuario,String Partida, String Destino, String Distancia, String Fecha, 
						   String ETA, String RecogerDineroEn, String CostoDelProducto, String EstadoDeOrden,LatLng LatLngA,
						   LatLng LatLngB, String driverAsignado, String Telefono, String CostoOrden
						   ) {
        this.Partida = Partida;
        this.Destino = Destino;
		this.Distancia = Distancia;
        this.Fecha = Fecha;
		this.ETA = ETA;
        this.RecogerDineroEn = RecogerDineroEn;
		this.CostoDelProducto = CostoDelProducto;
		this.EstadoDeOrden=EstadoDeOrden;
		this.LatLngA=LatLngA;
		this.Usuario=Usuario;
		this.LatLngB=LatLngB;
		this.UserId=userId;
		this.DriverAsignado = driverAsignado;
		this.Telefono=Telefono;
		this.Empresa=Empresa;
		this.DireccionEmpresa=DireccionDeEmpresa;
		this.InstruccionesDeLlegada=InstruccionesDeLlegada;
		this.costoDelEnvio=CostoDelEnvio;
		this.CostoOrden=CostoOrden;
	}

	public void setCostoOrden(String costoOrden)
	{
		CostoOrden = costoOrden;
	}

	public String getCostoOrden()
	{
		return CostoOrden;
	}

	public void setCostoDelEnvio(String costoDelEnvio)
	{
		this.costoDelEnvio = costoDelEnvio;
	}

	public String getCostoDelEnvio()
	{
		return costoDelEnvio;
	}

	public void setEmpresa(String empresa)
	{
		Empresa = empresa;
	}

	public String getEmpresa()
	{
		return Empresa;
	}

	public void setDireccionEmpresa(String direccionEmpresa)
	{
		DireccionEmpresa = direccionEmpresa;
	}

	public String getDireccionEmpresa()
	{
		return DireccionEmpresa;
	}

	public void setInstruccionesDeLlegada(String instruccionesDeLlegada)
	{
		InstruccionesDeLlegada = instruccionesDeLlegada;
	}

	public String getInstruccionesDeLlegada()
	{
		return InstruccionesDeLlegada;
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
	public String getCostoDelProducto() {
        return CostoDelProducto;
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
	public void setCostoDelProducto(String t){
		CostoDelProducto=t;
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

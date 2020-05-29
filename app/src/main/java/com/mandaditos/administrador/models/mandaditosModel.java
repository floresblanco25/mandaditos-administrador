package com.mandaditos.administrador.models;

import com.google.android.gms.maps.model.*;

public class mandaditosModel
{
	private String Usuario;
	private String ClienteDeDestino;
    private String DireccionDeDestino;
	private String CostoDelProducto;
	private String CostoDelEnvio;
	private String EstadoDeOrden;
	private LatLng LatLngA;
	private LatLng LatLngB;
	private String NumeroDeOrden;
	private String EmpresaUserId;
	private String DriverUid;
	private String DriverAsignado;
	private String TelefonoDeClienteDeDestino;
	private String EmpresaDePartida;
	private String DireccionEmpresaDePartida;
	private String Instrucciones;
	private String CostoOrden;
	private String LiquidadoPorDriver;
	
	public mandaditosModel(){}

    public mandaditosModel(String CostoDelEnvio, String EmpresaDePartida, String DireccionEmpresaDePartida, String Instrucciones, String EmpresaUserId,
						   String Usuario,String ClienteDeDestino, String DireccionDeDestino, String CostoDelProducto, String EstadoDeOrden,LatLng LatLngA,
						   LatLng LatLngB, String driverAsignado, String TelefonoDeClienteDeDestino, String CostoOrden, String LiquidadoPorDriver
						   ) {
        this.ClienteDeDestino = ClienteDeDestino;
        this.DireccionDeDestino = DireccionDeDestino;
		this.CostoDelProducto = CostoDelProducto;
		this.EstadoDeOrden=EstadoDeOrden;
		this.LatLngA=LatLngA;
		this.Usuario=Usuario;
		this.LatLngB=LatLngB;
		this.EmpresaUserId=EmpresaUserId;
		this.DriverAsignado = driverAsignado;
		this.TelefonoDeClienteDeDestino=TelefonoDeClienteDeDestino;
		this.EmpresaDePartida=EmpresaDePartida;
		this.DireccionEmpresaDePartida=DireccionEmpresaDePartida;
		this.Instrucciones=Instrucciones;
		this.CostoDelEnvio=CostoDelEnvio;
		this.CostoOrden=CostoOrden;
		this.LiquidadoPorDriver=LiquidadoPorDriver;
	}

	public void setUsuario(String usuario)
	{
		Usuario = usuario;
	}

	public String getUsuario()
	{
		return Usuario;
	}

	public void setClienteDeDestino(String clienteDeDestino)
	{
		ClienteDeDestino = clienteDeDestino;
	}

	public String getClienteDeDestino()
	{
		return ClienteDeDestino;
	}

	public void setDireccionDeDestino(String direccionDeDestino)
	{
		DireccionDeDestino = direccionDeDestino;
	}

	public String getDireccionDeDestino()
	{
		return DireccionDeDestino;
	}

	public void setCostoDelProducto(String costoDelProducto)
	{
		CostoDelProducto = costoDelProducto;
	}

	public String getCostoDelProducto()
	{
		return CostoDelProducto;
	}

	public void setCostoDelEnvio(String costoDelEnvio)
	{
		CostoDelEnvio = costoDelEnvio;
	}

	public String getCostoDelEnvio()
	{
		return CostoDelEnvio;
	}

	public void setEstadoDeOrden(String estadoDeOrden)
	{
		EstadoDeOrden = estadoDeOrden;
	}

	public String getEstadoDeOrden()
	{
		return EstadoDeOrden;
	}

	public void setLatLngA(LatLng latLngA)
	{
		LatLngA = latLngA;
	}

	public LatLng getLatLngA()
	{
		return LatLngA;
	}

	public void setLatLngB(LatLng latLngB)
	{
		LatLngB = latLngB;
	}

	public LatLng getLatLngB()
	{
		return LatLngB;
	}

	public void setNumeroDeOrden(String numeroDeOrden)
	{
		NumeroDeOrden = numeroDeOrden;
	}

	public String getNumeroDeOrden()
	{
		return NumeroDeOrden;
	}

	public void setEmpresaUserId(String empresaUserId)
	{
		EmpresaUserId = empresaUserId;
	}

	public String getEmpresaUserId()
	{
		return EmpresaUserId;
	}

	public void setDriverUid(String driverUid)
	{
		DriverUid = driverUid;
	}

	public String getDriverUid()
	{
		return DriverUid;
	}

	public void setDriverAsignado(String driverAsignado)
	{
		DriverAsignado = driverAsignado;
	}

	public String getDriverAsignado()
	{
		return DriverAsignado;
	}

	public void setTelefonoDeClienteDeDestino(String telefonoDeClienteDeDestino)
	{
		TelefonoDeClienteDeDestino = telefonoDeClienteDeDestino;
	}

	public String getTelefonoDeClienteDeDestino()
	{
		return TelefonoDeClienteDeDestino;
	}

	public void setEmpresaDePartida(String empresaDePartida)
	{
		EmpresaDePartida = empresaDePartida;
	}

	public String getEmpresaDePartida()
	{
		return EmpresaDePartida;
	}

	public void setDireccionEmpresaDePartida(String direccionEmpresaDePartida)
	{
		DireccionEmpresaDePartida = direccionEmpresaDePartida;
	}

	public String getDireccionEmpresaDePartida()
	{
		return DireccionEmpresaDePartida;
	}

	public void setInstrucciones(String instrucciones)
	{
		Instrucciones = instrucciones;
	}

	public String getInstrucciones()
	{
		return Instrucciones;
	}

	public void setCostoOrden(String costoOrden)
	{
		CostoOrden = costoOrden;
	}

	public String getCostoOrden()
	{
		return CostoOrden;
	}

	public void setLiquidadoPorDriver(String liquidadoPorDriver)
	{
		LiquidadoPorDriver = liquidadoPorDriver;
	}

	public String getLiquidadoPorDriver()
	{
		return LiquidadoPorDriver;
	}





}

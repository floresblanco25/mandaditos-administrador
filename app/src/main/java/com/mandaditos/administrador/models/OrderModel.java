package com.mandaditos.administrador.models;

import com.google.android.gms.maps.model.*;

public class OrderModel
{
	//tienda
	private String EmpresaUserId;
	private String EmpresaDePartida;
	private String DireccionEmpresaDePartida;
	private String TelefonoTienda;
	
	//usuario
	private String Usuario;
	private String ClienteDeDestino;
    private String DireccionDeDestino;
	private String TelefonoDeClienteDeDestino;
	
	//driver
	private String DriverUid;
	private String DriverAsignado;
	private String LiquidadoPorDriver;
	private String TelefonoDriver;
	
	//orden
	private String CostoDelProducto;
	private String CostoDelEnvio;
	private String EstadoDeOrden;
	private String NumeroDeOrden;
	private String CostoOrden;
	private Boolean RecibidoEnBase;
	private String Instrucciones;
	
	//mapas
	private LatLng LatLngA;
	private LatLng LatLngB;

	public OrderModel(String empresaUserId, String empresaDePartida, String direccionEmpresaDePartida, String telefonoTienda, String usuario, String clienteDeDestino, String direccionDeDestino, String telefonoDeClienteDeDestino, String driverUid, String driverAsignado, String liquidadoPorDriver, String telefonoDriver, String costoDelProducto, String costoDelEnvio, String estadoDeOrden, String numeroDeOrden, String costoOrden, Boolean recibidoEnBase, String instrucciones, LatLng latLngA, LatLng latLngB)
	{
		this.EmpresaUserId = empresaUserId;
		this.EmpresaDePartida = empresaDePartida;
		this.DireccionEmpresaDePartida = direccionEmpresaDePartida;
		this.TelefonoTienda = telefonoTienda;
		this.Usuario = usuario;
		this.ClienteDeDestino = clienteDeDestino;
		this.DireccionDeDestino = direccionDeDestino;
		this.TelefonoDeClienteDeDestino = telefonoDeClienteDeDestino;
		this.DriverUid = driverUid;
		this.DriverAsignado = driverAsignado;
		this.LiquidadoPorDriver = liquidadoPorDriver;
		this.TelefonoDriver = telefonoDriver;
		this.CostoDelProducto = costoDelProducto;
		this.CostoDelEnvio = costoDelEnvio;
		this.EstadoDeOrden = estadoDeOrden;
		this.NumeroDeOrden = numeroDeOrden;
		this.CostoOrden = costoOrden;
		this.RecibidoEnBase = recibidoEnBase;
		this.Instrucciones = instrucciones;
		this.LatLngA = latLngA;
		this.LatLngB = latLngB;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public OrderModel(){}


	public void setTelefonoTienda(String telefonoTienda)
	{
		TelefonoTienda = telefonoTienda;
	}

	public String getTelefonoTienda()
	{
		return TelefonoTienda;
	}

	public void setRecibidoEnBase(Boolean recibidoEnBase)
	{
		RecibidoEnBase = recibidoEnBase;
	}

	public Boolean getRecibidoEnBase()
	{
		return RecibidoEnBase;
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

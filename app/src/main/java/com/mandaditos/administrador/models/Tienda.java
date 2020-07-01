package com.mandaditos.administrador.models;

public class Tienda {

    private String Nombre;

	private String UserId;
	
	private String Address;
	
	private String Telefono;

	public Tienda(String nombre, String userId, String address, String telefono)
	{
		Nombre = nombre;
		UserId = userId;
		Address = address;
		Telefono = telefono;
	}


    public Tienda() {
    }

	public void setNombre(String nombre)
	{
		Nombre = nombre;
	}

	public String getNombre()
	{
		return Nombre;
	}

	public void setUserId(String userId)
	{
		UserId = userId;
	}

	public String getUserId()
	{
		return UserId;
	}

	public void setAddress(String address)
	{
		Address = address;
	}

	public String getAddress()
	{
		return Address;
	}

	public void setTelefono(String telefono)
	{
		Telefono = telefono;
	}

	public String getTelefono()
	{
		return Telefono;
	}


}

package com.mandaditos.administrador.models;

public class mUser {

    private String Nombre;

	private String mUserId;
	
	private String address;

    public mUser() {
    }

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getAddress()
	{
		return address;
	}

	public void setmUserId(String mUserId)
	{
		this.mUserId = mUserId;
	}

	public String getmUserId()
	{
		return mUserId;
	}

	public void setNombre(String n)
	{
		Nombre = n;
	}

	public String getNombre()
	{
		return Nombre;
	}

}

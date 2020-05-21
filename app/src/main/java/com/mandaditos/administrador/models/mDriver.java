package com.mandaditos.administrador.models;

public class mDriver {

    private String NombreDriver;

	private String mDriverId;
	
	private String telefonlDriver;


    public mDriver() {
    }

	public void setTelefonlDriver(String telefonlDriver)
	{
		this.telefonlDriver = telefonlDriver;
	}

	public String getTelefonlDriver()
	{
		return telefonlDriver;
	}


	public void setmDriverId(String mDriverId)
	{
		this.mDriverId = mDriverId;
	}

	public String getmDriverId()
	{
		return mDriverId;
	}

	public void setNombreDriver(String n)
	{
		NombreDriver = n;
	}

	public String getNombreDriver()
	{
		return NombreDriver;
	}

}

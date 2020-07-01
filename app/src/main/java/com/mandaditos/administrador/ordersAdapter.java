package com.mandaditos.administrador;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.support.v7.widget.*;
import android.view.*;
import android.view.View.*;
import android.view.ViewGroup.*;
import android.view.animation.*;
import android.view.inputmethod.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.google.android.gms.maps.model.*;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import com.mandaditos.administrador.models.*;
import java.util.*;

import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

public class ordersAdapter extends RecyclerView.Adapter<mViewHolder>
{

//Initialize
    private Context mContext;
    private List<OrderModel> mDataList;
	String[] statuses = { "Sin Completar", "Completada"};  
	String[] dondeRecoger = { "Partida", "Destino"};
	private ArrayList<String> DriversUIdList,DriversListNames;
	static LatLng latLngA,latLngB;
	private String asignedDriverName;
	private DatabaseReference mDataBase;

//Constructor
    ordersAdapter(Context mContext, List< OrderModel > mDataList)
	{
        this.mContext = mContext;
        this.mDataList = mDataList;
    }
	
	







//To acces from mappicker
	public static void setLatLng(String partidaODetino, LatLng latLng)
	{
		if (partidaODetino.matches("partida"))
		{
			latLngA = latLng;
		}
		if (partidaODetino.matches("destino"))
		{
			latLngB = latLng;
		}

    }

//Nothing

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row, parent, false);
        return new mViewHolder(mView);
    }







//Bind
    @Override
    public void onBindViewHolder(final mViewHolder holder, final int pos)
	{
		latLngA = mDataList.get(pos).getLatLngA();
		latLngB = mDataList.get(pos).getLatLngB();
		holder.ClienteDeDestinoEd.setText(mDataList.get(pos).getClienteDeDestino());
		holder.DestinoEd.setText(mDataList.get(pos).getDireccionDeDestino());
		holder.CostoDelProductoEd.setText(mDataList.get(pos).getCostoDelProducto());
		holder.CostoDelEnvioEd.setText(mDataList.get(pos).getCostoDelEnvio());
		holder.EstadoDeOrdenEd.setText(mDataList.get(pos).getEstadoDeOrden());
		holder.NumeroDeOrdenEd.setText(mDataList.get(pos).getNumeroDeOrden());
		holder.DriverAsignado.setText(mDataList.get(pos).getDriverAsignado());
		holder.callEd.setText(mDataList.get(pos).getTelefonoDeClienteDeDestino());
		holder.EmpresaEd.setText(mDataList.get(pos).getEmpresaDePartida());
		holder.direccionEmpresaEd.setText(mDataList.get(pos).getDireccionEmpresaDePartida());
		holder.InstruccionesEd.setText(mDataList.get(pos).getInstrucciones());
		holder.CostoTotalTv.setText(mDataList.get(pos).getCostoOrden());
		holder.chckbxRecogido.setChecked(mDataList.get(pos).getRecibidoEnBase());
		holder.TiendaPhoneEd.setText(mDataList.get(pos).getTelefonoTienda());
		holder.number.setText(pos+1+"");
		holder.DriverAsignado.setEnabled(true);
		holder.NumeroDeOrdenEd.setEnabled(false);
		holder.ClienteDeDestinoEd.setEnabled(false);
		holder.DestinoEd.setEnabled(false);
		holder.CostoDelProductoEd.setEnabled(false);
		holder.CostoDelEnvioEd.setEnabled(false);
		holder.EstadoDeOrdenEd.setEnabled(false);
		holder.save.setEnabled(false);
		holder.AssignarDriverButton.setEnabled(false);
		holder.DriverAsignado.setEnabled(false);
		holder.PartidaBt.setEnabled(false);
		holder.DestinoBt.setEnabled(false);
		holder.callEd.setEnabled(false);
		holder.EmpresaEd.setEnabled(false);
		holder.direccionEmpresaEd.setEnabled(false);
		holder.InstruccionesEd.setEnabled(false);
		holder.chckbxRecogido.setEnabled(false);
		holder.TiendaPhoneEd.setEnabled(false);
		
		
		
		
		
		
	
//Hide view for not me 
		if(!holder.uId.matches("bTn7vklJZGhVYa2tnPlDZKStwEi2")){
			holder.AssignarDriverButton.setVisibility(View.GONE);
			holder.SpinnerEstadoDeOrden.setVisibility(View.GONE);
			holder.deleteButton.setVisibility(View.GONE);
			holder.direccionEmpresaEd.setEnabled(false);
			holder.EmpresaEd.setEnabled(false);
			holder.llamarTienda.setVisibility(View.GONE);
			holder.whatsappTienda.setVisibility(View.GONE);
			holder.callDriver.setVisibility(View.GONE);
			holder.WhatsappDriver.setVisibility(View.GONE);

		}
		
		
		

		float CostProdNum=Float.parseFloat(mDataList.get(pos).getCostoDelProducto());
		float CostEnvNum=Float.parseFloat(mDataList.get(pos).getCostoDelEnvio());
		float resultadoDeProdMasEnv = CostProdNum + CostEnvNum;
		holder.CostoTotalTv.setText(String.valueOf(Float.toString(resultadoDeProdMasEnv)));




		ArrayAdapter statsAdapter = new ArrayAdapter(holder.context, android.R.layout.simple_spinner_item, statuses);  
        statsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		holder.SpinnerEstadoDeOrden.setEnabled(false);
        holder.SpinnerEstadoDeOrden.setAdapter(statsAdapter);  
		ArrayAdapter whereAdapter = new ArrayAdapter(holder.context, android.R.layout.simple_spinner_item, dondeRecoger);  
        statsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  

		
		
		
		
		
		
		
//FOR adding drivers list uid to spinner
		mDataBase = FirebaseDatabase.getInstance().getReference("Drivers");
		mDataBase.addListenerForSingleValueEvent(new ValueEventListener(){


				@Override
				public void onDataChange(DataSnapshot p1)
				{
					if (p1.exists())
					{
						DriversUIdList = new ArrayList<String>();
						DriversListNames = new ArrayList<String>();
						for (DataSnapshot postSnapshot : p1.getChildren())
						{
							String driver = postSnapshot.getKey().toString();
							String DriverName = postSnapshot.child("Perfil/nombre").getValue().toString();
							DriversUIdList.add(driver);
							DriversListNames.add(DriverName.toString());
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









//Obtenemos nombre por uid
		mDataBase = FirebaseDatabase.getInstance().getReference("Drivers/" + mDataList.get(pos).getDriverAsignado() + "/" + "Perfil").child("nombre");
		mDataBase.addListenerForSingleValueEvent(new ValueEventListener(){


				@Override
				public void onDataChange(DataSnapshot p1)
				{
					if (p1.exists())
					{
						holder.DriverName.setText(p1.getValue().toString());
						
					}
					else
					{}
				}
				@Override
				public void onCancelled(DatabaseError p1)
				{
				}
			});




















//llamar
		holder.llamar.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					String phone = holder.callEd.getText().toString();
					holder.context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null)));
				}
			});
		holder.callDriver.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					String phone = mDataList.get(pos).getTelefonoDeClienteDeDestino().toString();
					holder.context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null)));
				}
			});
		holder.llamarTienda.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					String phone = holder.TiendaPhoneEd.getText().toString();
					holder.context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null)));
				}
			});
			
			
			
			
			
			
			
//whatsapp 
		holder.whatsapp.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_BROWSABLE);
					intent.setData(Uri.parse("https://wa.me/503"+mDataList.get(pos).getTelefonoDeClienteDeDestino().toString()+"?text=Buen%20dia,%20le%20informo%20que%20su%20paquete%20de%20parte%20de%20"+mDataList.get(pos).getEmpresaDePartida().toString()+"%20está%20en%20ruta%20.%20Att.%20Mario%20Mandaditos."));
					holder.context.startActivity(intent);
				}
			});
		holder.whatsappTienda.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_BROWSABLE);
					intent.setData(Uri.parse("https://wa.me/503"+mDataList.get(pos).getTelefonoTienda().toString()+"?text=Buen%20dia,%20me%20podría%20brindar%20ayuda%20con%20el%20cliente%20"+mDataList.get(pos).getClienteDeDestino().toString()+"%20de%20"+mDataList.get(pos).getDireccionDeDestino()+"%20%20att.%20mensajero%20"+holder.DriverName.getText().toString()));
					holder.context.startActivity(intent);
				}
			});











//Spinner recoger dinero default









//Spinner estado de orden default
		if (mDataList.get(pos).getEstadoDeOrden().matches(statuses[0]))
		{
			holder.SpinnerEstadoDeOrden.setSelection(0);
			holder.EstadoDeOrdenEd.setTextColor(Color.RED);
		}
		if (mDataList.get(pos).getEstadoDeOrden().matches(statuses[1]))
		{
			holder.SpinnerEstadoDeOrden.setSelection(1);
			holder.EstadoDeOrdenEd.setTextColor(Color.BLUE);
		}










//spinner estado de orden
		holder.SpinnerEstadoDeOrden.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int position, long p4)
				{
					String selected = statuses[position];
					holder.EstadoDeOrdenEd.setText(selected);
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1)
				{

				}
			});




















//Guardar boton
		holder.save.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{

					String costoDelProducto = holder.CostoDelProductoEd.getText().toString();
					String costoDelEnvio = holder.CostoDelEnvioEd.getText().toString();
					if (costoDelProducto.isEmpty() | costoDelEnvio.isEmpty())
					{
						Toast.makeText(holder.context, "Ingresa el costo del producto y envío, si ya esta cobrado ingresa 0", 1000).show();
					}if (!costoDelProducto.isEmpty() && !costoDelEnvio.isEmpty())
					{
						final AlertDialog dialog = new AlertDialog.Builder(holder.context).create();
						dialog.setTitle("Alerta!");
						dialog.setMessage("Asegurate que los datos sean correctos");
						dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener(){

								@Override
								public void onClick(DialogInterface p1, int p2)
								{
									dialog.dismiss();
								}
							});
						dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Continuar", new DialogInterface.OnClickListener(){

								@Override
								public void onClick(DialogInterface p1, int p2)
								{
									String ordrStat = holder.EstadoDeOrdenEd.getText().toString();
									String clienteDeDestino = holder.ClienteDeDestinoEd.getText().toString();
									String destino = holder.DestinoEd.getText().toString();
									String costoDelProducto = holder.CostoDelProductoEd.getText().toString();
									String newDriverUid = holder.DriverAsignado.getText().toString();
									String telefono = holder.callEd.getText().toString();
									String empresa = holder.EmpresaEd.getText().toString();
									String direccionDeEmpresa = holder.direccionEmpresaEd.getText().toString();
									String instruccionesDeEnvio = holder.InstruccionesEd.getText().toString();
									String costoDelEnvio =holder.CostoDelEnvioEd.getText().toString();
									float CostProdNum=Float.parseFloat(costoDelProducto);
									float CostEnvNum=Float.parseFloat(costoDelEnvio);
									float resultadoDeProdMasEnv = CostProdNum + CostEnvNum;
									holder.CostoTotalTv.setText(String.valueOf(Float.toString(resultadoDeProdMasEnv)));
									String telefonoTienda = holder.TiendaPhoneEd.getText().toString();


									String costoOrden =String.valueOf(Float.toString(resultadoDeProdMasEnv));

									DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Ordenes").child(mDataList.get(pos).getNumeroDeOrden());
									mDatabase.child("estadoDeOrden").setValue(ordrStat);
									mDatabase.child("clienteDeDestino").setValue(clienteDeDestino);
									mDatabase.child("direccionDeDestino").setValue(destino);
									mDatabase.child("telefonoDeClienteDeDestino").setValue(telefono);
									mDatabase.child("costoDelProducto").setValue(costoDelProducto);
									mDatabase.child("costoDelEnvio").setValue(costoDelEnvio);
									mDatabase.child("latLngA").setValue(latLngA);
									mDatabase.child("latLngB").setValue(latLngB);
									mDatabase.child("driverAsignado").setValue(newDriverUid);
									mDatabase.child("empresaDePartida").setValue(empresa);
									mDatabase.child("direccionEmpresaDePartida").setValue(direccionDeEmpresa);
									mDatabase.child("instrucciones").setValue(instruccionesDeEnvio);
									mDatabase.child("costoOrden").setValue(costoOrden);
									mDatabase.child("telefonoTienda").setValue(telefonoTienda);

									holder.ClienteDeDestinoEd.setEnabled(false);
									holder.DestinoEd.setEnabled(false);
									holder.CostoDelProductoEd.setEnabled(false);
									holder.CostoDelEnvioEd.setEnabled(false);
									holder.save.setEnabled(false);
									holder.SpinnerEstadoDeOrden.setEnabled(false);
									holder.AssignarDriverButton.setEnabled(false);
									holder.PartidaBt.setEnabled(false);
									holder.DestinoBt.setEnabled(false);
									holder.callEd.setEnabled(false);
									holder.EmpresaEd.setEnabled(false);
									holder.direccionEmpresaEd.setEnabled(false);
									holder.InstruccionesEd.setEnabled(false);
									holder.TiendaPhoneEd.setEnabled(false);
									Toast.makeText(holder.context, "Guardado y enviado", Toast.LENGTH_LONG).show();

								}
							});
						dialog.show();
					}

				}

			});











//Edit button
		holder.edit.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					holder.ClienteDeDestinoEd.setEnabled(true);
					holder.DestinoEd.setEnabled(true);
					holder.CostoDelProductoEd.setEnabled(true);
					holder.CostoDelEnvioEd.setEnabled(true);
					holder.save.setEnabled(true);
					holder.SpinnerEstadoDeOrden.setEnabled(true);
					holder.AssignarDriverButton.setEnabled(true);
					holder.PartidaBt.setEnabled(true);
					holder.DestinoBt.setEnabled(true);
					holder.callEd.setEnabled(true);
					holder.callEd.setEnabled(true);
					holder.InstruccionesEd.setEnabled(true);
					holder.TiendaPhoneEd.setEnabled(true);
					if(holder.uId.matches("bTn7vklJZGhVYa2tnPlDZKStwEi2")){
						holder.direccionEmpresaEd.setEnabled(true);
						holder.EmpresaEd.setEnabled(true);

					}
					if(!holder.uId.matches("bTn7vklJZGhVYa2tnPlDZKStwEi2")){
						holder.direccionEmpresaEd.setEnabled(false);
						holder.EmpresaEd.setEnabled(false);

					}

				}
			});











//Mapa partida
		holder.PartidaBt.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					Intent i = new Intent(mContext, mapPicker.class);
					Bundle b = new Bundle();
					b.putParcelable("latLng", mDataList.get(pos).getLatLngA());
					b.putString("partidaODestino", "partida");
					i.putExtras(b);
					mContext.startActivity(i);

				}
			});











//mapa destino 
		holder.DestinoBt.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					Uri gmmIntentUri = Uri.parse("geo:0,0?q="+Uri.parse(mDataList.get(pos).getDireccionDeDestino()));
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
											   gmmIntentUri);
					holder.context.startActivity(intent);

				}
			});











//unfold button
		holder.unfoldButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if (holder.layoutToCollapse.getVisibility() == View.GONE)
					{
						expand(holder.layoutToCollapse);
						holder.unfoldButton.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.baseline_expand_less_black_24));
					}
					else
					{
						if (!(holder.layoutToCollapse.getVisibility() == View.GONE))
						{
							collapse(holder.layoutToCollapse);
							holder.unfoldButton.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.baseline_expand_more_black_24));
						}
					}
				}
			});











//asignar button
		holder.AssignarDriverButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{

					String[] drivers = GetStringArray(DriversUIdList);
					String[] names = GetStringArray(DriversListNames);
//		solo necesuto arreglar la lista de nlmbres 
					AlertDialog.Builder builder = new AlertDialog.Builder(holder.context);
					builder.setAdapter(new mSpinnerAdapter(holder.context, names, drivers), null);
					builder.setTitle("Lista de Drivers");
					builder.setItems(drivers, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								final String selectedUId = DriversUIdList.get(which);
								//aqui obtenemos el nombre del usuario
								DatabaseReference refByUid = FirebaseDatabase.getInstance().getReference("Drivers/" + selectedUId + "/Perfil").child("nombre");
								refByUid.addListenerForSingleValueEvent(new ValueEventListener() {

										@Override
										public void onDataChange(DataSnapshot dataSnapshot)
										{
											String nombre = dataSnapshot.getValue(String.class);
											holder.DriverAsignado.setText(selectedUId);
											asignedDriverName = nombre;
											holder.DriverName.setText(nombre);

										}

										@Override
										public void onCancelled(DatabaseError databaseError)
										{

										}
									});



							}
						});

					AlertDialog dialog = builder.create();
					dialog.show();
				}
			});












//Delete order
		holder.deleteButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					{
						final AlertDialog dialog = new AlertDialog.Builder(holder.context).create();
						dialog.setTitle("Borrado!");
						dialog.setMessage("Borraras esta orden " + mDataList.get(pos).getNumeroDeOrden());
						dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener(){

								@Override
								public void onClick(DialogInterface p1, int p2)
								{
									dialog.dismiss();
								}
							});
						dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Eliminar", new DialogInterface.OnClickListener(){

								@Override
								public void onClick(DialogInterface p1, int p2)
								{
									DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
									Query applesQuery = ref.child("Ordenes").orderByKey().equalTo(mDataList.get(pos).getNumeroDeOrden());

									applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
											@Override
											public void onDataChange(DataSnapshot dataSnapshot)
											{
												for (DataSnapshot appleSnapshot: dataSnapshot.getChildren())
												{
													appleSnapshot.getRef().removeValue();

													if (holder.uId.matches("bTn7vklJZGhVYa2tnPlDZKStwEi2")){
														holder.context.startActivity(new Intent(holder.context, Home.class));
													}if (!holder.uId.matches("bTn7vklJZGhVYa2tnPlDZKStwEi2")){
														holder.context.startActivity(new Intent(holder.context, HomeClient.class));
													}
												}
											}

											@Override
											public void onCancelled(DatabaseError databaseError)
											{
												Toast.makeText(holder.context, databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
											}
										});
								}
							});
						dialog.show();

					}
				}
			});
			
			
			




    }









//Get stringarray
	public static String[] GetStringArray(ArrayList<String> arr) 
    { 
        String str[] = new String[arr.size()]; 
        for (int j = 0; j < arr.size(); j++)
		{ 
            str[j] = arr.get(j); 
        } 

        return str; 

    } 
//total calc
	private float grandTotal(List<CostoPorOrden> items)
	{
		float totalPrice = 0;
		for (int i = 0 ; i < items.size(); i++)
		{
			totalPrice += items.get(i).getPrecioDeOrden(); 
		}

		return totalPrice;
	}












//Expand and collapse
	public static void expand(final View v)
	{
		int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
		int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
		final int targetHeight = v.getMeasuredHeight();
		v.getLayoutParams().height = 1;
		v.setVisibility(View.VISIBLE);
		Animation a = new Animation()
		{
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t)
			{
				v.getLayoutParams().height = interpolatedTime == 1
                    ? LayoutParams.WRAP_CONTENT
                    : (int)(targetHeight * interpolatedTime);
				v.requestLayout();
			}

			@Override
			public boolean willChangeBounds()
			{
				return true;
			}
		};
		a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
		v.startAnimation(a);
	}













//expand
	public static void collapse(final View v)
	{
		final int initialHeight = v.getMeasuredHeight();

		Animation a = new Animation()
		{
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t)
			{
				if (interpolatedTime == 1)
				{
					v.setVisibility(View.GONE);
				}
				else
				{
					v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
					v.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds()
			{
				return true;
			}
		};
		a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
		v.startAnimation(a);
	}












//nothing
    @Override
    public int getItemCount()
	{
        return mDataList.size();
    }
}


















//Class virwholder
class mViewHolder extends RecyclerView.ViewHolder
{

    EditText NumeroDeOrdenEd,ClienteDeDestinoEd,DestinoEd,CostoDelProductoEd,EstadoDeOrdenEd,DriverAsignado,EmpresaEd,direccionEmpresaEd,InstruccionesEd,CostoDelEnvioEd,TiendaPhoneEd;
	Button save,edit,PartidaBt,DestinoBt,AssignarDriverButton,llamar,whatsapp,callDriver,WhatsappDriver,whatsappTienda,llamarTienda;
	Spinner SpinnerEstadoDeOrden;
	ImageView unfoldButton,deleteButton;;
	LinearLayout layoutToCollapse;
	TextView DriverName,CostoTotalTv,number;
	EditText callEd;

	Context context;

	FirebaseAuth mFirebaseAuth;

	String uId;
	CheckBox chckbxRecogido;


    mViewHolder(View v)
	{
        super(v);

		NumeroDeOrdenEd = v.findViewById(R.id.dashboarOrderTitle);
		ClienteDeDestinoEd = v.findViewById(R.id.dashboardAddressA);
		DestinoEd = v.findViewById(R.id.dashboardAddressB);
		CostoDelProductoEd = v.findViewById(R.id.costodelproducto);
		CostoDelEnvioEd = v.findViewById(R.id.costodelenvio);
		CostoTotalTv = v.findViewById(R.id.totalCostSum);
		EstadoDeOrdenEd = v.findViewById(R.id.dashboardOrderStatus);
		save = v.findViewById(R.id.orderrowButtonGuardar);
		edit = v.findViewById(R.id.orderrowButtonEditar);
		SpinnerEstadoDeOrden = v.findViewById(R.id.orderStatus_rowSpinner);
		PartidaBt = v.findViewById(R.id.orderrowButtonPartida);
		DestinoBt = v.findViewById(R.id.orderrowButtonDestino);
		unfoldButton = v.findViewById(R.id.orderrowUnfoldButtom);
		layoutToCollapse = v.findViewById(R.id.orderRowLayoutToCollapse);
		DriverAsignado = v.findViewById(R.id.DriverAsignadodashboard);
		AssignarDriverButton = v.findViewById(R.id.AsignarDriverOrderrow);
		DriverName = v.findViewById(R.id.DriverNameTextView);
		deleteButton = v.findViewById(R.id.deleteorderrowImageView1);
		llamar = v.findViewById(R.id.llamarorderpoolrowButton1);
		callEd = v.findViewById(R.id.CelulardashboardAddressA);
		EmpresaEd = v.findViewById(R.id.empresaEd);
		direccionEmpresaEd = v.findViewById(R.id.direccionEmpresaEd);
		InstruccionesEd = v.findViewById(R.id.instruccionesEd);
		whatsapp = v.findViewById(R.id.whatsapporderrowButton1);
		number = v.findViewById(R.id.numberorderrowTextView1);
		callDriver = v.findViewById(R.id.callDriverorderrowButton1);
		WhatsappDriver = v.findViewById(R.id.whatsappDriverorderrowButton1);
		context = v.getContext();
		mFirebaseAuth = FirebaseAuth.getInstance();
		FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
		uId = mFirebaseUser.getUid().toString();
		CostoDelProductoEd.setImeOptions(EditorInfo.IME_ACTION_DONE);
		CostoDelEnvioEd.setImeOptions(EditorInfo.IME_ACTION_DONE);
		chckbxRecogido = v.findViewById(R.id.orderrowCheckBoxRecolecta);
		TiendaPhoneEd = v.findViewById(R.id.orderrowTiendaPhoneEd);
		whatsappTienda = v.findViewById(R.id.orderrowWhatsappTienda);
		llamarTienda = v.findViewById(R.id.orderrowCallTienda);
		
		



    }
	
	








}


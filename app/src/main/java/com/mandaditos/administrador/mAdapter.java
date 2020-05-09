package com.mandaditos.administrador;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.ViewGroup.*;
import android.view.animation.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.google.android.gms.maps.model.*;
import com.google.firebase.database.*;
import com.mandaditos.administrador.models.*;
import java.util.*;

import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

public class mAdapter extends RecyclerView.Adapter<mViewHolder>
{

//Initialize
    private Context mContext;
    private List<mandaditosModel> mDataList;
	String[] statuses = { "Sin Completar", "Completada"};  
	String[] dondeRecoger = { "Partida", "Destino"};
	private ArrayList<String> DriversList;
	static LatLng latLngA,latLngB;

	private DatabaseReference mDataBase;
	

//Constructor
    mAdapter(Context mContext, List< mandaditosModel > mDataList)
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
    public void onBindViewHolder(final mViewHolder holder, final int position)
	{
		latLngA = mDataList.get(position).getLatLngA();
		latLngB = mDataList.get(position).getLatLngB();
		holder.PartidaEd.setText(mDataList.get(position).getPartida());
		holder.DestinoEd.setText(mDataList.get(position).getDestino());
		holder.DistanciaEd.setText(mDataList.get(position).getDistancia());
		holder.FechaEtaEd.setText(mDataList.get(position).getFecha() + " " + mDataList.get(position).getEta());
		holder.DondeRecogerDineroEd.setText(mDataList.get(position).getRecogerDineroEn());
		holder.CostoEd.setText(mDataList.get(position).getCosto());
		holder.EstadoDeOrdenEd.setText(mDataList.get(position).getEstadoDeOrden());
		holder.NumeroDeOrdenEd.setText(mDataList.get(position).getNumeroDeOrden());
		holder.DriverAsignado.setText(mDataList.get(position).getDriverAsignado());
		holder.DriverAsignado.setEnabled(true);
		holder.NumeroDeOrdenEd.setEnabled(false);
		holder.PartidaEd.setEnabled(false);
		holder.DestinoEd.setEnabled(false);
		holder.DistanciaEd.setEnabled(false);
		holder.FechaEtaEd.setEnabled(false);
		holder.DondeRecogerDineroEd.setEnabled(false);
		holder.CostoEd.setEnabled(false);
		holder.EstadoDeOrdenEd.setEnabled(false);
		holder.save.setEnabled(false);
		holder.AssignarDriverButton.setEnabled(false);
		holder.DriverAsignado.setEnabled(false);
		holder.PartidaBt.setEnabled(false);
		holder.DestinoBt.setEnabled(false);
		ArrayAdapter statsAdapter = new ArrayAdapter(holder.context, android.R.layout.simple_spinner_item, statuses);  
        statsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		holder.SpinnerEstadoDeOrden.setEnabled(false);
        holder.SpinnerEstadoDeOrden.setAdapter(statsAdapter);  
		ArrayAdapter whereAdapter = new ArrayAdapter(holder.context, android.R.layout.simple_spinner_item, dondeRecoger);  
        statsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		holder.SpinnerDondeRecogerDinero.setEnabled(false);
        holder.SpinnerDondeRecogerDinero.setAdapter(whereAdapter);
		
		mDataBase = FirebaseDatabase.getInstance().getReference("Drivers");
		mDataBase.addValueEventListener(new ValueEventListener(){


				@Override
				public void onDataChange(DataSnapshot p1)
				{
					if (p1.exists())
					{
						DriversList = new ArrayList<String>();
						for (DataSnapshot postSnapshot : p1.getChildren())
						{
							String driver = postSnapshot.getKey().toString();
							DriversList.add(driver);

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
			

//Spinner recoger dinero default
		if (mDataList.get(position).getRecogerDineroEn().matches(dondeRecoger[0]))
		{
			holder.SpinnerDondeRecogerDinero.setSelection(0);
		}
		if (mDataList.get(position).getRecogerDineroEn().matches(dondeRecoger[1]))
		{
			holder.SpinnerDondeRecogerDinero.setSelection(1);
		}
//Spinner estado de orden default
		if (mDataList.get(position).getEstadoDeOrden().matches(statuses[0]))
		{
			holder.SpinnerEstadoDeOrden.setSelection(0);
		}
		if (mDataList.get(position).getEstadoDeOrden().matches(statuses[1]))
		{
			holder.SpinnerEstadoDeOrden.setSelection(1);
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
//Spinner donde recoger el dinero
		holder.SpinnerDondeRecogerDinero.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int pos, long p4)
				{
					String selected = dondeRecoger[pos];
					holder.DondeRecogerDineroEd.setText(selected);
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
								String partida = holder.PartidaEd.getText().toString();
								String destino = holder.DestinoEd.getText().toString();
								String distancia = holder.DistanciaEd.getText().toString();
								String recogerdinero = holder.DondeRecogerDineroEd.getText().toString();
								String costo = holder.CostoEd.getText().toString();
								String newDriverUid = holder.DriverAsignado.getText().toString();
								DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Ordenes").child(mDataList.get(position).getNumeroDeOrden());
								mDatabase.child("estadoDeOrden").setValue(ordrStat);
								mDatabase.child("partida").setValue(partida);
								mDatabase.child("destino").setValue(destino);
								mDatabase.child("distancia").setValue(distancia);
								mDatabase.child("recogerDineroEn").setValue(recogerdinero);
								mDatabase.child("costo").setValue(costo);
								mDatabase.child("latLngA").setValue(latLngA);
								mDatabase.child("latLngB").setValue(latLngB);
								mDatabase.child("driverAsignado").setValue(newDriverUid);
								holder.PartidaEd.setEnabled(false);
								holder.DestinoEd.setEnabled(false);
								holder.DistanciaEd.setEnabled(false);
								holder.FechaEtaEd.setEnabled(false);
								holder.DondeRecogerDineroEd.setEnabled(false);
								holder.CostoEd.setEnabled(false);
								holder.save.setEnabled(false);
								holder.SpinnerEstadoDeOrden.setEnabled(false);
								holder.SpinnerDondeRecogerDinero.setEnabled(false);
								holder.AssignarDriverButton.setEnabled(false);
								holder.PartidaBt.setEnabled(false);
								holder.DestinoBt.setEnabled(false);
							}
						});
					dialog.show();

				}

			});
//Edit button
		holder.edit.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					holder.PartidaEd.setEnabled(true);
					holder.DestinoEd.setEnabled(true);
					holder.DistanciaEd.setEnabled(true);
					holder.FechaEtaEd.setEnabled(true);
					holder.DondeRecogerDineroEd.setEnabled(true);
					holder.CostoEd.setEnabled(true);
					holder.save.setEnabled(true);
					holder.SpinnerEstadoDeOrden.setEnabled(true);
					holder.SpinnerDondeRecogerDinero.setEnabled(true);
					holder.AssignarDriverButton.setEnabled(true);
					holder.PartidaBt.setEnabled(true);
					holder.DestinoBt.setEnabled(true);
				}
			});
//Mapa partida
		holder.PartidaBt.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					Intent i = new Intent(mContext, mapPicker.class);
					Bundle b = new Bundle();
					b.putParcelable("latLng", mDataList.get(position).getLatLngA());
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
					Intent i = new Intent(mContext, mapPicker.class);
					Bundle b = new Bundle();
					b.putParcelable("latLng", mDataList.get(position).getLatLngB());
					b.putString("partidaODestino", "destino");
					i.putExtras(b);
					mContext.startActivity(i);

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
					
					AlertDialog.Builder builder = new AlertDialog.Builder(holder.context);
					builder.setTitle("Elige un Driver");
					String[] drivers = GetStringArray(DriversList);
					builder.setItems(drivers, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								final String selected = DriversList.get(which);
								//aqui obtenemos el nombre del usuario
								DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Drivers/" + selected + "/Perfil").child("nombre");
								ref.addListenerForSingleValueEvent(new ValueEventListener() {
										@Override
										public void onDataChange(DataSnapshot dataSnapshot) {
											String t = dataSnapshot.getValue(String.class);
											holder.DriverAsignado.setText(selected);
											holder.DriverName.setText(t);

										}

										@Override
										public void onCancelled(DatabaseError databaseError) {

										}
									});
								
								

							}
						});

					AlertDialog dialog = builder.create();
					dialog.show();
				}
			});
			
			


    }
	
	
	
	
	
	
	
//Get stringarray
	public static String[] GetStringArray(ArrayList<String> arr) 

    { 



        // declaration and initialise String Array 

        String str[] = new String[arr.size()]; 



        // ArrayList to Array Conversion 

        for (int j = 0; j < arr.size(); j++) { 



            // Assign each value to String array 

            str[j] = arr.get(j); 

        } 



        return str; 

    } 
	
	
	
	
	
	
	
	
	
	

//Expand and collapse
	public static void expand(final View v)
	{
		int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
		int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
		final int targetHeight = v.getMeasuredHeight();

		// Older versions of android (pre API 21) cancel animations for views with a height of 0.
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

		// Expansion speed of 1dp/ms
		a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
		v.startAnimation(a);
	}

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

		// Collapse speed of 1dp/ms
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

    EditText NumeroDeOrdenEd,PartidaEd,DestinoEd,DistanciaEd,FechaEtaEd,DondeRecogerDineroEd,CostoEd,EstadoDeOrdenEd,DriverAsignado;
	Button save,edit,PartidaBt,DestinoBt,AssignarDriverButton;
	Spinner SpinnerEstadoDeOrden,SpinnerDondeRecogerDinero;
	ImageView unfoldButton;
	LinearLayout layoutToCollapse;
	TextView DriverName;
	Context context;


    mViewHolder(View v)
	{
        super(v);

		NumeroDeOrdenEd = v.findViewById(R.id.dashboarOrderTitle);
		PartidaEd = v.findViewById(R.id.dashboardAddressA);
		DestinoEd = v.findViewById(R.id.dashboardAddressB);
		DistanciaEd = v.findViewById(R.id.dashboardDistance);
		FechaEtaEd = v.findViewById(R.id.dashboardDateEta);
		DondeRecogerDineroEd = v.findViewById(R.id.dashboardWhereGetMoney);
		CostoEd = v.findViewById(R.id.dashboardTotalCost);
		EstadoDeOrdenEd = v.findViewById(R.id.dashboardOrderStatus);
		save = v.findViewById(R.id.orderrowButtonGuardar);
		edit = v.findViewById(R.id.orderrowButtonEditar);
		SpinnerEstadoDeOrden = v.findViewById(R.id.orderStatus_rowSpinner);
		SpinnerDondeRecogerDinero = v.findViewById(R.id.where_rowSpinner);
		PartidaBt = v.findViewById(R.id.orderrowButtonPartida);
		DestinoBt = v.findViewById(R.id.orderrowButtonDestino);
		unfoldButton = v.findViewById(R.id.orderrowUnfoldButtom);
		layoutToCollapse = v.findViewById(R.id.orderRowLayoutToCollapse);
		DriverAsignado = v.findViewById(R.id.DriverAsignadodashboard);
		AssignarDriverButton = v.findViewById(R.id.AsignarDriverOrderrow);
		DriverName = v.findViewById(R.id.DriverNameTextView);
		context = v.getContext();





    }








}


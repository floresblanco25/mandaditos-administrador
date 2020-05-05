package com.mandaditos.administrador;

import android.content.*;
import android.support.v7.widget.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.google.firebase.database.*;
import com.mandaditos.administrador.models.*;
import java.util.*;
import android.app.*;

public class mAdapter extends RecyclerView.Adapter<mViewHolder>
{

    private Context mContext;
    private List<mandaditosModel> mDataList;

    mAdapter(Context mContext, List< mandaditosModel > mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row, parent, false);
        return new mViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final mViewHolder holder, final int position) {
		holder.PartidaEd.setText(mDataList.get(position).getPartida());
		holder.DestinoEd.setText(mDataList.get(position).getDestino());
		holder.DistanciaEd.setText(mDataList.get(position).getDistancia());
		holder.FechaEtaEd.setText(mDataList.get(position).getFecha()+" "+mDataList.get(position).getEta());
		holder.DondeRecogerDineroEd.setText(mDataList.get(position).getRecogerDineroEn());
		holder.CostoEd.setText(mDataList.get(position).getCosto());
		holder.EstadoDeOrdenEd.setText(mDataList.get(position).getEstadoDeOrden());
		holder.NumeroDeOrdenEd.setText(mDataList.get(position).getNumeroDeOrden());
		holder.NumeroDeOrdenEd.setEnabled(false);
		holder.PartidaEd.setEnabled(false);
		holder.DestinoEd.setEnabled(false);
		holder.DistanciaEd.setEnabled(false);
		holder.FechaEtaEd.setEnabled(false);
		holder.DondeRecogerDineroEd.setEnabled(false);
		holder.CostoEd.setEnabled(false);
		holder.EstadoDeOrdenEd.setEnabled(false);
		holder.save.setEnabled(false);
		
		
		
		
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
								DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Ordenes").child(mDataList.get(position).getNumeroDeOrden());
								mDatabase.child("estadoDeOrden").setValue(ordrStat);
								mDatabase.child("partida").setValue(partida);
								mDatabase.child("destino").setValue(destino);
								mDatabase.child("distancia").setValue(distancia);
								mDatabase.child("recogerDineroEn").setValue(recogerdinero);
								mDatabase.child("costo").setValue(costo);
								holder.PartidaEd.setEnabled(false);
								holder.DestinoEd.setEnabled(false);
								holder.DistanciaEd.setEnabled(false);
								holder.FechaEtaEd.setEnabled(false);
								holder.DondeRecogerDineroEd.setEnabled(false);
								holder.CostoEd.setEnabled(false);
								holder.EstadoDeOrdenEd.setEnabled(false);
								holder.save.setEnabled(false);
							}
						});
						dialog.show();

				}

			});
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
					holder.EstadoDeOrdenEd.setEnabled(true);
					holder.save.setEnabled(true);
				}
			});
			
    }
	
	
    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}

class mViewHolder extends RecyclerView.ViewHolder {

    EditText NumeroDeOrdenEd,PartidaEd,DestinoEd,DistanciaEd,FechaEtaEd,DondeRecogerDineroEd,CostoEd,EstadoDeOrdenEd;
	Button save,edit;
	Context context;

    mViewHolder(View v) {
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
		context = v.getContext();

    }
	
	







}


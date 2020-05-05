package com.mandaditos.administrador;

import android.content.*;
import android.support.v7.widget.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.google.firebase.database.*;
import com.mandaditos.administrador.models.*;
import java.util.*;

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
		
		holder.save.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					String ordrStat = holder.EstadoDeOrdenEd.getText().toString();
					DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Ordenes").child(mDataList.get(position).getNumeroDeOrden());
					mDatabase.child("estadoDeOrden").setValue(ordrStat);
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
	Button save;
	private Context context;

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
		context = v.getContext();

    }







}


package com.mandaditos.administrador.adapters;

import android.app.*;
import android.view.*;
import android.widget.*;
import android.widget.CompoundButton.*;
import com.google.firebase.database.*;
import com.mandaditos.administrador.*;
import com.mandaditos.administrador.models.*;
import java.util.*;

import com.mandaditos.administrador.R;

public class checklistAdapter extends BaseAdapter
{

	private Activity context_1;
	private ArrayList<OrderModel> ordersList;

	public checklistAdapter(Activity context,ArrayList<OrderModel> ordersList) {
		context_1 = context;
		this.ordersList = ordersList;
	}

	@Override
	public int getCount() {
		return ordersList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View v, ViewGroup parent) {
		ViewHolder vh = null;

		if (v == null) {
			v = LayoutInflater.from(context_1).inflate(R.layout.checklist_row, null);
			vh = new ViewHolder();
			vh.txt = v.findViewById(R.id.checklistRowText1);
			vh.chkbox = v.findViewById(R.id.checklistrowCheckBox1);
			vh.save= v.findViewById(R.id.checklistactivitysave);
			v.setTag(vh);
		} else {
			vh = (ViewHolder) v.getTag();
		}
		
		
		
		vh.txt.setText(position+1+" -"+ ordersList.get(position).getClienteDeDestino()+"\n"
		+ordersList.get(position).getEmpresaDePartida());
		
		
		
		//in some cases, it will prevent unwanted situations
		vh.chkbox.setOnCheckedChangeListener(null);

			

		vh.chkbox.setChecked(ordersList.get(position).getRecibidoEnBase());
		
		vh.chkbox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

				@Override
				public void onCheckedChanged(CompoundButton p1, boolean p2)
				{
					DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Ordenes").child(ordersList.get(position).getNumeroDeOrden());
					mDatabase.child("recibidoEnBase").setValue(p2);
					ordersList.get(position).setRecibidoEnBase(p2);
				}
			});
		
			
			
		


		return v;
	}
	

	public class ViewHolder {
		public TextView txt;
		public CheckBox chkbox;
		public Button save;

	}
	
}

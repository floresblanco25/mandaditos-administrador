package com.mandaditos.administrador.adapters;

import android.app.*;
import android.view.*;
import android.widget.*;
import com.mandaditos.administrador.*;
import java.util.*;
import com.mandaditos.administrador.models.*;
import android.graphics.*;

public class simpleListAdapter extends BaseAdapter
 {

	private Activity context_1;

	private ArrayList<mandaditosModel> ordersList;
	private boolean espago;

	public simpleListAdapter(Activity context,
							 ArrayList<mandaditosModel> ordersList,boolean espago) {
		context_1 = context;
		this.ordersList = ordersList;
		this.espago=espago;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;

		if (convertView == null) {
			convertView = LayoutInflater.from(context_1).inflate(
				R.layout.sumar_simple_row, null);
			viewHolder = new ViewHolder();
			viewHolder.txt = convertView
				.findViewById(R.id.text1);
			/**
			 * At very first time when the List View row Item control's
			 * instance is created it will be store in the convertView as a
			 * ViewHolder Class object for the reusability purpose
			 **/
			convertView.setTag(viewHolder);
		} else {
			/**
			 * Once the instance of the row item's control it will use from
			 * already created controls which are stored in convertView as a
			 * ViewHolder Instance
			 * */
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if(ordersList.get(position).getEstadoDeOrden().equalsIgnoreCase("Completada")){
			viewHolder.txt.setBackgroundColor(Color.GREEN);
			viewHolder.txt.setText("✓ "+ordersList.get(position).getClienteDeDestino()+" $"+ordersList.get(position).getCostoOrden());
			
			
			if(espago==true){
				viewHolder.txt.setBackgroundColor(Color.GREEN);
				viewHolder.txt.setText("✓ "+ordersList.get(position).getClienteDeDestino()+" $"+(Float.valueOf(ordersList.get(position).getCostoDelEnvio())-1));
			}
		}else{
			viewHolder.txt.setBackgroundColor(Color.WHITE);
		viewHolder.txt.setText("× "+ordersList.get(position).getClienteDeDestino()+" $"+ordersList.get(position).getCostoOrden());
			if(espago==true){
				viewHolder.txt.setBackgroundColor(Color.WHITE);
				viewHolder.txt.setText("× "+ordersList.get(position).getClienteDeDestino()+" $"+(Float.valueOf(ordersList.get(position).getCostoDelEnvio())-1));
			}
		}
		return convertView;
	}

	public class ViewHolder {
		public TextView txt;

	}
}

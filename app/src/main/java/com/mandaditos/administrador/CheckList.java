package com.mandaditos.administrador;
import android.os.*;
import android.support.v7.app.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.google.firebase.database.*;
import com.mandaditos.administrador.adapters.*;
import com.mandaditos.administrador.mUtilities.*;
import com.mandaditos.administrador.models.*;
import java.util.*;

public class CheckList extends AppCompatActivity
{
	ListView mListView;
	TextView countTv;
	private List<OrderModel> ordersList;
	private FireDataDb fireData;
	EditText searchEd;
	@Override
	protected void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		setContentView(R.layout.checklist_activity);
		mListView = findViewById(R.id.checklistactivityListView1);
		countTv = findViewById(R.id.checklistactivityCount);
		fireData = new FireDataDb();
		searchEd = findViewById(R.id.checklistactivityEditText1);
		
		
		
		
		
		
		
		
		
		filterEmpresa("");
		
		
		
		
		
		
		
		
//Buscar
		searchEd.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void afterTextChanged(Editable editable) {
					filterEmpresa(editable.toString());
				}
			});
		
		
		
		
		
		
		
	}
	
	
	
	
	
//SHOW LIST
	public void filterEmpresa(final String txt){
		final ArrayList<OrderModel> filteredOrdersListFinal = new ArrayList<OrderModel>();
		DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Ordenes");
		mRef.addListenerForSingleValueEvent(new ValueEventListener(){

				@Override
				public void onDataChange(DataSnapshot refSnapshot)
				{
					ordersList = fireData.getFireDataList(refSnapshot);
					for (OrderModel order : ordersList) {
						if (order.getEmpresaDePartida().toLowerCase().contains(txt.toLowerCase())) {
							filteredOrdersListFinal.add(order);
						} 

					}
					if (filteredOrdersListFinal.size() > 0) {
						Collections.sort(filteredOrdersListFinal, new Comparator<OrderModel>() {
								@Override
								public int compare(final OrderModel object1, final OrderModel object2) {
									return object1.getClienteDeDestino().compareTo(object2.getClienteDeDestino());
								}
							});
					}
					final checklistAdapter adptr = new checklistAdapter(CheckList.this,filteredOrdersListFinal);
					mListView.setAdapter(adptr);
					int countAll = 0;
					if (adptr != null){
						countAll = adptr.getCount();

					}
					countTv.setText(countAll+"");
					

					}

				@Override
				public void onCancelled(DatabaseError p1)
				{
				}
			});

		
	}
	
	
	
}

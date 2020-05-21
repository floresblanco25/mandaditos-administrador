package com.mandaditos.administrador;
import android.content.*;
import android.view.*;
import android.widget.*;
import com.mandaditos.administrador.*;

class mSpinnerAdapter extends BaseAdapter
{

	private CharSequence[] titles;
	private CharSequence[] susbtitles;
	private Context mContext;

	mSpinnerAdapter(Context mContext, String[] titles, String[] subtitles)
	{
		this.mContext = mContext;
		this.titles = titles;
		this.susbtitles = subtitles;
	}



	@Override
	public int getCount() 
	{
		return titles.length;
	}

	@Override
	public Object getItem(int position) 
	{
		//this isn't great
		return titles[position];
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if (convertView == null)
		{
			LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.drivers_list_row, null);
		}

		TextView a = convertView.findViewById(R.id.text1);
		a.setText(titles[position]);
		TextView b= convertView.findViewById(R.id.text2);
		b.setText(susbtitles[position]);


		return convertView;
	}

}

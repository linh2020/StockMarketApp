package edu.sjsu.stocksearch;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SAdapter extends ArrayAdapter<SASymbol> {
    private int resourceId;
    class ViewHolder {
        TextView symbol;
        TextView price;
        TextView changeString;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SASymbol fav = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new SAdapter.ViewHolder();
            viewHolder.symbol = (TextView) view.findViewById(R.id.favSymbol);
            viewHolder.price = (TextView) view.findViewById(R.id.favPrice);
            viewHolder.changeString = (TextView) view.findViewById(R.id.favChange);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.symbol.setText(fav.getSymbolName());
        viewHolder.price.setText(fav.getPrice());
        viewHolder.changeString.setText(fav.getChangeString());

        if (fav.getChangeFloat() >= 0) {
            viewHolder.changeString.setTextColor(Color.GREEN);
        } else {
            viewHolder.changeString.setTextColor(Color.RED);
        }
        return view;
    }

    public SAdapter(Context context, int textViewResourceId, List<SASymbol> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
}

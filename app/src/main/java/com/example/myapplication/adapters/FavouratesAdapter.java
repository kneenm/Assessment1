package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.classes.favourites;
import com.example.myapplication.classes.names;

import java.util.List;

public class FavouratesAdapter extends BaseAdapter{

    private final Context mContext;
    private List<String> mNames;

    public FavouratesAdapter(Context context, List<String> names) {
        mContext = context;
        mNames = names;
    }

    static class ViewHolder {
        public TextView appName;
        public TextView appColour;
    }

    @Override
    public int getCount() {
        return mNames.size();
    }

    @Override
    public Object getItem(int position) {
        return mNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mNames.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppsAdapter.ViewHolder holder;

        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
            holder = new AppsAdapter.ViewHolder();
            holder.appName = (TextView) convertView.findViewById(R.id.list_item_name);
            holder.appColour = (TextView) convertView.findViewById(R.id.list_item_colour);

            convertView.setTag(holder);

        } else {
            holder = (AppsAdapter.ViewHolder) convertView.getTag();
        }


        holder.appName.setText(mNames.get(position));
        holder.appColour.setText("");

        return convertView;
    }
}

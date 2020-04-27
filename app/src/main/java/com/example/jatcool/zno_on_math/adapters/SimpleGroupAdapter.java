package com.example.jatcool.zno_on_math.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.entity.Group;
import com.example.jatcool.zno_on_math.entity.Theme;

import java.util.List;

public class SimpleGroupAdapter extends ArrayAdapter<String> {
    private LayoutInflater inflater;
    private int layout;
    private List<String> simples;

    public SimpleGroupAdapter(Context context, int resource, List<String> simples){
        super(context,resource,simples);
        this.simples = simples;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(this.layout,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.simple.setText(simples.get(position));

        return convertView;
    }

    private class ViewHolder {
        final TextView simple;

        ViewHolder(View view){
            simple = view.findViewById(R.id.textView27);
        }
    }
}

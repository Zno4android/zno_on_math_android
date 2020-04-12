package com.example.jatcool.zno_on_math.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.entity.Statistics;

import org.w3c.dom.Text;

import java.util.List;

public class ProfileListAdapter extends ArrayAdapter<Statistics> {

    private LayoutInflater inflater;
    private int layout;
    private List<Statistics> statistics;

    public ProfileListAdapter(Context context, int resource, List<Statistics> statistics){
        super(context,resource,statistics);
        this.statistics = statistics;
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

        Statistics st = statistics.get(position);

        viewHolder.name_test.setText(st.getTest().getName());
        viewHolder.corect_answer.setText(String.valueOf(st.getResult()));
        viewHolder.isPassed.setText(st.getResult()>=50.00?"Склав":"Не склав");

        return convertView;
    }

    private class ViewHolder{
        final TextView name_test;
        final TextView corect_answer;
        final TextView isPassed;

        ViewHolder(View view){
             name_test = view.findViewById(R.id.Name_Test_list);
             corect_answer = view.findViewById(R.id.Corect_proc);
             isPassed = view.findViewById(R.id.isPassed_list);
        }
    }
}


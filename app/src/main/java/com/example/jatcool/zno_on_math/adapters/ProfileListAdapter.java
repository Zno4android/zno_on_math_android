package com.example.jatcool.zno_on_math.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.entity.Statistics;

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

        View view = inflater.inflate(this.layout,parent,false);
        TextView name_test = view.findViewById(R.id.Name_Test_list);
        TextView corect_answer = view.findViewById(R.id.Corect_proc);
        TextView isPassed = view.findViewById(R.id.isPassed_list);

        Statistics st = statistics.get(position);

        name_test.setText(st.getTest().getName());
        corect_answer.setText(String.valueOf(st.getResult()));
        isPassed.setText(st.getResult()>=50.00?"Склав":"Не склав");

        return view;
    }
}

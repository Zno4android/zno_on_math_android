package com.example.jatcool.zno_on_math.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.entity.Question;

import java.util.List;

public class DetailStatisticAdapter extends ArrayAdapter<Question> {
    private LayoutInflater inflater;
    private int layout;
    private List<Question> questions;

    public DetailStatisticAdapter(Context context, int resource, List<Question> questions){
        super(context,resource,questions);
        this.questions = questions;
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

        Question q = questions.get(position);

        viewHolder.question_number.setText((position+15)+"");
        viewHolder.isCorrect.setText(q.getResult() ? "Правильно" : "Неправильно");

        return convertView;
    }

    private class ViewHolder{
        final TextView question_number;
        final TextView isCorrect;


        ViewHolder(View view){
            question_number = view.findViewById(R.id.questionName_DetailList);
            isCorrect = view.findViewById(R.id.questionScore_DetailList);
        }
    }
}

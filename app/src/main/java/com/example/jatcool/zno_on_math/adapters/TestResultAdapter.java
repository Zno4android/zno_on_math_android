package com.example.jatcool.zno_on_math.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.util.Answer;

import java.util.List;

public class TestResultAdapter extends ArrayAdapter<Answer> {
    private LayoutInflater inflater;
    private int layout;
    private List<Answer> answers;

    public TestResultAdapter(Context context, int resource, List<Answer> answers) {
        super(context, resource, answers);
        this.answers = answers;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TestResultAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new TestResultAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TestResultAdapter.ViewHolder) convertView.getTag();
        }

        Answer answer = answers.get(position);

        viewHolder.question_number.setText((position + 1) + "");
        viewHolder.isCorrect.setText(answer.isCorrect() ? "Правильно" : "Неправильно");

        return convertView;
    }

    private class ViewHolder {
        final TextView question_number;
        final TextView isCorrect;


        ViewHolder(View view) {
            question_number = view.findViewById(R.id.questionName_DetailList);
            isCorrect = view.findViewById(R.id.questionScore_DetailList);
        }
    }
}

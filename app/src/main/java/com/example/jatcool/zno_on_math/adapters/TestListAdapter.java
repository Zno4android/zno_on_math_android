package com.example.jatcool.zno_on_math.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.entity.Test;

import java.util.List;

public class TestListAdapter extends ArrayAdapter<Test> {

    private LayoutInflater inflater;
    private int layout;
    private List<Test> tests;

    public TestListAdapter(Context context, int resource, List<Test> tests) {
        super(context, resource, tests);
        this.tests = tests;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Test test = tests.get(position);

        viewHolder.testName.setText(test.getName());

        return convertView;
    }

    public void remove(int index) {
        tests.remove(index);
    }

    public class ViewHolder {

        final TextView testName;

        ViewHolder(View view) {
            testName = view.findViewById(R.id.testName_test_list_view);
        }
    }
}

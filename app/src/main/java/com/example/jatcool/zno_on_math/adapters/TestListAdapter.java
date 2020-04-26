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
import com.example.jatcool.zno_on_math.entity.Statistics;
import com.example.jatcool.zno_on_math.entity.Test;

import java.text.SimpleDateFormat;
import java.util.List;

public class TestListAdapter extends ArrayAdapter<Test> {
    private LayoutInflater inflater;
    private int layout;
    private List<Test> tests;
    private List<Statistics> statistics;

    public TestListAdapter(Context context, int resource, List<Test> tests, List<Statistics> statistics) {
        super(context, resource, tests);
        this.tests = tests;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.statistics = statistics;
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

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(test.getName())
                .append(" (")
                .append(test.getTheme())
                .append(")");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Statistics statisticsOfTest : statistics) {
            if (statisticsOfTest.getTestId().equals(test.getId())) {
                stringBuilder.append("\n")
                        .append(statisticsOfTest.getResult())
                        .append("% ")
                        .append(simpleDateFormat.format(statisticsOfTest.getDate()));
                break;
            }
        }

        viewHolder.testName.setText(stringBuilder.toString());

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

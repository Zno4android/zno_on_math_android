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

import java.text.SimpleDateFormat;
import java.util.List;

public class StatisticsAdapter extends ArrayAdapter<Statistics> {
    private LayoutInflater inflater;
    private int layout;
    private List<Statistics> statistics;

    public StatisticsAdapter(Context context, int resource, List<Statistics> statistics) {
        super(context, resource, statistics);
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.statistics = statistics;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        StatisticsAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new StatisticsAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (StatisticsAdapter.ViewHolder) convertView.getTag();
        }

        Statistics st = statistics.get(position);

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("")
                .append(" (")
                .append("")
                .append(")");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        viewHolder.testName.setText(stringBuilder.toString());

        return convertView;
    }

    public void remove(int index) {
        statistics.remove(index);
    }

    public class ViewHolder {

        final TextView testName;

        ViewHolder(View view) {
            testName = view.findViewById(R.id.list_statistics);
        }
    }
}

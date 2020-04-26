package com.example.jatcool.zno_on_math.ui.statistics;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.activity.admin.TeachrStudentStatistic;
import com.example.jatcool.zno_on_math.adapters.StatisticsAdapter;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.entity.Statistics;
import com.example.jatcool.zno_on_math.entity.Test;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.STUDENT;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.TOKEN;

public class StatisticsFragment extends Fragment {
    ListView studList;
    String token;
    List<Statistics> statistics;
    List<Test> tests;
    StatisticsAdapter adapter;
    Spinner testSpinner;
    Context ctx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        SharedPreferences sh = getActivity().getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), Context.MODE_PRIVATE);
        token = sh.getString(TOKEN, "");
        studList = view.findViewById(R.id.list_statistics);
        testSpinner = view.findViewById(R.id.list_statistics_sort_by);
        ctx = getActivity();

        NetworkService.getInstance()
                .getJSONApi()
                .getAllTest(token)
                .enqueue(
                        new Callback<List<Test>>() {
                            @Override
                            public void onResponse(Call<List<Test>> call, Response<List<Test>> response) {
                                if (response.isSuccessful()) {
                                    tests = response.body();

                                    InitSpinner();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Test>> call, Throwable t) {
                                t.printStackTrace();
                            }
                        }
                );

        return view;
    }

    private void setList() {
        if (statistics.isEmpty()) {
            return;
        }

        Comparator<Statistics> comparator = new Comparator<Statistics>() {
            @Override
            public int compare(Statistics o1, Statistics o2) {
                if (o1.getDate().after(o2.getDate())) {
                    return -1;
                }

                if (o2.getDate().after(o1.getDate())) {
                    return 1;
                }

                return 0;
            }
        };

        Collections.sort(statistics, comparator);

        for (Statistics st : statistics) {
            st.getUser().setId(st.getUserId());
        }

        adapter = new StatisticsAdapter(ctx, R.layout.statistics_list_view, statistics);
        studList.setAdapter(adapter);

        studList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Statistics statistics = (Statistics) parent.getItemAtPosition(position);
                        Intent studentProfile = new Intent(ctx, TeachrStudentStatistic.class);
                        studentProfile.putExtra(STUDENT, new Gson().toJson(statistics.getUser()));
                        startActivity(studentProfile);
                    }
                }
        );
    }

    private void InitSpinner() {
        ArrayAdapter<Test> gr = new ArrayAdapter<>(ctx, R.layout.support_simple_spinner_dropdown_item, tests);
        testSpinner.setAdapter(gr);

        testSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        getStatistics();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
    }

    private void getStatistics() {
        String testId = ((Test) testSpinner.getSelectedItem()).getId();

        NetworkService.getInstance()
                .getJSONApi()
                .getStudentStatisticByTestId(token, testId)
                .enqueue(new Callback<List<Statistics>>() {
                    @Override
                    public void onResponse(Call<List<Statistics>> call, Response<List<Statistics>> response) {
                        if (response.isSuccessful()) {
                            statistics = response.body();
                            setList();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Statistics>> call, Throwable t) {

                    }
                });
    }
}

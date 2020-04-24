package com.example.jatcool.zno_on_math.ui.list_student;

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
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.activity.admin.TeachrStudentStatistic;
import com.example.jatcool.zno_on_math.adapters.StudentListAdapter;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.entity.Group;
import com.example.jatcool.zno_on_math.entity.User;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.STUDENT;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.TOKEN;

public class List_student extends Fragment {
    ListView studList;
    String token;
    List<User> mUsers;
    Group groups;
    String group, fname;
    StudentListAdapter adapter;
    SearchView fnameSearch;
    Spinner groupSpinner;
    Context ctx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_student, container, false);
        SharedPreferences sh = getActivity().getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), Context.MODE_PRIVATE);
        token = sh.getString(TOKEN, "");
        studList = view.findViewById(R.id.List_student);
        groupSpinner = view.findViewById(R.id.List_student_sort_by);
        fnameSearch = view.findViewById(R.id.List_student_search_view);
        ctx = getActivity();
        fnameSearch.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        fname = fnameSearch.getQuery().toString();
                        getStudents();
                        return false;
                    }
                }
        );

        NetworkService.getInstance()
                .getJSONApi()
                .GetGroup()
                .enqueue(
                        new Callback<Group>() {
                            @Override
                            public void onResponse(Call<Group> call, Response<Group> response) {
                                if (response.isSuccessful()) {
                                    groups = response.body();
                                    InitGroup();
                                }
                            }

                            @Override
                            public void onFailure(Call<Group> call, Throwable t) {

                            }
                        }
                );
        getStudents();

        return view;
    }

    private void setList() {
        adapter = new StudentListAdapter(ctx, R.layout.students_list_maket, mUsers);
        studList.setAdapter(adapter);

        studList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        User user = (User) parent.getItemAtPosition(position);
                        Intent studentProfile = new Intent(ctx, TeachrStudentStatistic.class);
                        studentProfile.putExtra(STUDENT, new Gson().toJson(user));
                        startActivity(studentProfile);
                    }
                }
        );
    }

    private void InitGroup() {
        ArrayAdapter<String> gr = new ArrayAdapter<>(ctx, R.layout.support_simple_spinner_dropdown_item, groups.getName());
        groupSpinner.setAdapter(gr);

        groupSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        group = (String) parent.getSelectedItem();
                        getStudents();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
    }

    private void getStudents() {
        NetworkService.getInstance()
                .getJSONApi()
                .getAllStudents(token, fname, group)
                .enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if (response.isSuccessful()) {
                            mUsers = response.body();
                            setList();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }
}

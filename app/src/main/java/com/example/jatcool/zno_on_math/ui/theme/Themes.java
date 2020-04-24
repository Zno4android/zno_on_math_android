package com.example.jatcool.zno_on_math.ui.theme;

import android.annotation.SuppressLint;
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

import androidx.fragment.app.Fragment;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.activity.admin.AddTheme;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.entity.Status;
import com.example.jatcool.zno_on_math.entity.Theme;
import com.example.jatcool.zno_on_math.ui.theory.theory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.STATUS;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.THEME;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.TOKEN;


public class Themes extends Fragment {
    FloatingActionButton addThem;
    ListView themeList;
    String status;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme, container, false);
        addThem = view.findViewById(R.id.addTheme);
        themeList = view.findViewById(R.id.theme_list);
        SharedPreferences s = getActivity().getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), Context.MODE_PRIVATE);
        status = s.getString(STATUS, "");
        if (status.equals(Status.Teacher.getName())) {
            addThem.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getActivity(), AddTheme.class));
                        }
                    }
            );
        } else {
            addThem.setVisibility(View.GONE);
        }
        NetworkService.getInstance()
                .getJSONApi()
                .getAllTheme(s.getString(TOKEN, ""))
                .enqueue(
                        new Callback<List<Theme>>() {
                            @Override
                            public void onResponse(Call<List<Theme>> call, Response<List<Theme>> response) {
                                if (response.isSuccessful()) {
                                    ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, response.body());
                                    themeList.setAdapter(adapter);

                                    themeList.setOnItemClickListener(
                                            new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    Theme theme = (Theme) parent.getItemAtPosition(position);

                                                    Intent passingTest = new Intent(getActivity(), theory.class);
                                                    passingTest.putExtra(THEME, theme.getName());
                                                    startActivity(passingTest);
                                                }
                                            }
                                    );
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Theme>> call, Throwable t) {
                                t.printStackTrace();
                            }
                        }
                );

        return view;
    }
}

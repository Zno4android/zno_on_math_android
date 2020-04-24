package com.example.jatcool.zno_on_math.ui.theme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.activity.admin.AddTheme;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.entity.Theme;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.TOKEN;


public class Themes extends Fragment {
FloatingActionButton addThem;
ListView themeList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme, container, false);
         addThem = view.findViewById(R.id.addTheme);
         themeList = view.findViewById(R.id.theme_list);
         addThem.setOnClickListener(
                 new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         startActivity(new Intent(getActivity(), AddTheme.class));
                     }
                 }
         );
        SharedPreferences s = getActivity().getSharedPreferences(ConstFile.FILE_NAME.replace(".xml",""), Context.MODE_PRIVATE);

        NetworkService.getInstance()
                .getJSONApi()
                .getAllTheme(s.getString(TOKEN, ""))
                .enqueue(
                        new Callback<List<Theme>>() {
                            @Override
                            public void onResponse(Call<List<Theme>> call, Response<List<Theme>> response) {
                                if(response.isSuccessful()){
                                    ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item, response.body());
                                    themeList.setAdapter(adapter);
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

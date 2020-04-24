package com.example.jatcool.zno_on_math.ui.theory;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.entity.Status;
import com.example.jatcool.zno_on_math.entity.Theme;
import com.example.jatcool.zno_on_math.entity.Theoretics;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.STATUS;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.TOKEN;


public class theory extends Fragment {

    List<Theoretics> mTheoretics = new ArrayList<>();
    int position = 0;
    Button nextBtn, deleteBtn, backBtn, editBtn;
    String token, status, currentID;
    TextView theme, themeName, text, theoryName;
     Spinner themeSpinner;
     String themeString;
     Context ctx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theory, container, false);
        themeSpinner = view.findViewById(R.id.spinner);
        nextBtn = view.findViewById(R.id.Next);
        ctx = getActivity();
        deleteBtn = view.findViewById(R.id.Delete);
        backBtn = view.findViewById(R.id.Back);
        editBtn = view.findViewById(R.id.Edit);
        theoryName = view.findViewById(R.id.TheoryName);
        theme = view.findViewById(R.id.Theme);
        text = view.findViewById(R.id.Theory);
        SharedPreferences pr = getActivity().getSharedPreferences(ConstFile.FILE_NAME.replace(".xml",""), Context.MODE_PRIVATE);
        token = pr.getString(TOKEN, "");
        status = pr.getString(STATUS, "");
        if(status.equals(Status.Student.getName())) {
            deleteBtn.setVisibility(View.GONE);
            editBtn.setVisibility(View.GONE);
        }
        getTheme();

        return view;
    }

    private void SetBtnClickListener(){

        deleteBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     NetworkService.getInstance()
                             .getJSONApi()
                             .deleteTheory(token, currentID)
                             .enqueue(
                                     new Callback<Theoretics>() {
                                         @Override
                                         public void onResponse(Call<Theoretics> call, Response<Theoretics> response) {
                                             if(response.isSuccessful()){
                                                 Toast.makeText(getContext(), "Успішно", Toast.LENGTH_LONG)
                                                         .show();
                                                 getTheory();
                                             }
                                         }

                                         @Override
                                         public void onFailure(Call<Theoretics> call, Throwable t) {

                                         }
                                     }
                             );
                    }
                }
        );

        nextBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(position<mTheoretics.size()-1){
                            position++;
                            theoryName.setText(mTheoretics.get(position).getName());
                            theme.setText(mTheoretics.get(position).getTheme());
                            text.setText(mTheoretics.get(position).getText());
                            currentID = mTheoretics.get(position).getId();
                        }
                    }
                }
        );

        backBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(position>0){
                            position--;
                            theoryName.setText(mTheoretics.get(position).getName());
                            theme.setText(mTheoretics.get(position).getTheme());
                            text.setText(mTheoretics.get(position).getText());
                            currentID = mTheoretics.get(position).getId();
                        }
                    }
                }
        );
    }

    private void getTheme(){
        NetworkService.getInstance()
                .getJSONApi()
                .getAllTheme(token)
                .enqueue(
                        new Callback<List<Theme>>() {
                            @Override
                            public void onResponse(Call<List<Theme>> call, Response<List<Theme>> response) {
                                if(response.isSuccessful()){
                                    initThemeSpinner(response);
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Theme>> call, Throwable t) {

                            }
                        }
                );
    }

    private void initThemeSpinner(Response<List<Theme>> response){
        ArrayAdapter<String> adapter = new ArrayAdapter(ctx,R.layout.support_simple_spinner_dropdown_item,response.body());
        themeSpinner.setAdapter(adapter);

        themeSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Theme t = (Theme)parent.getSelectedItem();
                        themeString = t.getName();
                        position = 0;
                        getTheory();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
    }

private void getTheory(){
    NetworkService.getInstance()
            .getJSONApi()
            .getTheory(token,themeString)
            .enqueue(new Callback<List<Theoretics>>() {
                @Override
                public void onResponse(Call<List<Theoretics>> call, Response<List<Theoretics>> response) {

                    if(response.isSuccessful()){
                        mTheoretics = response.body();
                        theoryName.setText(mTheoretics.get(position).getName());
                        theme.setText(mTheoretics.get(position).getTheme());
                        text.setText(mTheoretics.get(position).getText());
                        currentID = mTheoretics.get(position).getId();
                        SetBtnClickListener();
                    }
                }

                @Override
                public void onFailure(Call<List<Theoretics>> call, Throwable t) {
                    Toast.makeText(getContext(),"Немає з'єднання з інтернетом",Toast.LENGTH_LONG);
                }
            });
}

}

package com.example.jatcool.zno_on_math.ui.theory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.jatcool.zno_on_math.activity.admin.EditTheory;
import com.example.jatcool.zno_on_math.adapters.SimpleAdapterTheme;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.entity.Status;
import com.example.jatcool.zno_on_math.entity.Theme;
import com.example.jatcool.zno_on_math.entity.Theoretics;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.STATUS;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.THEORY;
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
    public void onResume() {
        super.onResume();
        getTheme();
    }

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

        editBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!currentID.equals("")) {
                            Intent edit = new Intent(ctx, EditTheory.class);
                            edit.putExtra(THEORY,  new Gson().toJson(mTheoretics.get(position)));
                            startActivity(edit);
                        }
                    }
                }
        );

        deleteBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mTheoretics.size()>0){
                            AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
                            alert.setTitle("Підтверждення");
                            alert.setMessage("Ви впевпненні що хочете видалити?");
                            alert.setPositiveButton("Так", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    NetworkService.getInstance()
                                            .getJSONApi()
                                            .deleteTheory(token, currentID)
                                            .enqueue(
                                                    new Callback<String>() {
                                                        @Override
                                                        public void onResponse(Call<String> call, Response<String> response) {
                                                            if (response.isSuccessful()) {
                                                                Toast.makeText(getContext(), "Успішно", Toast.LENGTH_LONG)
                                                                        .show();
                                                                getTheory();
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<String> call, Throwable t) {

                                                        }
                                                    }
                                            );
                                }
                            });
                            alert.setNegativeButton("Ні",null);
                            alert.setCancelable(false);
                            alert.show();

                        }
                        else {
                            Toast.makeText(ctx,"Нічого видаляти!",Toast.LENGTH_LONG)
                                    .show();
                        }
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
        SimpleAdapterTheme adapter = new SimpleAdapterTheme(ctx,R.layout.simple_list_view,response.body());
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
                        if(!mTheoretics.isEmpty()) {
                            position = 0;
                            theoryName.setText(mTheoretics.get(position).getName());
                            theme.setText(mTheoretics.get(position).getTheme());
                            text.setText(mTheoretics.get(position).getText());
                            currentID = mTheoretics.get(position).getId();
                            SetBtnClickListener();
                        }
                        else {
                            theoryName.setText("");
                            theme.setText("");
                            text.setText("За цією темою немає теорії");
                            currentID = "";
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Theoretics>> call, Throwable t) {
                    Toast.makeText(getContext(),"Немає з'єднання з інтернетом",Toast.LENGTH_LONG);
                }
            });
}

}

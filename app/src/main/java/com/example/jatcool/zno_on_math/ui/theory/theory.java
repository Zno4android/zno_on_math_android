package com.example.jatcool.zno_on_math.ui.theory;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.entity.Theoretics;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.TOKEN;


public class theory extends Fragment {

    List<Theoretics> mTheoretics = new ArrayList<>();
    int position = 0;
    Button nextBtn, deleteBtn, backBtn, editBtn;
    String token;
    TextView theme, themeName, text, theoryName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theory, container, false);

        nextBtn = view.findViewById(R.id.Next);
        deleteBtn = view.findViewById(R.id.Delete);
        backBtn = view.findViewById(R.id.Back);
        editBtn = view.findViewById(R.id.Edit);
        theoryName = view.findViewById(R.id.TheoryName);
        theme = view.findViewById(R.id.Theme);
        text = view.findViewById(R.id.Theory);

        SharedPreferences pr = getActivity().getSharedPreferences(ConstFile.FILE_NAME.replace(".xml",""), Context.MODE_PRIVATE);
        token = pr.getString(TOKEN, "");
        NetworkService.getInstance()
                .getJSONApi()
                .getTheory(token)
                .enqueue(new Callback<List<Theoretics>>() {
                    @Override
                    public void onResponse(Call<List<Theoretics>> call, Response<List<Theoretics>> response) {

                        if(response.isSuccessful()){
                            mTheoretics = response.body();
                            theoryName.setText(mTheoretics.get(position).getThemeName());
                            theme.setText(mTheoretics.get(position).getTheme());
                            text.setText(mTheoretics.get(position).getText());
                            SetBtnClickListener();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Theoretics>> call, Throwable t) {
                        Toast.makeText(getContext(),"Немає з'єднання з інтернетом",Toast.LENGTH_LONG);
                    }
                });

        return view;
    }

    private void SetBtnClickListener(){
        nextBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(position<mTheoretics.size()-1){
                            position++;
                            theoryName.setText(mTheoretics.get(position).getThemeName());
                            theme.setText(mTheoretics.get(position).getTheme());
                            text.setText(mTheoretics.get(position).getText());
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
                            theoryName.setText(mTheoretics.get(position).getThemeName());
                            theme.setText(mTheoretics.get(position).getTheme());
                            text.setText(mTheoretics.get(position).getText());
                        }
                    }
                }
        );
    }


}

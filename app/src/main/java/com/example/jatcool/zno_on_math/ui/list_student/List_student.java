package com.example.jatcool.zno_on_math.ui.list_student;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.adapters.StudentListAdapter;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.entity.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class List_student extends Fragment {
ListView studList;
String token;
List<User> mUsers;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_student, container, false);
        SharedPreferences sh = getActivity().getSharedPreferences(ConstFile.FILE_NAME.replace(".xml",""), Context.MODE_PRIVATE);
        token = sh.getString("token","");
        studList = view.findViewById(R.id.List_student);
        NetworkService.getInstance()
                .getJSONApi()
                .getAllStudents(token)
                .enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        Toast.makeText(getContext(),response.message(),Toast.LENGTH_LONG)
                                .show();
                        if(response.isSuccessful()){
                            Log.d("BODY",response.message());
                            mUsers = response.body();
                            setList();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                           Toast.makeText(getContext(),t.getMessage(), Toast.LENGTH_LONG)
                                   .show();
                    }
                });
        return view;
    }

    private void setList(){
        StudentListAdapter adapter = new StudentListAdapter(getActivity(),R.layout.students_list_maket,mUsers);
        studList.setAdapter(adapter);
    }
}

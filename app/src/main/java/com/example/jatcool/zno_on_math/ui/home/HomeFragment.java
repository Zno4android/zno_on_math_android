package com.example.jatcool.zno_on_math.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.activity.admin.AddTest;
import com.example.jatcool.zno_on_math.activity.user.Tests;
import com.example.jatcool.zno_on_math.adapters.TestListAdapter;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.entity.Test;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

   ListView testList;
    List<Test> tests;
    FloatingActionButton addTestBtn;
    String token;
    private HomeViewModel homeViewModel;
    Button btn;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        testList = root.findViewById(R.id.listTest);
        addTestBtn = root.findViewById(R.id.addTestFloatBtn);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        addTestBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), AddTest.class);

                        intent.putExtra("token", token);
                        startActivity(intent);
                    }
                }
        );

        NetworkService.getInstance()
                .getJSONApi()
                .getAllTest(token)
                .enqueue(
                        new Callback<List<Test>>() {
                            @Override
                            public void onResponse(Call<List<Test>> call, Response<List<Test>> response) {
                                if(response.isSuccessful()){
                                    tests = response.body();
                                    InitList();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Test>> call, Throwable t) {
                                                 t.printStackTrace();
                            }
                        }
                );

        return root;
    }

    private void InitList(){
        TestListAdapter adapter = new TestListAdapter(getActivity(),R.layout.test_list_view,tests);
        testList.setAdapter(adapter);

        testList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Test test = (Test) parent.getItemAtPosition(position);

                        Intent passingTest = new Intent(getActivity(),Tests.class);
                        passingTest.putExtra("testID", test.getId());
                        passingTest.putExtra("token", token);
                        startActivity(passingTest);
                    }
                }
        );
    }
}
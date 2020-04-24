package com.example.jatcool.zno_on_math.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.activity.admin.AddTest;
import com.example.jatcool.zno_on_math.activity.user.Tests;
import com.example.jatcool.zno_on_math.adapters.TestListAdapter;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.entity.Status;
import com.example.jatcool.zno_on_math.entity.Test;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.jatcool.zno_on_math.constants.ErrorMessageConstants.DIALOG_DELETE_TEST_ERROR;
import static com.example.jatcool.zno_on_math.constants.FragmentConstants.DELETE_TEXT;
import static com.example.jatcool.zno_on_math.constants.FragmentConstants.DIALOG_MESSAGE;
import static com.example.jatcool.zno_on_math.constants.FragmentConstants.DIALOG_NEGATIVE_TEXT;
import static com.example.jatcool.zno_on_math.constants.FragmentConstants.DIALOG_POSITIVE_TEXT;
import static com.example.jatcool.zno_on_math.constants.FragmentConstants.DIALOG_TITLE;
import static com.example.jatcool.zno_on_math.constants.FragmentConstants.EDIT_TEXT;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.STATUS;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.TEST_ID;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.TOKEN;
import static com.example.jatcool.zno_on_math.constants.SuccessMessageConstants.DIALOG_DELETE_TEST_SuCCESs;

public class HomeFragment extends Fragment {

    private ListView testList;
    private List<Test> tests;
    private FloatingActionButton addTestBtn;
    private String token;
    private Test selectedTest;
    private String status;

    @SuppressLint("RestrictedApi")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        testList = root.findViewById(R.id.listTest);
        addTestBtn = root.findViewById(R.id.addTestFloatBtn);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
        token = sharedPreferences.getString(TOKEN, "");
        status = sharedPreferences.getString(STATUS, "");
        if(status.equals(Status.Teacher.getName())) {
            addTestBtn.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), AddTest.class);

                            intent.putExtra(TOKEN, token);
                            startActivity(intent);
                        }
                    }
            );
        }
        else {
            addTestBtn.setVisibility(View.GONE);
        }

        NetworkService.getInstance()
                .getJSONApi()
                .getAllTest(token)
                .enqueue(
                        new Callback<List<Test>>() {
                            @Override
                            public void onResponse(Call<List<Test>> call, Response<List<Test>> response) {
                                if (response.isSuccessful()) {
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
            if(status.equals(Status.Teacher.getName())) {
                registerForContextMenu(testList);
            }
        return root;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        ListView listView = (ListView) v;
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        selectedTest = (Test) listView.getItemAtPosition(acmi.position);
        menu.add(Menu.NONE, 1, Menu.NONE, EDIT_TEXT);
        menu.add(Menu.NONE, 2, Menu.NONE, DELETE_TEXT);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1: {
                Intent intent = new Intent(getActivity(), AddTest.class);

                intent.putExtra(TOKEN, token);
                intent.putExtra(TEST_ID, selectedTest.getId());
                startActivity(intent);
                return true;
            }
            case 2: {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(DIALOG_TITLE);
                builder.setMessage(DIALOG_MESSAGE);
                builder.setPositiveButton(DIALOG_POSITIVE_TEXT, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                builder.setNegativeButton(DIALOG_NEGATIVE_TEXT, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NetworkService.getInstance()
                                .getJSONApi()
                                .deleteTest(token, selectedTest.getId())
                                .enqueue(
                                        new Callback<String>() {
                                            @Override
                                            public void onResponse(Call<String> call, Response<String> response) {
                                                if (response.isSuccessful()) {
                                                    Toast.makeText(getActivity(), DIALOG_DELETE_TEST_SuCCESs, Toast.LENGTH_LONG)
                                                            .show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<String> call, Throwable t) {
                                                Toast.makeText(getActivity(), DIALOG_DELETE_TEST_ERROR, Toast.LENGTH_LONG)
                                                        .show();
                                            }
                                        }
                                );
                    }
                });

                builder.show();
                return true;
            }

        }
        return super.onContextItemSelected(item);
    }

    private void InitList() {
        TestListAdapter adapter = new TestListAdapter(getActivity(), R.layout.test_list_view, tests);
        testList.setAdapter(adapter);

        testList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Test test = (Test) parent.getItemAtPosition(position);

                        Intent passingTest = new Intent(getActivity(), Tests.class);
                        passingTest.putExtra(TEST_ID, test.getId());
                        passingTest.putExtra(TOKEN, token);
                        startActivity(passingTest);
                    }
                }
        );
    }
}
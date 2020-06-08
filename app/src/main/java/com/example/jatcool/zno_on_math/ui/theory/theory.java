package com.example.jatcool.zno_on_math.ui.theory;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.activity.admin.EditTheory;
import com.example.jatcool.zno_on_math.adapters.FilesViewAdapter;
import com.example.jatcool.zno_on_math.adapters.SimpleAdapterTheme;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.constants.URLConstants;
import com.example.jatcool.zno_on_math.entity.Status;
import com.example.jatcool.zno_on_math.entity.Theme;
import com.example.jatcool.zno_on_math.entity.Theoretics;
import com.example.jatcool.zno_on_math.listeners.ItemClickSupport;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
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
    static final String FILE_DIR = "files/";
    Button nextBtn, deleteBtn, backBtn, editBtn;
    String token, status, currentID;
    TextView theme, themeName, text, theoryName;
     Spinner themeSpinner;
     String themeString;
    int positionList = 0;
    List<String> filesNames;
    LinearLayout fileLiner;
     Context ctx;
    RecyclerView filesList;

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
        FirebaseApp.initializeApp(ctx);
        LinearLayoutManager horizontal = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);
        filesList = view.findViewById(R.id.viewTheoryFilesList);
        filesList.setLayoutManager(horizontal);
        deleteBtn = view.findViewById(R.id.Delete);
        backBtn = view.findViewById(R.id.Back);
        fileLiner = view.findViewById(R.id.filesLiner);
        fileLiner.setVisibility(View.GONE);
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

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
    }

    private void SetBtnClickListener(){

        editBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!currentID.equals("")) {
                            Intent edit = new Intent(ctx, EditTheory.class);
                            edit.putExtra(THEORY, new Gson().toJson(mTheoretics.get(positionList)));
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
                                                    new Callback<JsonObject>() {
                                                        @Override
                                                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                                            if (response.isSuccessful()) {
                                                                Toast.makeText(getContext(), response.body().get("message").toString(), Toast.LENGTH_LONG)
                                                                        .show();
                                                                getTheory();
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<JsonObject> call, Throwable t) {

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
                        if (positionList < mTheoretics.size() - 1) {
                            positionList++;
                            theoryName.setText(mTheoretics.get(positionList).getName());
                            theme.setText(mTheoretics.get(positionList).getTheme());
                            text.setText(mTheoretics.get(positionList).getText());
                            currentID = mTheoretics.get(positionList).getId();
                            if (mTheoretics.get(positionList).getFiles().size() > 0) {
                                fileLiner.setVisibility(View.VISIBLE);
                                FilesViewAdapter adapter = new FilesViewAdapter(ctx, mTheoretics.get(positionList).getFiles());
                                filesList.setAdapter(adapter);
                                setOnClickListener();
                            } else {
                                fileLiner.setVisibility(View.GONE);
                            }
                        }
                    }
                }
        );

        backBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (positionList > 0) {
                            positionList--;
                            theoryName.setText(mTheoretics.get(positionList).getName());
                            theme.setText(mTheoretics.get(positionList).getTheme());
                            text.setText(mTheoretics.get(positionList).getText());
                            currentID = mTheoretics.get(positionList).getId();
                            if (mTheoretics.get(positionList).getFiles().size() > 0) {
                                fileLiner.setVisibility(View.VISIBLE);
                                FilesViewAdapter adapter = new FilesViewAdapter(ctx, mTheoretics.get(positionList).getFiles());
                                filesList.setAdapter(adapter);
                                setOnClickListener();
                            } else {
                                fileLiner.setVisibility(View.GONE);
                            }
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
                            positionList = 0;
                            theoryName.setText(mTheoretics.get(positionList).getName());
                            theme.setText(mTheoretics.get(positionList).getTheme());
                            text.setText(mTheoretics.get(positionList).getText());
                            currentID = mTheoretics.get(positionList).getId();
                            if (mTheoretics.get(positionList).getFiles().size() > 0) {
                                fileLiner.setVisibility(View.VISIBLE);
                                FilesViewAdapter adapter = new FilesViewAdapter(ctx, mTheoretics.get(positionList).getFiles());
                                filesList.setAdapter(adapter);
                                setOnClickListener();
                            } else {
                                fileLiner.setVisibility(View.GONE);
                            }
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
                    Toast.makeText(getContext(), "Немає з'єднання з інтернетом", Toast.LENGTH_LONG)
                            .show();
                }
            });
}

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        RecyclerView r = (RecyclerView) v;
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;

        menu.add(0, v.getId(), 0, "Видалити");
    }

    private void setOnClickListener() {
        ItemClickSupport.addTo(filesList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                FirebaseStorage storage = FirebaseStorage.getInstance(URLConstants.FIREBASE_URL);
                StorageReference gsReference = storage.getReferenceFromUrl(URLConstants.FIREBASE_URL + "/" + FILE_DIR + mTheoretics.get(positionList).getFiles().get(position));
                gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Intent view = new Intent(Intent.ACTION_VIEW);
                        ContentResolver cR = ctx.getContentResolver();
                        MimeTypeMap mime = MimeTypeMap.getSingleton();
                        String intentType = null;
                        try {
                            intentType = fileType(uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (intentType.isEmpty()) {
                            return;
                        }
                        String type = mime.getExtensionFromMimeType(cR.getType(uri));
                        view.setDataAndType(uri, intentType);
                        view.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Intent intent = Intent.createChooser(view, "Відкрити файл");
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(ctx, "Виникла помилка при відкриті файла", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    private String fileType(Uri uri) throws IOException {
        String filePath = uri.toString();
        if (filePath.contains("pdf")) {
            return "application/pdf";
        } else if (filePath.contains("doc") || filePath.contains("docx")) {
            return "text/plain";
        } else if (filePath.contains("zip")) {
            return "text/plain";
        }
        return "";
    }

}

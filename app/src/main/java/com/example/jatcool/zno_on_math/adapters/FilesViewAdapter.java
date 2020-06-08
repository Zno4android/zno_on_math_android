package com.example.jatcool.zno_on_math.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jatcool.zno_on_math.R;

import java.util.List;

public class FilesViewAdapter extends RecyclerView.Adapter<FilesViewAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<String> files;

    public FilesViewAdapter(Context context, List<String> files) {
        this.files = files;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.files_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String file = files.get(position);
        holder.name.setText(file);
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;

        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.textView32);
        }
    }

}


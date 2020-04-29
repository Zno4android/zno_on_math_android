package com.example.jatcool.zno_on_math.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.entity.User;

import java.util.List;

public class StudentListAdapter extends ArrayAdapter<User> {
    private LayoutInflater inflater;
    private int layout;
    private List<User> mUsers;

    public StudentListAdapter(Context context, int resource, List<User> users) {
        super(context, resource, users);
        this.mUsers = users;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        User user = mUsers.get(position);
        if (!user.getImage().isEmpty()) {
            Glide.with(convertView).load(user.getImage()).into(viewHolder.studeImg);
        }
        viewHolder.FIO.setText(user.getLastname() + " " + user.getFirstname());
        viewHolder.email.setText(user.getEmail());
        viewHolder.group.setText(user.getGroup());
        return convertView;
    }

    private class ViewHolder {
        final ImageView studeImg;
        final TextView FIO;
        final TextView group;
        final TextView email;

        ViewHolder(View view) {
            studeImg = view.findViewById(R.id.studImg);
            FIO = view.findViewById(R.id.list_student_maket_name);
            group = view.findViewById(R.id.list_student_maket_group);
            email = view.findViewById(R.id.list_student_maket_email);
        }
    }
}

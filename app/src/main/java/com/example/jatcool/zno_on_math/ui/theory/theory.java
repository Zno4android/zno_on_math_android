package com.example.jatcool.zno_on_math.ui.theory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.jatcool.zno_on_math.R;


public class theory extends Fragment {

  TextView t;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theory, container, false);
        return view;
    }


}

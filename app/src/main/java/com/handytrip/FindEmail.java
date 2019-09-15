package com.handytrip;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handytrip.Utils.AutoLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class FindEmail extends Fragment {


    public FindEmail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_find_email, container, false);
        AutoLayout.setView(v);
        return v;
    }

}

package com.app.chendurfincorp.client.tab;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.chendurfincorp.client.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Investment extends Fragment {


    public Investment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_investment, container, false);
    }

}

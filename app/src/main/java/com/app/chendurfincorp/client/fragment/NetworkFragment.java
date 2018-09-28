package com.app.chendurfincorp.client.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.card.MaterialCardView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.chendurfincorp.client.R;
import com.app.chendurfincorp.client.activity.Network1Activity;
import com.app.chendurfincorp.client.activity.Network2Activity;
import com.app.chendurfincorp.client.activity.Network3Activity;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class NetworkFragment extends Fragment {

    MaterialCardView cvNet1, cvNet2, cvNet3;

    public NetworkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_network, container, false);

        cvNet1 = view.findViewById(R.id.net1_cv);
        cvNet2 = view.findViewById(R.id.net2_cv);
        cvNet3 = view.findViewById(R.id.net3_cv);

        cvNet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Network1Activity.class));
            }
        });
        cvNet2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Network2Activity.class));
            }
        });
        cvNet3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Network3Activity.class));
            }
        });
        return view;
    }
}

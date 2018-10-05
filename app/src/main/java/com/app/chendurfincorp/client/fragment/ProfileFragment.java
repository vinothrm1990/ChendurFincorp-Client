package com.app.chendurfincorp.client.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.chendurfincorp.client.R;
import com.app.chendurfincorp.client.helper.Constants;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    TextView pUName, pId, pName, pAddress;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Constants.pref = getActivity().getApplicationContext().getSharedPreferences("CF", MODE_PRIVATE);
        Constants.editor = Constants.pref.edit();

        pId = view.findViewById(R.id.profile_id);
        pUName = view.findViewById(R.id.profile_tv);
        pName = view.findViewById(R.id.profile_name);
        pAddress = view.findViewById(R.id.profile_address);

        String id = Constants.pref.getString("id", "");
        String name = Constants.pref.getString("name", "");
        String uname = Constants.pref.getString("username", "");
        String address = Constants.pref.getString("address", "");

        pId.setText(id);
        pName.setText(name);
        pUName.setText(uname);
        pAddress.setText(address);

        return  view;
    }
}

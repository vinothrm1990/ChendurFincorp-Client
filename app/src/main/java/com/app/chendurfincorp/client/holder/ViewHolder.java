package com.app.chendurfincorp.client.holder;

import android.view.View;
import android.widget.TextView;
import com.app.chendurfincorp.client.R;

public class ViewHolder {
    public TextView mTextView;
    public ViewHolder(View view) {
        mTextView = view.findViewById(R.id.tree_tv);
    }
}

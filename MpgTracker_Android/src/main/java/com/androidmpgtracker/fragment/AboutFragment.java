package com.androidmpgtracker.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidmpgtracker.R;

public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, null);

        if(root != null) {
            ((TextView) root.findViewById(R.id.about_text)).setText(getText(R.string.about_text));
        }

        return root;
    }
}

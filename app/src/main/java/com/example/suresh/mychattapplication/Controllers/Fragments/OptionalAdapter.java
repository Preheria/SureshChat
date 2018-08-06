package com.example.suresh.mychattapplication.Controllers.Fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.suresh.mychattapplication.R;
import java.util.ArrayList;

public class OptionalAdapter extends ArrayAdapter<String> {

    private Context context;
    private int resource;
    private ArrayList<String> optionalArray;
    private String[] strings;

    public OptionalAdapter(@NonNull Context context, int resource, @NonNull String[] strings) {

        super(context, resource, strings);
        this.context=context;
        this.resource=resource;
        this.strings=strings;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater=LayoutInflater.from(context);

        //each item will be written as a view which needs to be returned
        final View view=inflater.inflate(resource,null);
        TextView tvDefault=view.findViewById(R.id.RNFTextView);
        tvDefault.setText(strings[position]);

        return view;
    }
}













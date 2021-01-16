package com.piyush025.lifeshare;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> name;
    private final ArrayList<String> address;
    private final ArrayList<String> email;

    public MyListAdapter(Activity context,ArrayList<String> name,ArrayList<String> address,ArrayList<String> email)
    {
        super(context, R.layout.mylist,name);

        this.context=context;
        this.name=name;
        this.address=address;
        this.email=email;
    }

    public View getView(int position, View view, ViewGroup parent)
    {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView bbname = (TextView) rowView.findViewById(R.id.bbname);
        TextView bbaddress = (TextView) rowView.findViewById(R.id.bbaddress);
        TextView bbemail = (TextView) rowView.findViewById(R.id.bbemail);

        bbname.setText(name.get(position));
        bbaddress.setText(address.get(position));
        bbemail.setText(email.get(position));
        return rowView;
    }
}

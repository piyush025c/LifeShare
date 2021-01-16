package com.piyush025.lifeshare;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListAdapter1 extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> name;
    private final ArrayList<String> gender;
    private final ArrayList<String> bloodgroup;
    private final ArrayList<String> dob;
    private final ArrayList<String> mob;
    private final ArrayList<String> email;

    public MyListAdapter1(Activity context,ArrayList<String> name,ArrayList<String> gender,ArrayList<String> bloodgroup,ArrayList<String> dob,ArrayList<String> mob,ArrayList<String> email) {
        super(context, R.layout.mylist1, name);

        this.context = context;
        this.name = name;
        this.gender = gender;
        this.bloodgroup = bloodgroup;
        this.dob = dob;
        this.mob = mob;
        this.email = email;
    }

        public View getView(int position, View view, ViewGroup parent)
        {
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.mylist1, null,true);

            TextView uname = (TextView) rowView.findViewById(R.id.uname);
            TextView ugender = (TextView) rowView.findViewById(R.id.ugender);
            TextView ubloodgroup = (TextView) rowView.findViewById(R.id.ubloodgroup);
            TextView udob = (TextView) rowView.findViewById(R.id.udob);
            TextView umob = (TextView) rowView.findViewById(R.id.umob);
            TextView uemail = (TextView) rowView.findViewById(R.id.uemail);


            uname.setText(name.get(position));
            ugender.setText(gender.get(position));
            ubloodgroup.setText(bloodgroup.get(position));
            udob.setText(dob.get(position));
            umob.setText(mob.get(position));
            uemail.setText(email.get(position));


            return rowView;
        }

}

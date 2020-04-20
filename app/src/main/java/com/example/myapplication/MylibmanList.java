package com.example.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MylibmanList extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    private ListView remindersList;



    HashSet<String> selectedBooks = new HashSet<String>();

    //This listener will be used on all your checkboxes, there's no need to
    //create a listener for every checkbox.
    CompoundButton.OnCheckedChangeListener checkChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            String SelRem = (String) remindersList.getTag();
            if (b) {
                selectedBooks.add(SelRem);
            } else {
                selectedBooks.remove(SelRem);
            }
        }
    };


    public MylibmanList(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    static class ViewHolder {
        TextView id;
        TextView content;
        TextView important;
        CheckBox check;
        View viewColor;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_listview, null);

            holder = new ViewHolder();
            holder.id = (TextView) convertView.findViewById(R.id.id_list);
            holder.content = (TextView) convertView.findViewById(R.id.content_list);
            holder.important = (TextView) convertView.findViewById(R.id.important_id);
            holder.check = (CheckBox) convertView.findViewById(R.id.checkbox);
            holder.viewColor = (View) convertView.findViewById(R.id.color_item);

            holder.check.setOnCheckedChangeListener(checkChangedListener);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        HashMap<String, String> reminder = new HashMap<String, String>();
        reminder = (HashMap<String, String>) getItem(position);

        holder.check.setTag(reminder.get(RemindersDbAdapter.COL_IMPORTANT));

        holder.id.setText(reminder.get(RemindersDbAdapter.COL_ID));
        holder.content.setText(reminder.get(RemindersDbAdapter.COL_CONTENT));
        holder.important.setText(reminder.get(RemindersDbAdapter.COL_IMPORTANT));

        boolean selected = false;
        if (selectedBooks.contains(reminder.get(RemindersDbAdapter.COL_IMPORTANT))) {
            selected = true;
        }

        if (holder.important.getText() == "1"){
            holder.viewColor.setBackgroundColor(Color.RED);
        }
        else {
            holder.viewColor.setBackgroundColor(Color.BLUE);
        }

        holder.check.setChecked(selected);

        return convertView;
    }
}
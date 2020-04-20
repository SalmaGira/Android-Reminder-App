package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;

public class RemindersActivity extends AppCompatActivity {
    RemindersDbAdapter db;
    Reminder reminder = new Reminder();
    private ListView remindersList;
    private RemindersSimpleCursorAdapter listAdapter;
    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new RemindersDbAdapter(this);


        Cursor cursor = db.fetchAllReminders();
        String preChanged = "Add reminders Please";//Replace row name with your row name
        String[] from = {
                RemindersDbAdapter.COL_ID,
                RemindersDbAdapter.COL_CONTENT ,
                RemindersDbAdapter.COL_IMPORTANT};
        //reminder view items ids
        int[] to = {
                R.id.id_list,
                R.id.content_list,
                R.id.important_id};

        //if it didnt work use this
        listAdapter = new RemindersSimpleCursorAdapter(this,R.layout.activity_listview,cursor,from,to,0);
        remindersList = findViewById(R.id.listView1);

        ArrayAdapter<String> itemsAdapter;
//        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listAdapter, adapter);

        remindersList.setAdapter(listAdapter);


        remindersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                final String selected = ((TextView) view.findViewById(R.id.id_list)).getText().toString();
                Toast.makeText(RemindersActivity.this, selected, Toast.LENGTH_SHORT).show();

                PopupMenu popup = new PopupMenu(RemindersActivity.this, view);
                popup.getMenuInflater().inflate(R.menu.dialog_custom, popup.getMenu());
                final Menu menu = popup.getMenu();
                menu.getItem(0).setTitle("Edit Reminder");
                menu.getItem(1).setTitle("Delete Reminder");
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = reminder.getId();
//                        AdapterView.AdapterContextMenuInfo info  = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
//                        Reminder reminder = (Reminder)listAdapter.getItem(info.position);
                        id = Integer.parseInt(selected);
                        switch (menuItem.getItemId()) {
                            case R.id.item1_dialog_custom:
                                Toast.makeText(RemindersActivity.this, "here to edit", Toast.LENGTH_SHORT).show();
                                showAlertDialog(1,id);
                                break;
                            case R.id.item2_dialog_custom:
                                // delete query
                                Toast.makeText(RemindersActivity.this, "here to delete", Toast.LENGTH_SHORT).show();
                                db.deleteReminderById(id);
                                break;
                            default:
                                break;
                        }

                        return true;
                    }
                });

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dialog_custom, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // menu item check handling
        switch (id) {
            case R.id.item1_dialog_custom:
                // make here the action of new remainder
                showAlertDialog(0,reminder.getId());
                break;
            case R.id.item2_dialog_custom:
                // make here the action of exit
                Toast.makeText(this, "hello from title 1", Toast.LENGTH_SHORT).show();
                db.deleteAllReminders();
                break;
            default: //nothing to do here
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showAlertDialog(int i,int id) {
        final int type=i;
        final int ID=id;
        View checkBoxView = View.inflate(this, R.layout.checkbox, null);
        final CheckBox checkBox = (CheckBox) checkBoxView.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // Save to shared preferences
            }
        });
        checkBox.setText("Important");

        final EditText editText = new EditText(RemindersActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(editText);
        layout.addView(checkBoxView);

        String title;
        if (type == 1)
            title = "Edit Reminder";
        else
            title = "New Reminder";

        alert.setTitle(title)
                .setView(layout)
                .setPositiveButton("COMMIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String reminderContent = editText.getText().toString();
                        Toast.makeText(RemindersActivity.this, reminderContent, Toast.LENGTH_SHORT).show();
                        if (!reminderContent.equals(" "));
                            reminder.setContent(reminderContent);
                        reminder.setId(ID);
                        int isSelected = checkBox.isChecked() ? 1 : 0;
                        reminder.setImportant(isSelected);
                        Toast.makeText(RemindersActivity.this, String.valueOf(ID), Toast.LENGTH_SHORT).show();
                        if (type == 1) {
                            // do query of edit

                            db.updateReminder(reminder);
                        } else {
                            // do query of new reminder
                            long isCreated = db.createReminder(reminder);
                            if (isCreated == -1) {
                                Toast.makeText(RemindersActivity.this, "error creation", Toast.LENGTH_SHORT).show();
                            }
                        }
                        recreate();

                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).show();

    }
}

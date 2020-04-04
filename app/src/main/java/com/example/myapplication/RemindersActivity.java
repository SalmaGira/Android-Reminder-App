package com.example.myapplication;

import android.content.DialogInterface;
import android.database.Cursor;
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
import android.widget.Toast;

public class RemindersActivity extends AppCompatActivity {
    String[] StringArray;// = {"Volvo", "BMW", "Ford", "Mazda"};
    RemindersDbAdapter db;
    RemindersSimpleCursorAdapter sc;
    Reminder reminder = new Reminder();

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


//        StringArray = db.fetchAllReminders();
        Cursor cursor = db.fetchAllReminders();
        String preChanged = "Add reminders Please";//Replace row name with your row name
//        if( cursor != null && cursor.moveToFirst() ){
//            preChanged = cursor.getString(cursor.getColumnIndex("content"));
//            cursor.close();
//        }
        StringArray = preChanged.split(",");//Can be changed to parts
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, StringArray);

        final ListView listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(MainActivity.this, StringArray[i], Toast.LENGTH_SHORT).show();

                PopupMenu popup = new PopupMenu(RemindersActivity.this, view);
                popup.getMenuInflater().inflate(R.menu.dialog_custom, popup.getMenu());
                Menu menu = popup.getMenu();
                menu.getItem(0).setTitle("Edit Reminder");
                menu.getItem(1).setTitle("Delete Reminder");
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();

                        switch (id) {
                            case R.id.item1_dialog_custom:
                                showAlertDialog(1);
                                break;
                            case R.id.item2_dialog_custom:
                                // delete query
                                db.deleteReminderById(reminder.getId());
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
                showAlertDialog(0);
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

    public void showAlertDialog(int i) {
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
        if (i == 1)
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

                        reminder.setContent(reminderContent);
                        int isSelected = checkBox.isSelected() ? 1 : 0;
                        reminder.setImportant(isSelected);

                        if (i == 1) {
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

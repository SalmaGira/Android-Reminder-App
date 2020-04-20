//package com.mine.trial1;
package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;

//import androidx.core.content.ContextCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


/**
 * Created by engMa_000 on 2017-04-03.
 */

public class RemindersSimpleCursorAdapter extends SimpleCursorAdapter {

    public RemindersSimpleCursorAdapter(Context context, int layout, Cursor c, String[]
            from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }


    @Override
    public Object getItem(int position) {
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(RemindersDbAdapter.COL_ID));
        String content = cursor.getString(cursor.getColumnIndexOrThrow(RemindersDbAdapter.COL_CONTENT));
        int imp = cursor.getInt(cursor.getColumnIndexOrThrow(RemindersDbAdapter.COL_IMPORTANT));
        Reminder reminder = new Reminder(id,content,imp);
        return reminder;
    }

    //to use a viewholder, you must override the following two methods and define a ViewHolder class
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return super.newView(context, cursor, parent);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.colImp = cursor.getColumnIndexOrThrow(RemindersDbAdapter.COL_IMPORTANT);
            holder.colContent = cursor.getColumnIndexOrThrow(RemindersDbAdapter.COL_CONTENT);
            holder.colID = cursor.getColumnIndexOrThrow(RemindersDbAdapter.COL_ID);

            holder.id = (TextView) view.findViewById(R.id.id_list);
            holder.content = (TextView) view.findViewById(R.id.content_list);
            holder.listTab = (TextView) view.findViewById(R.id.important_id);

            view.setTag(holder);
        }
        if (cursor.getInt(holder.colImp) > 0) {
            holder.listTab.setBackgroundColor(ContextCompat.getColor(context, R.color.orange));
        } else {
            holder.listTab.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
        }
    }
    static class ViewHolder {
        //store the column index
        int colID;
        int colImp;
        int colContent;


        //store the view
        TextView listTab;

        //store the text
        TextView content;

        //store the id
        TextView id;
    }

}

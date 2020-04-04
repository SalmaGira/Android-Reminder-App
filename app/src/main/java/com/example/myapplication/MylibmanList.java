//package com.example.myapplication;
//
//import android.app.Activity;
//import android.widget.BaseAdapter;
//import android.widget.CompoundButton;
//
//public class MylibmanList extends BaseAdapter {
//
//    private Activity activity;
//    private ArrayList<HashMap<String, String>> data;
//    private static LayoutInflater inflater=null;
//
//    HashSet<String> selectedBooks = new HashSet<String>();
//
//    //This listener will be used on all your checkboxes, there's no need to
//    //create a listener for every checkbox.
//    CompoundButton.OnCheckedChangeListener checkChangedListener = new CompoundButton.OnCheckedChangeListener{
//        @Override
//        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//            String bookDuration = (String) buttonView.getTag();
//            if(isChecked){
//                selectedBooks.add(bookDuration);
//            }else{
//                selectedBooks.remove(bookDuration);
//            }
//        }
//    }
//
//
//    public MylibmanList(Activity a, ArrayList<HashMap<String, String>> d) {
//        activity = a;
//        data=d;
//        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    }
//
//    static class ViewHolder {
//        TextView title;
//        TextView artist;
//        TextView duration;
//        CheckBox check;
//    }
//
//
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        if(convertView==null){
//            convertView = inflater.inflate(R.layout.listrow_row, null);
//
//            holder = new ViewHolder();
//            holder.title = (TextView) convertView.findViewById(R.id.title);
//            holder.artist = (TextView) convertView.findViewById(R.id.artist);
//            holder.duration = (TextView) convertView.findViewById(R.id.duration);
//            holder.check = (CheckBox) convertView.findViewById(R.id.check);
//
//
//            holder.check.setOnCheckedChangeListener(checkChangedListener);
//
//            convertView.setTag(holder);
//        }else{
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//
//        HashMap<String, String> book = new HashMap<String, String>();
//        book = (HashMap<String, String>) getItem(position);
//
//        holder.check.setTag(book.get(ShowList.LIST_KEY_ID));
//
//        holder.title.setText(book.get(ShowList.LIST_KEY_NAME));
//        holder.artist.setText(book.get(ShowList.LIST_KEY_WRITER));
//        holder.duration.setText(book.get(ShowList.LIST_KEY_ID));
//
//        boolean bookSelected = false;
//        if(selectedBooks.contains(book.get(ShowList.LIST_KEY_ID))){
//            bookSelected = true;
//        }
//
//        holder.check.setChecked(bookSelected);
//
//        return convertView;
//    }
//}

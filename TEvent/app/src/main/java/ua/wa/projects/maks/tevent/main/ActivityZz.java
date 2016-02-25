package ua.wa.projects.maks.tevent.main;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import ua.wa.projects.maks.tevent.R;

public class ActivityZz extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    //Вывод записей по текущему дню
    DataBaseHelper helper = new DataBaseHelper(this);
    SQLiteDatabase db;

    TextView textViewZz;
    TVadapter2 adapter;
    ArrayList<MyMessage> mGetMessages;
    String[] mTime;
    String[] mName;
    String[] mPost;
    String[] mPhone;
    String[] mWhat;
    Long[] mID;
    Long myID;

    int PosClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_zz);

        String name;
        Intent intent = getIntent();

        name = intent.getStringExtra("name1");                          //current date

        textViewZz = (TextView)findViewById(R.id.textViewZz);
        textViewZz.setText(name);

        mGetMessages = helper.getMessages(name); //получаем массив по дню

        mID = new Long[mGetMessages.size()];
        mTime = new String[mGetMessages.size()];
        mName = new String[mGetMessages.size()];
        mPost = new String[mGetMessages.size()];
        mPhone = new String[mGetMessages.size()];
        mWhat = new String[mGetMessages.size()];

        for(int i=0; i<mGetMessages.size(); i++) {
            //mGetMessages.get(i).id;
            mID[i] = (mGetMessages.get(i).id);
            mTime[i] = (mGetMessages.get(i).mTimeMsg);
            mName[i] = (mGetMessages.get(i).mFirstName);
            mPost[i] = (mGetMessages.get(i).mPostADR);
            mPhone[i] = (mGetMessages.get(i).mPhone);
            mWhat[i] = (mGetMessages.get(i).mWhatDo);
        }

        ListView listView = (ListView)findViewById(R.id.listViewZz);

        adapter = new TVadapter2(this,R.layout.dey_item,R.id.textView2, mTime);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        PopupMenu menu = new PopupMenu(this, view);
        menu.inflate(R.menu.pap_menu);

        final Context context = this;     // MainActivity.this
        PosClick = position;
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu1:
                        goDEditRecord(PosClick);
                        //Toast.makeText(context, "Menu Edit" + String.valueOf(PosClick), Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu2:
                        goDelRecord(PosClick);
                        //Toast.makeText(context, "Menu Delete" + String.valueOf(PosClick), Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });
        menu.show();

        //Toast.makeText(this,"Удалить?" + String.valueOf(position)+ "-" + String.valueOf(id), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        //Toast.makeText(this,"2" + String.valueOf(position),Toast.LENGTH_SHORT).show();
        return true;
    }


    //дополнительный адаптер2
    public class TVadapter2 extends ArrayAdapter<String> {
        private Context mContext;
        private int mResource;
        private int mResource2;
        private String[] mArr;

        public TVadapter2(Context context, int resource,  int resource2, String[] objects) {
            super(context, resource, objects);
            mContext = context;
            mResource = resource;
            mResource2 = resource2;
            mArr = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItemView(position);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getItemView(position);
        }


        private View getItemView (int position){
            LayoutInflater inflater = LayoutInflater.from(this.mContext);

            View view = inflater.inflate(this.mResource, null);

            switch (position){
                case 5:
                    ((TextView) view.findViewById(R.id.textView2)).setText(mTime[position]);
                    ((TextView) view.findViewById(R.id.textView2)).setTextColor(getResources().getColor(R.color.colorCoral1));
                    ((TextView) view.findViewById(R.id.textView3)).setText(mName[position]);
                    ((TextView) view.findViewById(R.id.textView4)).setText(mPost[position]);
                    ((TextView) view.findViewById(R.id.textView5)).setText(mPhone[position]);
                    ((TextView) view.findViewById(R.id.textView6)).setText(mWhat[position]);
                    break;
                case 6:
                    ((TextView) view.findViewById(R.id.textView2)).setText(mTime[position]);
                    ((TextView) view.findViewById(R.id.textView2)).setTextColor(getResources().getColor(R.color.colorCoral1));
                    ((TextView) view.findViewById(R.id.textView3)).setText(mName[position]);
                    ((TextView) view.findViewById(R.id.textView4)).setText(mPost[position]);
                    ((TextView) view.findViewById(R.id.textView5)).setText(mPhone[position]);
                    ((TextView) view.findViewById(R.id.textView6)).setText(mWhat[position]);
                    break;
                case 7:
                    ((TextView) view.findViewById(R.id.textView2)).setText(" ");
                    //((TextView) view.findViewById(R.id.textViewA)).setTextColor(getResources().getColor(R.color.colorCoral1));

                    break;
                default:
                    ((TextView) view.findViewById(R.id.textView2)).setText(mTime[position]);
                    ((TextView) view.findViewById(R.id.textView2)).setTextColor(getResources().getColor(R.color.colorBlack));
                    ((TextView) view.findViewById(R.id.textView3)).setText(mName[position]);
                    ((TextView) view.findViewById(R.id.textView4)).setText(mPost[position]);
                    ((TextView) view.findViewById(R.id.textView5)).setText(mPhone[position]);
                    ((TextView) view.findViewById(R.id.textView6)).setText(mWhat[position]);
                    break;
            }

            //if (position == posCurData){
            //    ((TextView) view.findViewById(R.id.textViewA)).setBackgroundColor(getResources().getColor(R.color.colorCoral4));
            //}
            return view;
        }
    }

    public void goDelRecord (int position){
        db = helper.getWritableDatabase();
        myID = mID[position];

        int countD = db.delete(MyMessage.TABLE_NAME, "_id = " + myID, null);
        Toast.makeText(this, "Menu Delete -" + String.valueOf(countD), Toast.LENGTH_SHORT).show();
    }

    public void goDEditRecord (int position){
        myID = mID[position];
        String name = String.valueOf(myID);
        Intent intent = new Intent(ActivityZz.this, ActivityZ.class);
        intent.putExtra("nameE", name);
        intent.putExtra("key", "E");
        startActivity(intent);

        //Toast.makeText(this, "Menu Edit" + String.valueOf(PosClick), Toast.LENGTH_SHORT).show();

    }
}

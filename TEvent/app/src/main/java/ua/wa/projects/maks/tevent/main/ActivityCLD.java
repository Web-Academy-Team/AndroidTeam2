package ua.wa.projects.maks.tevent.main;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;

import ua.wa.projects.maks.tevent.R;

public class ActivityCLD extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    //вывод записей по текущей неделе

    DataBaseHelper helper = new DataBaseHelper(this);
    SQLiteDatabase db;

    GridView gridView;
    String[] data = new String[8];
    String[] mData1 = new String[8];
    String[] mData2 = new String[8];
    String[] mData3 = new String[8];
    String[] mData4 = new String[8];
    String[] mData5 = new String[8];

    //ArrayAdapter<String> adapter;
    TVadapter adapter;
    int posCurData;
    ArrayList<MyMessage> mGetMessages;
    String[] mTime = new String[5];
    String[] mName = new String[5];
    String[] mPost = new String[5];
    String[] mPhon = new String[5];
    String[] mWhat = new String[5];
    Long[] mID = new Long[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_cld);

        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText(getDate());

        getEE();  //заполняем массив data
        int n;
        for(n=0; n<7; n++) {
            String mCurDey = data[n];
            mGetMessages = helper.getMessages(mCurDey); //получаем массив по дню

            int mMesSize;
            if (mGetMessages.size() < 5){
                mMesSize = mGetMessages.size();
            }else {
                mMesSize = 5;
            }

            for (int i = 0; i < mMesSize; i++) {
                //mGetMessages.get(i).id;
                //mID[i] = (mGetMessages.get(i).id);
                if(mGetMessages.get(i).mTimeMsg.equals("")){
                    mTime[i] = "-";
                }else { mTime[i] = mGetMessages.get(i).mTimeMsg;};
                if(mGetMessages.get(i).mFirstName.equals("")){
                    mName[i] = "-";
                }else { mName[i] = mGetMessages.get(i).mFirstName;};
                if(mGetMessages.get(i).mPhone.equals("")){
                    mPhon[i] = "-";
                }else {mPhon[i] = mGetMessages.get(i).mPhone; };
                if(mGetMessages.get(i).mWhatDo.equals("")){
                    mWhat[i] = "-";
                }else {mWhat[i] = mGetMessages.get(i).mWhatDo; };

                //mTime[i] = (mGetMessages.get(i).mTimeMsg);
                //mName[i] = (mGetMessages.get(i).mFirstName);
                //--mPost[i] = (mGetMessages.get(i).mPostADR);
                //mPhon[i] = (mGetMessages.get(i).mPhone);
                //mWhat[i] = (mGetMessages.get(i).mWhatDo);
            }

            switch (mMesSize) {
                case 1:
                    mData1[n] = mTime[0] + " " + mName[0] + " " + mPhon[0] + " " + mWhat[0];
                    break;
                case 2:
                    mData1[n] = mTime[0] + " " + mName[0] + " " + mPhon[0] + " " + mWhat[0];
                    mData2[n] = mTime[1] + " " + mName[1] + " " + mPhon[1] + " " + mWhat[1];
                    break;
                case 3:
                    mData1[n] = mTime[0] + " " + mName[0] + " " + mPhon[0] + " " + mWhat[0];
                    mData2[n] = mTime[1] + " " + mName[1] + " " + mPhon[1] + " " + mWhat[1];
                    mData3[n] = mTime[2] + " " + mName[2] + " " + mPhon[2] + " " + mWhat[2];
                    break;
                case 4:
                    mData1[n] = mTime[0] + " " + mName[0] + " " + mPhon[0] + " " + mWhat[0];
                    mData2[n] = mTime[1] + " " + mName[1] + " " + mPhon[1] + " " + mWhat[1];
                    mData3[n] = mTime[2] + " " + mName[2] + " " + mPhon[2] + " " + mWhat[2];
                    mData4[n] = mTime[3] + " " + mName[3] + " " + mPhon[3] + " " + mWhat[3];
                    break;
                case 5:
                    mData1[n] = mTime[0] + " " + mName[0] + " " + mPhon[0] + " " + mWhat[0];
                    mData2[n] = mTime[1] + " " + mName[1] + " " + mPhon[1] + " " + mWhat[1];
                    mData3[n] = mTime[2] + " " + mName[2] + " " + mPhon[2] + " " + mWhat[2];
                    mData4[n] = mTime[3] + " " + mName[3] + " " + mPhon[3] + " " + mWhat[3];
                    mData5[n] = mTime[4] + " " + mName[4] + " " + mPhon[4] + " " + mWhat[4];
                    break;
            }
        }




        gridView = (GridView) findViewById(R.id.gridView);


        //adapter = new ArrayAdapter<String>(this,R.layout.item,R.id.textViewA, data);
        adapter = new TVadapter(this,R.layout.item,R.id.textViewA, data);

        gridView.setAdapter(adapter);


        settingGridView();

        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);
    }
    private void settingGridView(){
        gridView.setNumColumns(2);
        gridView.setHorizontalSpacing(5);
        gridView.setVerticalSpacing(5);
        gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
    }



    private String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("EE  dd.MM.yyyy");
        return  sdf.format(new Date());
    }

    private String getDate(int dey){ // по отклонению от текущего дня определяем дату
        Date currentDate = new Date();
        Long time = currentDate.getTime();
        Long time2 = time + (60*60*24*1000*(dey));
        SimpleDateFormat sdf = new SimpleDateFormat("EE  dd.MM.yyyy");
        return  sdf.format(new Date(time2));
    }

    private void getEE(){   // определяем позицию текущего дня в массиве
        SimpleDateFormat sdf = new SimpleDateFormat("EE");
        String deyEE = sdf.format(new Date());

        switch (deyEE) {
            case "Mon":
                posCurData = 0;
                break;
            case "Tue":
                posCurData = 1;
                break;
            case "Wed":
                posCurData = 2;
                break;
            case "Thu":
                posCurData = 3;
                break;
            case "Fri":
                posCurData = 4;
                break;
            case "Sat":
                posCurData = 5;
                break;
            case "Sun":
                posCurData = 6;
                break;
        }

        int k = -posCurData;

        //заполняем массив data
        setData(k);
        data[7] = "-";

    }

    private void setData(int k){
        for(int i =0; i<7; i++, k++){
            data[i] = getDate(k);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String name1 = data[position];
        Intent intent1 = new Intent(ActivityCLD.this, ActivityZz.class);
        intent1.putExtra("name1", name1);
        startActivity(intent1);
        //Toast.makeText(this,"Ok-" + String.valueOf(position),Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        String name = data[position];
        Intent intent = new Intent(ActivityCLD.this, ActivityZ.class);
        intent.putExtra("nameE", name);
        intent.putExtra("key", "N");
        startActivity(intent);
        //Toast.makeText(this,"Ok-" + String.valueOf(position),Toast.LENGTH_SHORT).show();
        return true;
    }


    //дополнительный адаптер
    public class TVadapter extends ArrayAdapter<String> {
        private Context mContext;
        private int mResource;
        private int mResource2;
        private String[] mArr;

        public TVadapter(Context context, int resource,  int resource2, String[] objects) {
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
                    ((TextView) view.findViewById(R.id.textViewA)).setText(data[position]);
                    ((TextView) view.findViewById(R.id.textViewA)).setTextColor(getResources().getColor(R.color.colorCoral1));
                    ((TextView) view.findViewById(R.id.textViewB)).setText(mData1[position]);
                    ((TextView) view.findViewById(R.id.textViewC)).setText(mData2[position]);
                    ((TextView) view.findViewById(R.id.textViewD)).setText(mData3[position]);
                    ((TextView) view.findViewById(R.id.textViewE)).setText(mData4[position]);
                    ((TextView) view.findViewById(R.id.textViewF)).setText(mData5[position]);
                    break;
                case 6:
                    ((TextView) view.findViewById(R.id.textViewA)).setText(data[position]);
                    ((TextView) view.findViewById(R.id.textViewA)).setTextColor(getResources().getColor(R.color.colorCoral1));
                    ((TextView) view.findViewById(R.id.textViewB)).setText(mData1[position]);
                    ((TextView) view.findViewById(R.id.textViewC)).setText(mData2[position]);
                    ((TextView) view.findViewById(R.id.textViewD)).setText(mData3[position]);
                    ((TextView) view.findViewById(R.id.textViewE)).setText(mData4[position]);
                    ((TextView) view.findViewById(R.id.textViewF)).setText(mData5[position]);
                    break;
                case 7:
                    ((TextView) view.findViewById(R.id.textViewA)).setText(" ");
                    //((TextView) view.findViewById(R.id.textViewA)).setTextColor(getResources().getColor(R.color.colorCoral1));
                    ((TextView) view.findViewById(R.id.textViewB)).setText(mData1[position]);
                    break;
                default:
                    ((TextView) view.findViewById(R.id.textViewA)).setText(data[position]);
                    ((TextView) view.findViewById(R.id.textViewA)).setTextColor(getResources().getColor(R.color.colorBlack));
                    ((TextView) view.findViewById(R.id.textViewB)).setText(mData1[position]);
                    ((TextView) view.findViewById(R.id.textViewC)).setText(mData2[position]);
                    ((TextView) view.findViewById(R.id.textViewD)).setText(mData3[position]);
                    ((TextView) view.findViewById(R.id.textViewE)).setText(mData4[position]);
                    ((TextView) view.findViewById(R.id.textViewF)).setText(mData5[position]);
                    break;
            }

            if (position == posCurData){
                ((TextView) view.findViewById(R.id.textViewA)).setBackgroundColor(getResources().getColor(R.color.colorCoral4));
            }
            return view;
        }
    }
}
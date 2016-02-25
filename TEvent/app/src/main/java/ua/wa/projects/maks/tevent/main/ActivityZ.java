package ua.wa.projects.maks.tevent.main;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import ua.wa.projects.maks.tevent.R;

public class ActivityZ extends AppCompatActivity implements View.OnClickListener{
    //Создание новой записи в БД

    DataBaseHelper helper = new DataBaseHelper(this);
    SQLiteDatabase db;

    TextView textView;
    EditText editText;
    EditText editText2;
    EditText editText3;
    EditText editText8;
    EditText editText5;
    EditText editText6;
    EditText editText7;
    CheckBox checkBox;
    CheckBox checkBox2;
    CheckBox checkBox3;
    String nameKey;
    String nameZn;
    String nameCurDey;
    long nameID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_z);


        textView = (TextView)findViewById(R.id.textView);

        editText = (EditText)findViewById(R.id.editText);      //time
        editText2 = (EditText)findViewById(R.id.editText2);    //Name
        editText3 = (EditText)findViewById(R.id.editText3);    //Phone
        editText8 = (EditText)findViewById(R.id.editText8);    //what to do
        editText5 = (EditText)findViewById(R.id.editText5);    //Post Adress

        checkBox = (CheckBox) findViewById(R.id.checkBox);     //Remind me
        editText6 = (EditText)findViewById(R.id.editText6);    //date
        editText7 = (EditText)findViewById(R.id.editText7);    //time
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);   //alarm
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);   //Notification

        Button button3 = (Button)findViewById(R.id.button3);             //save
        Button button4 = (Button)findViewById(R.id.button4);

        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        editText.setOnClickListener(this);
        editText6.setOnClickListener(this);
        editText7.setOnClickListener(this);

        Intent intent = getIntent();

        nameZn = intent.getStringExtra("nameE");
        nameKey = intent.getStringExtra("key");

        switch (nameKey){
            case "N":
                nameCurDey = nameZn;  //для создания новой записи
                textView.setText(nameCurDey);
                break;
            case "E":
                nameID = Long.parseLong(nameZn);  //для редактирования записи ID
                fillInTheFields(nameID); //заполним поля
                break;
        }
    }

    @Override
    public void onClick(View v) {
        db = helper.getWritableDatabase();

        switch (v.getId()){
            case R.id.button3:

                int ch1 = 0;
                int ch2 = 0;
                int ch3 = 0;

                if(checkBox.isChecked()) {ch1 = 1;}
                if(checkBox2.isChecked()) {ch2 = 1;}
                if(checkBox3.isChecked()) {ch3 = 1;}

                ContentValues valuesS = new ContentValues();


                valuesS.put(MyMessage.COLUMN_DATE, textView.getText().toString());
                valuesS.put(MyMessage.COLUMN_TIME, editText.getText().toString());
                valuesS.put(MyMessage.COLUMN_NAME, editText2.getText().toString());
                valuesS.put(MyMessage.COLUMN_PHONE, editText3.getText().toString());
                valuesS.put(MyMessage.COLUMN_WHOT, editText8.getText().toString());
                valuesS.put(MyMessage.COLUMN_PADR, editText5.getText().toString());

                valuesS.put(MyMessage.COLUMN_RMIND, Integer.valueOf(ch1));

                valuesS.put(MyMessage.COLUMN_RDATE, editText6.getText().toString());
                valuesS.put(MyMessage.COLUMN_RTIME, editText7.getText().toString());


                valuesS.put(MyMessage.COLUMN_ALARM, Integer.valueOf(ch2));
                valuesS.put(MyMessage.COLUMN_NOTIF, Integer.valueOf(ch3));


                switch (nameKey){
                    case "N":
                        long nomZs = db.insert(MyMessage.TABLE_NAME, null, valuesS);  // номер записи
                        Toast.makeText(this, "Create - " + String.valueOf(nomZs), Toast.LENGTH_SHORT).show();  //для создания новой записи
                        break;
                    case "E":
                        long nomZu = db.update(MyMessage.TABLE_NAME, valuesS, "_id = " + nameID, null);   //для редактирования записи ID
                        Toast.makeText(this, "Edit - " + String.valueOf(nomZu), Toast.LENGTH_SHORT).show();
                        break;
                }


                break;
            case R.id.button4:
                //helper.onDelete(db);  //Удаляет таблицу БД
                //Toast.makeText(this, "Ok-8", Toast.LENGTH_SHORT).show();

                //MySectionNotification mySectionNotification = new MySectionNotification();
                sendNotif();

                break;
            case R.id.editText:
                getTimeDialig();
                break;
            case R.id.editText7:
                getTimeDialigR();
                break;
            case R.id.editText6:
                getDateDialigR();
                break;
        }

    }


    private void getTimeDialig(){
        new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String strTime = String.format("%s:%s", hourOfDay, minute);
                        editText.setText(strTime);
                    }
                }, 0, 0, true
        ).show();
    }

    private void getTimeDialigR(){
        new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String strTime = String.format("%s:%s", hourOfDay, minute);
                        editText7.setText(strTime);
                    }
                }, 0, 0, true
        ).show();
    }

    private void getDateDialigR(){
        new DatePickerDialog(ActivityZ.this,
                new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String strTime = String.format("%s.%s.%s", String.valueOf(dayOfMonth),
                                String.valueOf(monthOfYear+1), String.valueOf(year));
                        editText6.setText(strTime);
                    }
                }, 2016, 0, 0
        ).show();
    }


    private void fillInTheFields(long id){
        MyMessage mMessage = helper.getMessage(id);

        textView.setText(mMessage.mDateMsg);                         //nameCurDey

        editText.setText(mMessage.mTimeMsg);                        //time
        editText2.setText(mMessage.mFirstName);                     //Name
        editText3.setText(mMessage.mPhone);                         //Phone
        editText8.setText(mMessage.mWhatDo);                        //what to do
        editText5.setText(mMessage.mPostADR);                       //Post Adress

        if(mMessage.mRemind == 1){
            checkBox.setChecked(true);                              //Remind me
        }else {
            checkBox.setChecked(false);
        }

        editText6.setText(mMessage.mRdate);                         //date
        editText7.setText(mMessage.mRtime);                         //time

        if(mMessage.mRalarm == 1){
            checkBox2.setChecked(true);                              //alarm
        }else {
            checkBox2.setChecked(false);
        }

        if(mMessage.mRnotif == 1){
            checkBox3.setChecked(true);                              //Notification
        }else {
            checkBox3.setChecked(false);
        }

    }



    public void sendNotif() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;

        Intent intent = new Intent(this, ActivityCLD.class);
        //intent.putExtra("MS","somefile");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.notifi_item);

        remoteViews.setTextViewText(R.id.textView2, "Notification Title");
        remoteViews.setTextViewText(R.id.textView3, "Notification Text");
        notification = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(Notification.DEFAULT_ALL)
                .setTicker("Notification!")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .build();

        notification.contentView = remoteViews;
        notificationManager.notify(2, notification);
    }

}


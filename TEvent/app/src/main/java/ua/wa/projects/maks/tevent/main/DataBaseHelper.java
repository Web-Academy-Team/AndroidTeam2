package ua.wa.projects.maks.tevent.main;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.util.ArrayList;

import ua.wa.projects.maks.tevent.R;

public class DataBaseHelper extends SQLiteOpenHelper{


    public DataBaseHelper(Context context) {
        super(context, "MyDBcl.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MyMessage.TABLE_NAME + " ("
                + MyMessage.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MyMessage.COLUMN_DATE + " TEXT NOT NULL, "
                + MyMessage.COLUMN_TIME + " TEXT NOT NULL, "
                + MyMessage.COLUMN_NAME + " TEXT NOT NULL, "
                + MyMessage.COLUMN_PHONE + " TEXT NOT NULL, "
                + MyMessage.COLUMN_WHOT + " TEXT NOT NULL, "
                + MyMessage.COLUMN_PADR + " TEXT NOT NULL, "
                + MyMessage.COLUMN_RMIND + " INTEGER NOT NULL, "
                + MyMessage.COLUMN_RDATE + " TEXT NOT NULL, "
                + MyMessage.COLUMN_RTIME + " TEXT NOT NULL, "
                + MyMessage.COLUMN_ALARM + " INTEGER NOT NULL, "
                + MyMessage.COLUMN_NOTIF + " INTEGER NOT NULL);");
        Log.d("MyLOG","------ onCreateTableDB");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Обновление БД
    }

    public void onDelete(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + MyMessage.TABLE_NAME + "");
        //deleting DB!
    }


    public MyMessage getMessage(long id){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        MyMessage mMessage = null;
        try {
            //запрос к БД из таблицы MyMessage    также db.rawQuery("SELECT...")
            cursor = db.query(MyMessage.TABLE_NAME, null,
                    MyMessage.COLUMN_ID + " = " + id,
                    null, null, null, null);

            if (cursor.moveToFirst()) {

                mMessage = new MyMessage();

                mMessage.id = cursor.getLong(cursor.getColumnIndex(MyMessage.COLUMN_ID));
                mMessage.mDateMsg = cursor.getString(cursor.getColumnIndex(MyMessage.COLUMN_DATE));
                mMessage.mTimeMsg = cursor.getString(cursor.getColumnIndex(MyMessage.COLUMN_TIME));
                mMessage.mFirstName = cursor.getString(cursor.getColumnIndex(MyMessage.COLUMN_NAME));
                mMessage.mPhone = cursor.getString(cursor.getColumnIndex(MyMessage.COLUMN_PHONE));
                mMessage.mWhatDo = cursor.getString(cursor.getColumnIndex(MyMessage.COLUMN_WHOT));
                mMessage.mPostADR = cursor.getString(cursor.getColumnIndex(MyMessage.COLUMN_PADR));
                mMessage.mRemind = cursor.getInt(cursor.getColumnIndex(MyMessage.COLUMN_RMIND));
                mMessage.mRdate = cursor.getString(cursor.getColumnIndex(MyMessage.COLUMN_RDATE));
                mMessage.mRtime = cursor.getString(cursor.getColumnIndex(MyMessage.COLUMN_RTIME));
                mMessage.mRalarm = cursor.getInt(cursor.getColumnIndex(MyMessage.COLUMN_ALARM));
                mMessage.mRnotif = cursor.getInt(cursor.getColumnIndex(MyMessage.COLUMN_NOTIF));
            }
        }catch(Exception e){
            e.printStackTrace();
        } finally{
            if(cursor !=null){
                cursor.close();
            }
        }
        return  mMessage;
    }


    public ArrayList<MyMessage> getMessages(String mDT){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        ArrayList<MyMessage> mMessageS = new ArrayList<>();
        try {
            //запрос к БД из таблицы MyMessage    также db.rawQuery("SELECT...")
            cursor = db.query(MyMessage.TABLE_NAME, null,
                    MyMessage.COLUMN_DATE + " = '" + mDT + "' ",
                    null, null, null, null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    MyMessage mMessage = new MyMessage();

                    mMessage.id = cursor.getLong(cursor.getColumnIndex(MyMessage.COLUMN_ID));
                    mMessage.mDateMsg = cursor.getString(cursor.getColumnIndex(MyMessage.COLUMN_DATE));
                    mMessage.mTimeMsg = cursor.getString(cursor.getColumnIndex(MyMessage.COLUMN_TIME));
                    mMessage.mFirstName = cursor.getString(cursor.getColumnIndex(MyMessage.COLUMN_NAME));
                    mMessage.mPhone = cursor.getString(cursor.getColumnIndex(MyMessage.COLUMN_PHONE));
                    mMessage.mWhatDo = cursor.getString(cursor.getColumnIndex(MyMessage.COLUMN_WHOT));
                    mMessage.mPostADR = cursor.getString(cursor.getColumnIndex(MyMessage.COLUMN_PADR));
                    //mMessage.mRemind = cursor.getInt(cursor.getColumnIndex(MyMessage.COLUMN_RMIND));
                    //mMessage.mRdate = cursor.getString(cursor.getColumnIndex(MyMessage.COLUMN_RDATE));
                    //mMessage.mRtime = cursor.getString(cursor.getColumnIndex(MyMessage.COLUMN_RTIME));
                    //mMessage.mRalarm = cursor.getInt(cursor.getColumnIndex(MyMessage.COLUMN_ALARM));
                    //mMessage.mRnotif = cursor.getInt(cursor.getColumnIndex(MyMessage.COLUMN_NOTIF));

                    mMessageS.add(mMessage);
                    cursor.moveToNext();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        } finally{
            if(cursor !=null){
                cursor.close();
            }
        }
        return  mMessageS;
        //Toast.makeText(this, "Students count: " + students.size(), Toast.LENGTH_SHORT).show();
    }


}

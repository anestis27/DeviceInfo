package com.example.orion.leitourgika;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class MyDataBase extends SQLiteOpenHelper{

    private static final int Database_Version = 1;
    private static final String Database_Name = "HealthDatabase";

    private static final String Table_Space = "Space";


    //Table columns
    private static final String Key_Id = "_id";
    private static final String Key_INTERNAL = "internal";
    private static final String Key_EXTERNAL = "external";


    private static final String Create_Diet_Table= "CREATE TABLE "+ Table_Space + "(" + Key_Id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Key_INTERNAL + " STRING," + Key_EXTERNAL + " STRING " +  ")";

    private SQLiteDatabase db;


    public MyDataBase(Context context){
        super(context, Database_Name, null, Database_Version);
        db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create_Diet_Table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_Space);
        onCreate(db);
    }

    public void InsertSpace(Space item){

        ContentValues cv=new ContentValues();
        cv.put(Key_INTERNAL,item.getInternal());
        cv.put(Key_EXTERNAL,item.getExternal());

        db.insert(Table_Space,null,cv);
    }

    public Cursor getAllSpaceItems(){
        return getReadableDatabase().rawQuery("SELECT * FROM " + Table_Space,
                null);
    }
}
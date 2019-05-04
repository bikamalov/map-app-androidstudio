package com.example.map;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "place.db";
    public static final String DATABASE_TABLE = "place";
    public static final String ID = "_id";
    public static final String PLACE_TITLE = "_title";
    public static final String PLACE_DESCRIPTION = "_description";
    public static final String PLACE_LATITUDE = "_latitude";
    public static final String PLACE_LONGITUDE = "_longitude";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE table " + DATABASE_TABLE + "(" +
                ID + " integer primary key autoincrement, "+
                PLACE_TITLE + " text not null, " +
                PLACE_DESCRIPTION + " text not null, " +
                PLACE_LATITUDE + " real , " +
                PLACE_LONGITUDE + " real ); "
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DATABASE_TABLE);
        onCreate(db);
    }
    public void insertNote(Places place){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PLACE_TITLE, place.getTitle());
        cv.put(PLACE_DESCRIPTION, place.getDescription());
        cv.put(PLACE_LATITUDE,place.getLatitude());
        cv.put(PLACE_LONGITUDE,place.getLongitude());
        db.insert(DATABASE_TABLE, null, cv);
    }
    public ArrayList<Places> getNotes(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Places> notes = new ArrayList<>();
        String query = "SELECT * FROM " + DATABASE_TABLE+" ORDER BY " + ID + " DESC";
        Cursor c = db.rawQuery(query,null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Places item = new Places(c.getString(c.getColumnIndex(PLACE_TITLE)),
                    c.getString(c.getColumnIndex(PLACE_DESCRIPTION)),
                    c.getDouble(c.getColumnIndex(PLACE_LATITUDE)),
                    c.getDouble(c.getColumnIndex(PLACE_LONGITUDE))
            );
            item.setID(c.getInt(c.getColumnIndex(ID)));
            notes.add(item);
        }
        return notes;
    }
    public boolean deleteNote(int id){
        SQLiteDatabase db = getWritableDatabase();
        if (id != -1)
            return db.delete(DATABASE_TABLE, ID + "=" + id, null) > 0;
        else return false;
    }
}
package edu.umsl.notes.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.HashMap;

import edu.umsl.notes.Notes;

import static edu.umsl.notes.database.FinalSchema.*;

/**
 * Created by taylorfreiner on 4/15/17.
 */

public class FinalBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "finalBase.db";
    private Context mContext;
    private static HashMap<Context, FinalBaseHelper> mInstances;

    public FinalBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        mContext = context;
    }

    public static FinalBaseHelper getInstance(Context context) {
        if(mInstances == null) { mInstances = new HashMap<>(); }
        if(mInstances.get(context) == null) { mInstances.put(context, new FinalBaseHelper(context)); }
        return mInstances.get(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table if not exists " + UserTable.NAME + " (" +

                UserTable.Cols.FIRST + " text, " +
                UserTable.Cols.LAST + " text, " +
                UserTable.Cols.EMAIL + " text, " +
                UserTable.Cols.USER + " text, " +
                UserTable.Cols.PASS + " text, " +
                UserTable.Cols.ACTIVE_USER + " text);"
        );

        db.execSQL("create table if not exists " + NotesTable.NAME + " (" +
                NotesTable.Cols.USER_ID + " text, " +
                NotesTable.Cols.TITLE + " text, " +
                NotesTable.Cols.TEXT + " text, " +
                NotesTable.Cols.ACTIVE_NOTE + " text);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

package edu.umsl.notes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.util.Log;

import edu.umsl.notes.Notes;
import edu.umsl.notes.User;
import edu.umsl.notes.database.FinalSchema.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taylorfreiner on 4/15/17.
 */

public class FinalPersistence {
    private static String TAG = "FinalPersistence";
    private static FinalPersistence sFinalPersistence;
    private Context mContext;
    private FinalBaseHelper mHelper;
    private SQLiteDatabase mDatabase;


    private FinalPersistence(Context context){
        Log.d(TAG, "FinalPersistence()---init");
        mContext = context.getApplicationContext();
        mHelper = FinalBaseHelper.getInstance(mContext);
        mDatabase = mHelper.getWritableDatabase();
    }

    public static FinalPersistence get(Context context){
        Log.d(TAG, "get()");
        if(sFinalPersistence == null){
            sFinalPersistence = new FinalPersistence(context);
        }
        return sFinalPersistence;
    }

    //checks to see if users exists in the database
    public Boolean usersExist(){
        mDatabase = mHelper.getWritableDatabase();
        Cursor resultSet = mDatabase.rawQuery("select * from " + UserTable.NAME, null);
        int num = resultSet.getCount();
        if (num == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    public Boolean notesExist(){
        mDatabase = mHelper.getWritableDatabase();
        Cursor resultSet = mDatabase.rawQuery("select * from " + NotesTable.NAME, null);
        int num = resultSet.getCount();
        if (num == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    // checks to see if a user is logged in
    public Boolean isUserLoggedIn() {
        mDatabase = mHelper.getWritableDatabase();
        List<User> users;
        users = getUsers();
        for(int i = 0; i < users.size(); i++){
            if (users.get(i).getActiveUser().equals("yes"))
                return true;
        }
        return false;
    }

    public String getActiveUser(){
        mDatabase = mHelper.getWritableDatabase();
        List<User> users;
        String user;
        users = getUsers();
        for(int i = 0; i < users.size(); i++){
            if (users.get(i).getActiveUser().equals("yes")) {
                user = users.get(i).getUsername();
                return user;
            }
        }
        return null;
    }

    //persists a user to the database
    public void addUser(User user) {

        ContentValues cv = new ContentValues();
        mDatabase = mHelper.getWritableDatabase();

        Log.d(TAG, "addUser()");
        cv.put(UserTable.Cols.USER, user.getUsername());
        cv.put(UserTable.Cols.EMAIL, user.getEmail());
        cv.put(UserTable.Cols.FIRST, user.getFirst());
        cv.put(UserTable.Cols.LAST, user.getLast());
        cv.put(UserTable.Cols.PASS, user.getPassword());
        cv.put(UserTable.Cols.ACTIVE_USER, user.getActiveUser());
        mDatabase.insert(UserTable.NAME, null, cv);
        cv.clear();
        mDatabase.close();
    }

    //updates an active user
    public void updateUser(User user){

        mDatabase = mHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(UserTable.Cols.USER, user.getUsername());
        cv.put(UserTable.Cols.EMAIL, user.getEmail());
        cv.put(UserTable.Cols.FIRST, user.getFirst());
        cv.put(UserTable.Cols.LAST, user.getLast());

        String whereClause = UserTable.Cols.ACTIVE_USER + " = '" + "yes" + "'";
        mDatabase.update(UserTable.NAME, cv, whereClause, null);
        cv.clear();
        mDatabase.close();

    }

    // returns a List of User populated with content from the database
    public List<User> getUsers() {

        List<User> users;
        users = new ArrayList<>();
        Log.d(TAG, "getUsers()");
        mDatabase = mHelper.getWritableDatabase();
        Cursor resultSet = mDatabase.rawQuery(
                "select * from " + UserTable.NAME + ";", null
        );
        resultSet.moveToFirst();

        User user = new User(
                resultSet.getString(resultSet.getColumnIndex(UserTable.Cols.FIRST)),
                resultSet.getString(resultSet.getColumnIndex(UserTable.Cols.LAST)),
                resultSet.getString(resultSet.getColumnIndex(UserTable.Cols.EMAIL)),
                resultSet.getString(resultSet.getColumnIndex(UserTable.Cols.USER)),
                resultSet.getString(resultSet.getColumnIndex(UserTable.Cols.PASS)),
                resultSet.getString(resultSet.getColumnIndex(UserTable.Cols.ACTIVE_USER))
        );
        int num = resultSet.getCount();
        users.add(user);

        for(int i = 1; i < num; i++){

            resultSet.moveToNext();
            user = new User(
                    resultSet.getString(resultSet.getColumnIndex(UserTable.Cols.FIRST)),
                    resultSet.getString(resultSet.getColumnIndex(UserTable.Cols.LAST)),
                    resultSet.getString(resultSet.getColumnIndex(UserTable.Cols.EMAIL)),
                    resultSet.getString(resultSet.getColumnIndex(UserTable.Cols.USER)),
                    resultSet.getString(resultSet.getColumnIndex(UserTable.Cols.PASS)),
                    resultSet.getString(resultSet.getColumnIndex(UserTable.Cols.ACTIVE_USER))
            );
            users.add(user);
        }

        mDatabase.close();
        return users;
    }

    //adds a note to the database
    public void addNote(Notes note){
        ContentValues cv = new ContentValues();
        mDatabase = mHelper.getWritableDatabase();
        Log.d(TAG, "addNote()");
        cv.put(NotesTable.Cols.USER_ID, note.getUserId());
        cv.put(NotesTable.Cols.TITLE, note.getTitle());
        cv.put(NotesTable.Cols.TEXT, note.getText());
        cv.put(NotesTable.Cols.ACTIVE_NOTE, note.getActiveNote());
        mDatabase.insert(NotesTable.NAME, null, cv);
        cv.clear();
    }

    //updates a note if it is active
    public void updateNote(Notes note){
        Log.i("FINAL PERSISTENCE", "updateNote()");
        mDatabase = mHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NotesTable.Cols.TITLE, note.getTitle());
        cv.put(NotesTable.Cols.TEXT, note.getText());
        Log.i("TITLE: ", note.getTitle());
        Log.i("TEXT: ", note.getText());
        Log.i("ACTIVE: ", note.getActiveNote());
        String whereClause = NotesTable.Cols.ACTIVE_NOTE + " = '" + note.getActiveNote() + "'" + " and " +
                NotesTable.Cols.USER_ID + " = '" + note.getUserId() + "'";
        Log.i("whereClause: ", whereClause);
        mDatabase.update(NotesTable.NAME, cv, whereClause, null);
        cv.clear();
        mDatabase.close();
    }

    public List<Notes> getNotes(String userId) {
        List<Notes> notes = new ArrayList<>();
        Log.d(TAG, "getNotes()");
        mDatabase = mHelper.getWritableDatabase();
        Cursor resultSet = mDatabase.rawQuery(
                "select * from " + NotesTable.NAME + " where " +
                        NotesTable.Cols.USER_ID + " = '" + userId + "';", null
        );
        resultSet.moveToFirst();
        int num = resultSet.getCount();
        if (num > 0) {
            Notes note = new Notes(
                    resultSet.getString(resultSet.getColumnIndex(NotesTable.Cols.USER_ID)),
                    resultSet.getString(resultSet.getColumnIndex(NotesTable.Cols.TITLE)),
                    resultSet.getString(resultSet.getColumnIndex(NotesTable.Cols.TEXT)),
                    resultSet.getString(resultSet.getColumnIndex(NotesTable.Cols.ACTIVE_NOTE))
            );

            notes.add(note);

            for (int i = 1; i < num; i++) {

                resultSet.moveToNext();
                note = new Notes(
                        resultSet.getString(resultSet.getColumnIndex(NotesTable.Cols.USER_ID)),
                        resultSet.getString(resultSet.getColumnIndex(NotesTable.Cols.TITLE)),
                        resultSet.getString(resultSet.getColumnIndex(NotesTable.Cols.TEXT)),
                        resultSet.getString(resultSet.getColumnIndex(NotesTable.Cols.ACTIVE_NOTE))
                );
                notes.add(note);
            }

        }

        mDatabase.close();
        return notes;
    }

    public void setActiveUser(String userName, String activeUser){
        ContentValues cv = new ContentValues();
        mDatabase = mHelper.getWritableDatabase();
        cv.put(UserTable.Cols.ACTIVE_USER, activeUser);
        String whereClause = UserTable.Cols.USER + " = '" + userName + "'";
        mDatabase.update(UserTable.NAME, cv, whereClause, null);
        cv.clear();
        mDatabase.close();

    }

    public void setActiveNote(String noteTitle, String userId){
        mDatabase = mHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NotesTable.Cols.ACTIVE_NOTE, "yes");
        String whereClause = NotesTable.Cols.USER_ID + " = '" + userId + "' and " +
                NotesTable.Cols.TITLE + " = '" + noteTitle + "'";
        mDatabase.update(NotesTable.NAME, cv, whereClause, null);
        cv.clear();
        mDatabase.close();
    }

    public void updateUserForNotes(String userName, String noteTitle){
        mDatabase = mHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NotesTable.Cols.USER_ID, userName);
        String whereClause = NotesTable.Cols.TITLE + " = '" + noteTitle + "'";
        mDatabase.update(NotesTable.NAME, cv, whereClause, null);
        cv.clear();
        mDatabase.close();
    }

    public void setInactiveNote(String noteTitle){
        mDatabase = mHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NotesTable.Cols.ACTIVE_NOTE, "no");
        String whereClause = NotesTable.Cols.TITLE + " = '" + noteTitle + "'";
        mDatabase.update(NotesTable.NAME, cv, whereClause, null);
        cv.clear();
        mDatabase.close();
    }

    public void logout(String userName){
        ContentValues cv = new ContentValues();
        mDatabase = mHelper.getWritableDatabase();
        cv.put(UserTable.Cols.ACTIVE_USER, "no");
        String whereClause = UserTable.Cols.USER + " = '" + userName + "'";
        mDatabase.update(UserTable.NAME, cv, whereClause, null);
        cv.clear();
        mDatabase.close();
    }

    public void deleteNote(String noteTitle){
        mDatabase = mHelper.getWritableDatabase();
        mDatabase.delete(NotesTable.NAME, NotesTable.Cols.TITLE + " = '" + noteTitle + "'", null);
    }
}
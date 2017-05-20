package edu.umsl.notes;

/**
 * Created by taylorfreiner on 4/21/17.
 */

public class Notes {

    private String userId;
    private String title;
    private String text;
    private String activeNote;

    public Notes(){}

    public Notes(String userId, String title, String text, String activeNote){
        this.userId = userId;
        this.title = title;
        this.text = text;
        this.activeNote = activeNote;
    }

    public String getUserId(){
        return userId;
    }

    public String getTitle(){
        return title;
    }

    public String getText(){
        return text;
    }

    public String getActiveNote(){
        return activeNote;
    }
}
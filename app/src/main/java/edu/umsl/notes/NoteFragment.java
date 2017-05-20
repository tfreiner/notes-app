package edu.umsl.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.umsl.notes.database.FinalPersistence;
import java.util.List;

/**
 * Created by taylorfreiner on 4/25/17.
 */

public class NoteFragment extends Fragment{

    private NoteListener mListener;
    private FinalPersistence mFinalPersistence;
    private Button mSaveButton;
    private Button mDeleteButton;
    private EditText mTitleEditText;
    private EditText mNoteTextEditText;
    private String mUserId;
    private String mNoteTitle;
    private String mNoteText;
    private int mIndex;
    private List<Notes> notes;
    private List<User> users;

    interface NoteListener{
        void saveNoteButtonPressed();
    }

    public void setListener(NoteListener listener){
        mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View containerView = inflater.inflate(R.layout.note_fragment, container, false);
        Bundle bundle = this.getArguments();
        mIndex = bundle.getInt("index");
        mFinalPersistence = FinalPersistence.get(getActivity());

        mUserId = mFinalPersistence.getActiveUser();

        mTitleEditText = (EditText) containerView.findViewById(R.id.title_edit_text);
        mNoteTextEditText = (EditText) containerView.findViewById(R.id.note_edit_text);
        mSaveButton = (Button) containerView.findViewById(R.id.save_button);
        mDeleteButton = (Button) containerView.findViewById(R.id.delete_button);
        mSaveButton.setOnClickListener(saveNoteButtonPressed);
        mDeleteButton.setOnClickListener(deleteNoteButtonPressed);
        if(mIndex != -1){
            notes = new ArrayList<>();
            notes = mFinalPersistence.getNotes(mUserId);
            mTitleEditText.setText(notes.get(mIndex).getTitle());
            mNoteTitle = mTitleEditText.getText().toString();
            mNoteTextEditText.setText(notes.get(mIndex).getText());
        }


        return containerView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.share_note, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_note:

                Intent sendReportIntent = new Intent(Intent.ACTION_SEND);
                sendReportIntent.setType("text/plain");
                String messageText = mTitleEditText.getText().toString() + ": " + mNoteTextEditText.getText().toString();
                sendReportIntent.putExtra(Intent.EXTRA_TEXT, messageText);
                sendReportIntent = Intent.createChooser(sendReportIntent, getString(R.string.send_note));
                startActivity(sendReportIntent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private View.OnClickListener saveNoteButtonPressed = new View.OnClickListener(){

        @Override
        public void onClick(View v){
            Notes note = new Notes(mUserId, mTitleEditText.getText().toString(), mNoteTextEditText.getText().toString(), "yes");
            Boolean noteTitleTaken = false;
            int noteLocation = -1;
            if(mFinalPersistence.notesExist()){
                notes = mFinalPersistence.getNotes(mUserId);
                for(int i = 0; i < notes.size(); i++) {
                    if (notes.get(i).getTitle().equals(mTitleEditText.getText().toString())) {
                        noteTitleTaken = true;
                        noteLocation = i;
                    }
                }
                if(noteTitleTaken && noteLocation != mIndex) {
                    Toast.makeText(getActivity(), "Title already taken.", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (mIndex == -1){
                        if(mTitleEditText.getText().toString().equals("") || mNoteTextEditText.getText().toString().equals(""))
                            Toast.makeText(getActivity(), "Please enter text in both fields.", Toast.LENGTH_SHORT).show();
                        else {
                            mFinalPersistence.addNote(note);
                            mListener.saveNoteButtonPressed();

                        }
                    }
                    else{
                        if(mTitleEditText.getText().toString().equals("") || mNoteTextEditText.getText().toString().equals(""))
                            Toast.makeText(getActivity(), "Please enter text in both fields.", Toast.LENGTH_SHORT).show();
                        else {
                            mFinalPersistence.updateNote(note);
                            mListener.saveNoteButtonPressed();
                        }
                    }

                }

            }else{
                if(mTitleEditText.getText().toString().equals("") || mNoteTextEditText.getText().toString().equals(""))
                    Toast.makeText(getActivity(), "Please enter text in both fields.", Toast.LENGTH_SHORT).show();
                else {
                    mFinalPersistence.addNote(note);
                    mListener.saveNoteButtonPressed();
                }
            }
        }

    };

    private View.OnClickListener deleteNoteButtonPressed = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mIndex == -1)
                mListener.saveNoteButtonPressed();
            else if(mNoteTitle != null) {
                mFinalPersistence.deleteNote(mNoteTitle);
                mListener.saveNoteButtonPressed();
            }
            else
                mListener.saveNoteButtonPressed();

        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("frag", "noteFrag");
    }

}
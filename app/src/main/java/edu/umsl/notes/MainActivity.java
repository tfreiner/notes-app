package edu.umsl.notes;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener, CreateAccountFragment.CreateListener, ProfileFragment.ProfileListener, NotesListFragment.NotesListener, NoteFragment.NoteListener{

    private LoginFragment mLoginFragment;
    private CreateAccountFragment mCreateAccountFragment;
    private NotesListFragment mNotesListFragment;
    private NoteFragment mNoteFragment;
    private ProfileFragment mProfileFragment;
    private String TAG = "MainActivity";
    private String frag;
    private String currentFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNoteFragment = new NoteFragment();
        mProfileFragment = new ProfileFragment();
        mLoginFragment = new LoginFragment();
        mNotesListFragment = new NotesListFragment();
        currentFrag = "LoginFragment";

        if(savedInstanceState != null) {

            frag = savedInstanceState.getString("frag");
        }

        if (savedInstanceState == null) {

            FragmentManager manager = getSupportFragmentManager();
            mLoginFragment = (LoginFragment) manager.findFragmentById(R.id.fragment_container);

            if (mLoginFragment == null) {
                mLoginFragment = new LoginFragment();
                manager.beginTransaction()
                        .add(R.id.fragment_container, mLoginFragment)
                        .commit();
            }
            mLoginFragment.setListener(this);

        } else if(frag != null && frag.equals("NotesListFragment")){
           saveNoteButtonPressed();
        } else if(frag != null && frag.equals("NoteFragment")){
            newNotePressed();
        } else if(frag != null && frag.equals("ProfileFragment")){
            profilePressed();
        }

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(TAG, "restoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
        frag = savedInstanceState.getString("frag");
    }

    @Override
    public void createButtonPressed() {
        if (mLoginFragment.isVisible()) {
            mCreateAccountFragment = new CreateAccountFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.fragment_container, mCreateAccountFragment, "CREATE")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            currentFrag = "CreateAccountFragment";
        }
        mCreateAccountFragment.setListener(this);
    }

    @Override
    public void loginButtonPressed(Boolean userSignedIn) {
        if (mLoginFragment.isVisible() || userSignedIn) {
            mNotesListFragment = new NotesListFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.fragment_container, mNotesListFragment, "NOTESLIST")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            currentFrag = "NotesListFragment";
        }
        mNotesListFragment.setListener(this);
    }

    @Override
    public void signUpButtonPressed() {
        if (mCreateAccountFragment.isVisible()) {
            mNotesListFragment = new NotesListFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.fragment_container, mNotesListFragment, "NOTESLIST")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            currentFrag = "NotesListFragment";
        }
        mNotesListFragment.setListener(this);
    }

    @Override
    public void savedButtonPressed() {
        if (mProfileFragment.isVisible()) {
            mNotesListFragment = new NotesListFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStack();
            manager.beginTransaction()
                    .replace(R.id.fragment_container, mNotesListFragment, "NOTESLIST")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            currentFrag = "NotesListFragment";
        }
        mNotesListFragment.setListener(this);
    }

    @Override
    public void logoutButtonPressed() {
        if (mProfileFragment.isVisible() || currentFrag.equals("LoginFragment")) {
            mLoginFragment = new LoginFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.fragment_container, mLoginFragment, "LOGIN")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
        mLoginFragment.setListener(this);
    }

    @Override
    public void profilePressed() {
        if (mNotesListFragment.isVisible() || frag.equals("ProfileFragment")) {
            Log.i(TAG, "profilePressed");
            mProfileFragment = new ProfileFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.fragment_container, mProfileFragment, "PROFILE")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            currentFrag = "ProfileFragment";
        }
        mProfileFragment.setListener(this);
    }

    @Override
    public void newNotePressed() {
        if (mNotesListFragment.isVisible() || frag.equals("NoteFragment")) {
            Log.i(TAG, "newNotePressed");
            mNoteFragment = new NoteFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("index", -1);
            mNoteFragment.setArguments(bundle);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.fragment_container, mNoteFragment, "NOTE")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            currentFrag = "NoteFragment";
        }
        mNoteFragment.setListener(this);
    }

    @Override
    public void
    saveNoteButtonPressed(){
        if (mNoteFragment.isVisible() || frag.equals("NotesListFragment")) {
            mNotesListFragment = new NotesListFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStack();
            manager.beginTransaction()
                    .replace(R.id.fragment_container, mNotesListFragment, "NOTESLIST")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            currentFrag = "NotesListFragment";
        }
        mNotesListFragment.setListener(this);
    }

    @Override
    public void editNotePressed(int index){
        if (mNotesListFragment.isVisible()) {
            Log.i(TAG, "editNotePressed");
            mNoteFragment = new NoteFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("index", index);
            mNoteFragment.setArguments(bundle);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.fragment_container, mNoteFragment, "NOTE")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            currentFrag = "NoteFragment";
        }
        mNoteFragment.setListener(this);
    }

    @Override
    public void onBackPressed() {
        if (currentFrag.equals("ProfileFragment")) {
            savedButtonPressed();
        } else if (currentFrag.equals("NoteFragment")){
            saveNoteButtonPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("frag", currentFrag);
    }

}
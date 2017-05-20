package edu.umsl.notes;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import edu.umsl.notes.database.FinalPersistence;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private ProfileListener mListener;
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mUsernameEditText;
    private EditText mEmailEditText;
    private Button mSaveButton;
    private Button mEditProfileButton;
    private Button mLogoutButton;
    private String mActiveUsername;
    private FinalPersistence mFinalPersistence;
    private List<User> users;
    private User user;

    interface ProfileListener {
        void savedButtonPressed();

        void logoutButtonPressed();
    }

    public void setListener(ProfileListener listener) {
        mListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View containerView = inflater.inflate(R.layout.profile_fragment, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        mFinalPersistence = FinalPersistence.get(getActivity());

        mFirstNameEditText = (EditText) containerView.findViewById(R.id.first_name_profile_edit_text);
        mLastNameEditText = (EditText) containerView.findViewById(R.id.last_name_profile_edit_text);
        mEmailEditText = (EditText) containerView.findViewById(R.id.email_profile_edit_text);
        mUsernameEditText = (EditText) containerView.findViewById(R.id.username_profile_edit_text);
        users = new ArrayList<>();

        users = FinalPersistence.get(getActivity()).getUsers();

        mActiveUsername = mFinalPersistence.getActiveUser();

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getActiveUser().equals("yes")) {
                mFirstNameEditText.setText(users.get(i).getFirst());
                mLastNameEditText.setText(users.get(i).getLast());
                mEmailEditText.setText(users.get(i).getEmail());
                mUsernameEditText.setText(users.get(i).getUsername());
                user = users.get(i);
            }
        }

        mSaveButton = (Button) containerView.findViewById(R.id.save_button);
        mEditProfileButton = (Button) containerView.findViewById(R.id.edit_profile_button);
        mLogoutButton = (Button) containerView.findViewById(R.id.logout_button);
        mEditProfileButton.setOnClickListener(editButtonPressed);
        mSaveButton.setOnClickListener(saveButtonPressed);
        mLogoutButton.setOnClickListener(logoutButtonPressed);

        return containerView;
    }

    private View.OnClickListener editButtonPressed = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mFirstNameEditText.setEnabled(true);
            mLastNameEditText.setEnabled(true);
            mEmailEditText.setEnabled(true);
            mUsernameEditText.setEnabled(true);
            mSaveButton.setVisibility(View.VISIBLE);
            mEditProfileButton.setEnabled(false);
            mLogoutButton.setEnabled(false);
        }

    };

    private View.OnClickListener saveButtonPressed = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            user = new User(mFirstNameEditText.getText().toString(), mLastNameEditText.getText().toString(), mEmailEditText.getText().toString(), mUsernameEditText.getText().toString(), null, "yes");
            FinalPersistence.get(getActivity()).updateUser(user);
            List<Notes> notes = mFinalPersistence.getNotes(mActiveUsername);
            mActiveUsername = mUsernameEditText.getText().toString();
            for (int i = 0; i < notes.size(); i++) {
                FinalPersistence.get(getActivity()).updateUserForNotes(mActiveUsername, notes.get(i).getTitle());
            }
            mListener.savedButtonPressed();
        }
    };

    private View.OnClickListener logoutButtonPressed = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mFinalPersistence.logout(user.getUsername());
            mListener.logoutButtonPressed();
        }

    };


}
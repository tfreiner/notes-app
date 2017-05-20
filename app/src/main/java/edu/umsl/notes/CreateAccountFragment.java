package edu.umsl.notes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import edu.umsl.notes.database.FinalPersistence;
import android.app.Activity;
import android.content.pm.ActivityInfo;

public class CreateAccountFragment extends Fragment {

    private CreateListener mListener;
    private EditText mFirstNameEditText;
    private EditText mLastNamesEditText;
    private EditText mEmailEditText;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private EditText mPasswordConfirmEditText;
    private Button mSignUpButton;
    private List<User> users;
    private FinalPersistence mFinalPersistence;

    interface CreateListener{
        void signUpButtonPressed();
    }

    public void setListener(CreateListener listener){
        mListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View containerView = inflater.inflate(R.layout.create_account_fragment, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mFinalPersistence = FinalPersistence.get(getActivity());
        mSignUpButton = (Button) containerView.findViewById(R.id.signup_button);
        mSignUpButton.setOnClickListener(signUpButtonPressed);
        mFirstNameEditText = (EditText) containerView.findViewById(R.id.first_name_edit_text);
        mLastNamesEditText = (EditText) containerView.findViewById(R.id.last_name_edit_text);
        mEmailEditText = (EditText) containerView.findViewById(R.id.email_edit_text);
        mUsernameEditText = (EditText) containerView.findViewById(R.id.username_edit_text);
        mPasswordEditText = (EditText) containerView.findViewById(R.id.password_edit_text);
        mPasswordConfirmEditText = (EditText) containerView.findViewById(R.id.password_confirm_edit_text);

        return containerView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Activity a = getActivity();
            if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private View.OnClickListener signUpButtonPressed = new View.OnClickListener(){

        @Override
        public void onClick(View v){

            users = new ArrayList<>();
            if(mFinalPersistence.usersExist()){
                users = mFinalPersistence.getUsers();
                Boolean userExists = false;
                for(int i = 0; i < users.size(); i++){
                    if(mUsernameEditText.getText().toString().equals(users.get(i).getUsername()))
                        userExists = true;
                }
                if(mFirstNameEditText.getText() == null || mLastNamesEditText.getText() == null || mEmailEditText.getText() == null || mUsernameEditText.getText() == null || mPasswordEditText == null || mPasswordConfirmEditText == null){
                    Toast.makeText(getActivity(), "Please enter values in all fields.", Toast.LENGTH_SHORT).show();
                }
                else if(!mPasswordEditText.getText().toString().equals(mPasswordConfirmEditText.getText().toString())){
                    Toast.makeText(getActivity(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
                }
                else if(userExists){
                    Toast.makeText(getActivity(), "That username is taken.", Toast.LENGTH_SHORT).show();
                }
                else{
                    User user = new User(mFirstNameEditText.getText().toString(), mLastNamesEditText.getText().toString(), mEmailEditText.getText().toString(), mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString(), "yes");
                    mFinalPersistence.addUser(user);
                    mListener.signUpButtonPressed();
                }

            }else {
                if(mFirstNameEditText.getText() == null || mLastNamesEditText.getText() == null || mEmailEditText.getText() == null || mUsernameEditText.getText() == null || mPasswordEditText == null || mPasswordConfirmEditText == null){
                    Toast.makeText(getActivity(), "Please enter values in all fields.", Toast.LENGTH_SHORT).show();
                }
                else if(!mPasswordEditText.getText().toString().equals(mPasswordConfirmEditText.getText().toString())){
                    Toast.makeText(getActivity(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
                }
                else{
                    User user = new User(mFirstNameEditText.getText().toString(), mLastNamesEditText.getText().toString(), mEmailEditText.getText().toString(), mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString(), "yes");
                    mFinalPersistence.addUser(user);
                    mListener.signUpButtonPressed();
                }

            }

        }

    };


}
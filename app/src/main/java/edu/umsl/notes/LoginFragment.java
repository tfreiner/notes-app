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
import android.util.Log;
import edu.umsl.notes.database.FinalPersistence;
import java.util.ArrayList;
import java.util.List;
import android.content.pm.ActivityInfo;
public class LoginFragment extends Fragment {

    private LoginListener mListener;
    private Button mLoginButton;
    private Button mCreateButton;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private List<User> users;
    private FinalPersistence mFinalPersistence;

    interface LoginListener{
        void loginButtonPressed(Boolean userSignedIn);
        void createButtonPressed();
    }

    public void setListener(LoginListener listener){
        mListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View containerView = inflater.inflate(R.layout.login_fragment, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mFinalPersistence = FinalPersistence.get(getActivity());
        users = new ArrayList<>();
        if(mFinalPersistence.usersExist() && mFinalPersistence.isUserLoggedIn()){
            users = mFinalPersistence.getUsers();
            for(int i = 0; i < users.size(); i++) {
                if (users.get(i).getActiveUser().equals("yes"))
                    mListener.loginButtonPressed(true);
            }
        }

        mLoginButton = (Button) containerView.findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(loginButtonPressed);
        mUsernameEditText = (EditText) containerView.findViewById(R.id.username_edit_text);
        mPasswordEditText = (EditText) containerView.findViewById(R.id.password_edit_text);

        mCreateButton = (Button) containerView.findViewById(R.id.signup_button);
        mCreateButton.setOnClickListener(createButtonPressed);

        return containerView;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private View.OnClickListener loginButtonPressed = new View.OnClickListener(){

        @Override
        public void onClick(View v){

            if(mFinalPersistence.usersExist()) {
                users = mFinalPersistence.getUsers();
                int userFound = 0;
                int userLocation = -1;
                for(int i = 0; i < users.size(); i++){
                    if(users.get(i).getUsername().equals(mUsernameEditText.getText().toString())){
                        userFound = 1;
                        userLocation = i;
                    }
                }
                if(userFound == 0){
                    Toast.makeText(getActivity(), "User not found.", Toast.LENGTH_SHORT).show();
                }
                else if(users.get(userLocation).getUsername().equals(mUsernameEditText.getText().toString()) && !users.get(userLocation).getPassword().equals(mPasswordEditText.getText().toString())){
                    Toast.makeText(getActivity(), "Incorrect password.", Toast.LENGTH_SHORT).show();
                }
                else{
                    mFinalPersistence.setActiveUser(users.get(userLocation).getUsername(), "yes");
                    mListener.loginButtonPressed(false);
                }
            }
            else{
                Toast.makeText(getActivity(), "User not found.", Toast.LENGTH_SHORT).show();
            }
        }

    };

    private View.OnClickListener createButtonPressed = new View.OnClickListener(){

        @Override
        public void onClick(View v){
            Log.i("onClick:", "createButtonPressed");
            mListener.createButtonPressed();
        }
    };

    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.e("TAG", "DESTROY Fragment1");
    }

}

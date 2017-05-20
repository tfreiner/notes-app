package edu.umsl.notes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.MenuInflater;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import edu.umsl.notes.database.FinalPersistence;
import android.content.pm.ActivityInfo;

public class NotesListFragment extends Fragment {

    private String TAG = "NotesListFragment";
    private FinalPersistence mFinalPersistence;
    private RecyclerView mNotesRecyclerView;
    private NotesAdapter mAdapter;
    private List<Notes> notes;
    private NotesListener mListener;
    private String mUserId;

    public void setListener(NotesListener listener){
        mListener = listener;
    }

    interface NotesListener{
        void profilePressed();
        void newNotePressed();
        void editNotePressed(int index);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        View containerView = inflater.inflate(R.layout.notes_list_fragment, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        mFinalPersistence = FinalPersistence.get(getActivity());
        mUserId = mFinalPersistence.getActiveUser();
        mNotesRecyclerView = (RecyclerView)containerView.findViewById(R.id.notes_recycler_view);
        mNotesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        notes = new ArrayList<>();
        updateUI();
        return containerView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.notes_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_note:
                mListener.newNotePressed();
                return true;
            case R.id.profile:
                mListener.profilePressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI(){
        notes = mFinalPersistence.getNotes(mUserId);
        if(mAdapter == null){
            mAdapter = new NotesAdapter(notes);
            mNotesRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setNotes(notes);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesHolder> {

        private List<Notes> mDataSet;

        public NotesAdapter(List<Notes> myDataSet) {
            mDataSet = myDataSet;
        }

        @Override
        public NotesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.notes_list, parent, false);
            NotesHolder holder = new NotesListFragment.NotesAdapter.NotesHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(NotesHolder holder, int position) {
            if (mDataSet != null) {
                holder.bindNotes(mDataSet.get(position).getTitle());
            }
        }

        @Override
        public int getItemCount() {
            if (mDataSet != null) {
                return mDataSet.size();
            }
            return 0;
        }

        public void setNotes(List<Notes> notes) {
            mDataSet = notes;
        }

        public class NotesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView mNoteTitle;

            public NotesHolder(View v) {
                super(v);
                mNoteTitle = (TextView) itemView.findViewById(R.id.notes_title);
                v.setOnClickListener(this);
            }

            public void bindNotes(String noteTitle) {
                mNoteTitle.setText(noteTitle);

            }

            @Override
            public void onClick(View v){
                clickedItemAtIndex(getAdapterPosition());
            }

        }
    }

    public void clickedItemAtIndex(int index){
        Log.i("CLICK", "HOLDER CLICKED");
        for(int i = 0; i < notes.size(); i++){
            mFinalPersistence.setInactiveNote(notes.get(i).getTitle());
        }
        mFinalPersistence.setActiveNote(notes.get(index).getTitle(), mUserId);
        mListener.editNotePressed(index);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putString("frag", "NotesListFragment");
        outState.putString("user", mUserId);
    }

}
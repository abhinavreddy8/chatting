package com.example.chatting30.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatting30.R;
import com.example.chatting30.Users;
import com.example.chatting30.useradapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class userFragment extends Fragment {

    private RecyclerView recyclerView;
    private useradapter userAdapter;
    private List<Users> mUsers;
    EditText search_users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();
        readUsers();

        search_users = view.findViewById(R.id.search_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void searchUsers(String s) {

        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
                .startAt(s)
                .endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear(); // Clear the list first

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users user = snapshot.getValue(Users.class);

                    if (user != null && user.getId() != null && !user.getId().equals(fuser.getUid())) {
                        mUsers.add(user);
                    }
                }

                // Initialize or update the adapter
                if (userAdapter == null) {
                    userAdapter = new useradapter(getContext(), mUsers, false);
                    recyclerView.setAdapter(userAdapter);
                } else {
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Log the error if it occurs
                Log.e("FirebaseError", "Error: " + databaseError.getMessage());
            }
        });
    }

    private void readUsers() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear(); // Clear the list first

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users user = snapshot.getValue(Users.class);

                    // Check if user or user.getId() is null
                    if (user != null && user.getId() != null) {
                        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
                        // Check if fuser and fuser.getUid() are not null before comparing
                        if (fuser != null && fuser.getUid() != null && !user.getId().equals(fuser.getUid())) {
                            mUsers.add(user);
                        }
                    }
                }

                // Initialize or update the adapter
                if (userAdapter == null) {
                    userAdapter = new useradapter(getContext(), mUsers, false);
                    recyclerView.setAdapter(userAdapter);
                } else {
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Log the error if it occurs
                Log.e("FirebaseError", "Error: " + databaseError.getMessage());
            }
        });
    }
}

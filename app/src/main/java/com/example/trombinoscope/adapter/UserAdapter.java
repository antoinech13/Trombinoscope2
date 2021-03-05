package com.example.trombinoscope.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.trombinoscope.R;
import com.example.trombinoscope.dataStructure.Trombi;
import com.example.trombinoscope.dataStructure.User;
import com.example.trombinoscope.view.TrombViewHolder;
import com.example.trombinoscope.view.UserViewHolder;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

    // FOR DATA
    private List<User> Users;

    // CONSTRUCTOR
    public UserAdapter(List<User> Users) {
        this.Users = Users;
    }

    private int position;


    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.popup_droits_layout, parent, false);

        return new UserViewHolder(view);
    }




    @Override
    public void onBindViewHolder(UserViewHolder viewHolder, int position) {
        viewHolder.updateWithUser(this.Users.get(position));
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(viewHolder.getPosition());
                return false;
            }
        });

    }
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
        return this.Users.size();
    }

    public User getTrombi(int position){
        return this.Users.get(position);
    }
}

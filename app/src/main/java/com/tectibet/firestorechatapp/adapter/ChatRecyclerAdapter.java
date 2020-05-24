package com.tectibet.firestorechatapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.tectibet.firestorechatapp.ChatActivity;
import com.tectibet.firestorechatapp.R;
import com.tectibet.firestorechatapp.domain.Chat;

import java.util.List;

/**
 * Created by kharag on 23-05-2020.
 */
public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Chat> mChatList;
    FirebaseAuth mAuth;
    public ChatRecyclerAdapter(Context context, List<Chat> mChatList) {
        this.context= context;
        this.mChatList= mChatList;
        mAuth =FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view =null;
       if(viewType==0){
           view = LayoutInflater.from(context).inflate(R.layout.from_ui,parent,false);
           return new SenderViewHolder(view);
       }
            view = LayoutInflater.from(context).inflate(R.layout.to_ui,parent,false);
            return new ReceiverViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()==0){
            SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
            senderViewHolder.mText.setText(mChatList.get(position).getMessage());
        }
        if(holder.getItemViewType()==1){
            ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder) holder;
            receiverViewHolder.mText.setText(mChatList.get(position).getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mChatList.get(position).getFrom().equals(mAuth.getCurrentUser().getUid())){
            return 0;
        }
        return 1;
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder{
        private TextView mText;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.from_chat);
        }
    }
    public class ReceiverViewHolder extends RecyclerView.ViewHolder{
        private TextView mText;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.to_chat);
        }
    }
}

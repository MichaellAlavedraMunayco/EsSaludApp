package com.aplicacion.essalud.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aplicacion.essalud.R;
import com.aplicacion.essalud.models.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessageAdapter extends BaseAdapter {

    private List<Message> messages = new ArrayList<Message>();
    private LayoutInflater layoutInflater;
    private Context context;

    public MessageAdapter(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void add(Message message) {
        this.messages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = null;
        Message message = messages.get(i);
        if (message.getIssuerName().equals(Message.CHATBOT)) {
            view = layoutInflater.inflate(R.layout.their_message, null);
            TextView txvIssuerName = (TextView) view.findViewById(R.id.txvIssuerName);
            TextView txvMessageChatBot = (TextView) view.findViewById(R.id.txvMessageChatBot);
            txvIssuerName.setText(message.getIssuerName());
            txvMessageChatBot.setText(message.getMessage());
        } else if (message.getIssuerName().equals(Message.USER)) {
            view = layoutInflater.inflate(R.layout.my_message, null);
            TextView txvMessageClient = (TextView) view.findViewById(R.id.txvMessageClient);
            txvMessageClient.setText(message.getMessage());
        }
        return view;
    }

}
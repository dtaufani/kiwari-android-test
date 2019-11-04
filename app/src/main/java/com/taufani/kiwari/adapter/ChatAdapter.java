package com.taufani.kiwari.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taufani.kiwari.R;
import com.taufani.kiwari.model.ChatModel;
import com.taufani.kiwari.utilities.DateParser;

import java.util.List;

/**
 * Created by dtaufani@gmail.com on 28/10/19.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<ChatModel> mChatModels;

    public ChatAdapter(Context context, List<ChatModel> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        this.mChatModels = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_message, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final ChatModel model = mChatModels.get(position);
        viewHolder.mSenderName.setText(model.getSenderName());
        viewHolder.mMessage.setText(model.getMessage());
        viewHolder.mCreatedDate.setText(DateParser.formatDate(
                "dd-MM-yyyy (HH:mm:ss)",
                model.getCreatedDate()
        ));
    }


    @Override
    public int getItemCount() {
        return mChatModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mSenderName;
        private TextView mCreatedDate;
        private TextView mMessage;

        public ViewHolder(View view) {
            super(view);
            this.mSenderName = (TextView) view.findViewById(R.id.textview_name);
            this.mCreatedDate = (TextView) view.findViewById(R.id.textview_created_date);
            this.mMessage = (TextView) view.findViewById(R.id.textview_message);
        }
    }
}

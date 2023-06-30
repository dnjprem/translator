package com.voicetranslator.translate.languagetranslator.adapter;

import android.annotation.SuppressLint;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.voicetranslator.translate.R;
import com.voicetranslator.translate.languagetranslator.model.Conversation;
import com.voicetranslator.translate.languagetranslator.utils.ConvertConversationTextToSpeech;
import com.voicetranslator.translate.languagetranslator.utils.GeneralPreference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Object> conversationList;
    private long mSourceLastClickTime = 0;
    private long mTargetLastClickTime = 0;


    private static final int TYPE_ITEM_DATE_CONTAINER = 0; // for DateTime View
    private static final int TYPE_ITEM_CONVERSATION = 1; // for Conversation View

    public ConversationAdapter(ArrayList<Conversation> conversationList) {
        this.conversationList = GeneralPreference.sortConversions(conversationList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == TYPE_ITEM_DATE_CONTAINER) {
            view = layoutInflater.inflate(R.layout.item_view_conversation_time, parent, false);
            return new DateContainerViewHolder(view);
        } else {
            view = layoutInflater.inflate(R.layout.item_view_conversation, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (conversationList.get(position) instanceof Conversation) {
            Conversation item = (Conversation) conversationList.get(position);
            MyViewHolder holder = (MyViewHolder) viewHolder;

            if (viewHolder.getItemViewType() == TYPE_ITEM_CONVERSATION) {
                if (item.getIsSource() == 0) {

                    holder.layout_target.setVisibility(View.GONE);
                    holder.text_target_time.setVisibility(View.GONE);

                    // Set Source Item data to left side
                    holder.layout_source.setVisibility(View.VISIBLE);
                    holder.text_source_time.setVisibility(View.VISIBLE);
                    holder.text_first_source_code.setText(item.getSourceCode().toUpperCase());
                    holder.text_first_source_text.setText(item.getSourceText());
                    holder.text_first_target_code.setText(item.getTargetCode().toUpperCase());
                    holder.text_first_target_text.setText(item.getTargetText());
                    holder.text_source_time.setText(getTime(item.getSourceDate()));
                    holder.img_first_source_speak.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mSourceLastClickTime < 1500) {
                            return;
                        }
                        mSourceLastClickTime = SystemClock.elapsedRealtime();
                        pressedOnClick(view, item.getSourceCode(), item.getSourceText());
                    });
                    holder.img_first_target_speak.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mTargetLastClickTime < 1500) {
                            return;
                        }
                        mTargetLastClickTime = SystemClock.elapsedRealtime();
                        pressedOnClick(view, item.getTargetCode(), item.getTargetText());
                    });
                }

                if (item.getIsSource() == 1) {

                    holder.layout_source.setVisibility(View.GONE);
                    holder.text_source_time.setVisibility(View.GONE);

                    // Set Target Item data to right side
                    holder.layout_target.setVisibility(View.VISIBLE);
                    holder.text_target_time.setVisibility(View.VISIBLE);
                    holder.text_second_source_code.setText(item.getSourceCode().toUpperCase());
                    holder.text_second_source_text.setText(item.getSourceText());
                    holder.text_second_target_code.setText(item.getTargetCode().toUpperCase());
                    holder.text_second_target_text.setText(item.getTargetText());
                    holder.text_target_time.setText(getTime(item.getTargetDate()));
                    holder.img_second_source_speak.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mSourceLastClickTime < 1500) {
                            return;
                        }
                        mSourceLastClickTime = SystemClock.elapsedRealtime();
                        pressedOnClick(view, item.getSourceCode(), item.getSourceText());
                    });
                    holder.img_second_target_speak.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mTargetLastClickTime < 1500) {
                            return;
                        }
                        mTargetLastClickTime = SystemClock.elapsedRealtime();
                        pressedOnClick(view, item.getTargetCode(), item.getTargetText());
                    });
                }
            }


        } else if (conversationList.get(position) instanceof String) {
            DateContainerViewHolder holder = (DateContainerViewHolder) viewHolder;
            String item = (String) conversationList.get(position);
            holder.text_date_time.setText(item);
        }

    }

    private void pressedOnClick(View view, String code, String text) {
        new ConvertConversationTextToSpeech(view.getContext(), code, text);
    }

    @Override
    public int getItemViewType(int position) {
        if (conversationList.get(position) instanceof Conversation) {
            return TYPE_ITEM_CONVERSATION;
        } else {
            return TYPE_ITEM_DATE_CONTAINER;
        }
    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    public static class DateContainerViewHolder extends RecyclerView.ViewHolder {
        TextView text_date_time;

        public DateContainerViewHolder(@NonNull View itemView) {
            super(itemView);
            text_date_time = itemView.findViewById(R.id.text_date_time);
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout_source;
        TextView text_first_source_code;
        TextView text_first_source_text;
        TextView text_first_target_code;
        TextView text_first_target_text;
        TextView text_source_time;
        ImageView img_first_source_speak;
        ImageView img_first_target_speak;

        LinearLayout layout_target;
        TextView text_second_source_code;
        TextView text_second_source_text;
        TextView text_second_target_code;
        TextView text_second_target_text;
        TextView text_target_time;
        ImageView img_second_source_speak;
        ImageView img_second_target_speak;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // Source Data
            layout_source = itemView.findViewById(R.id.layout_source);
            text_first_source_code = itemView.findViewById(R.id.text_first_source_code);
            text_first_source_text = itemView.findViewById(R.id.text_first_source_text);
            text_first_target_code = itemView.findViewById(R.id.text_first_target_code);
            text_first_target_text = itemView.findViewById(R.id.text_first_target_text);
            text_source_time = itemView.findViewById(R.id.text_source_time);
            img_first_source_speak = itemView.findViewById(R.id.img_first_source_speak);
            img_first_target_speak = itemView.findViewById(R.id.img_first_target_speak);

            // Target Data
            layout_target = itemView.findViewById(R.id.layout_target);
            text_second_source_code = itemView.findViewById(R.id.text_second_source_code);
            text_second_source_text = itemView.findViewById(R.id.text_second_source_text);
            text_second_target_code = itemView.findViewById(R.id.text_second_target_code);
            text_second_target_text = itemView.findViewById(R.id.text_second_target_text);
            text_target_time = itemView.findViewById(R.id.text_target_time);
            img_second_source_speak = itemView.findViewById(R.id.img_second_source_speak);
            img_second_target_speak = itemView.findViewById(R.id.img_second_target_speak);
        }
    }

    public void addRecord(Conversation conversation) {
        GeneralPreference.addConversation(conversationList, conversation);
        notifyItemInserted(conversationList.size() - 1);
    }


    @SuppressLint("SimpleDateFormat")
    private String getTime(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        try {
            Date date = dateFormat.parse(time);
            assert date != null;
            timeFormat.format(date);
            return timeFormat.format(date);
        } catch (Exception e) {
            Log.d("=========> Time ERR ", e.getMessage());
            return "00:00";
        }
    }

}

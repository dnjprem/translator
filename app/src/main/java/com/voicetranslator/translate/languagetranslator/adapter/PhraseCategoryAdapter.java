package com.voicetranslator.translate.languagetranslator.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.voicetranslator.translate.R;
import com.voicetranslator.translate.languagetranslator.listener.TranslateListener;
import com.voicetranslator.translate.languagetranslator.model.Language;
import com.voicetranslator.translate.languagetranslator.model.PhraseWord;
import com.voicetranslator.translate.languagetranslator.utils.ConvertTextToSpeech;
import com.voicetranslator.translate.languagetranslator.utils.DataTranslator;
import com.voicetranslator.translate.languagetranslator.utils.GeneralPreference;
import com.preference.PowerPreference;
import com.preference.Preference;

import java.util.ArrayList;

public class PhraseCategoryAdapter extends RecyclerView.Adapter<PhraseCategoryAdapter.MyViewHolder> {


    ArrayList<PhraseWord> words;
    String sourceCode;
    String targetCode;
    Preference preference = PowerPreference.getFileByName("preferenceName");
    Language targetLanguagePhrase;
    ClipboardManager clipboard;
    ConvertTextToSpeech convertTextToSpeech;

    public PhraseCategoryAdapter(ConvertTextToSpeech convertTextToSpeech, ArrayList<PhraseWord> words, String sourceCode, String targetCode) {
        this.convertTextToSpeech = convertTextToSpeech;
        this.words = words;
        this.sourceCode = sourceCode;
        this.targetCode = targetCode;
        targetLanguagePhrase = preference.getObject("targetLanguagePhrase", Language.class, GeneralPreference.getDefaultTarget());
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_view_essentials, parent, false);
        clipboard = (ClipboardManager) parent.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        return new MyViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PhraseWord item = words.get(position);

        if (sourceCode.equalsIgnoreCase("en")) {
            holder.sourceText.setText(item.getSourceText());
        } else {
            holder.sourceText.setText(item.getTranslatedSourceText());
        }
        holder.txt_show_target_language.setText(targetLanguagePhrase.getDisplayName());

        if (item.isTranslating()) {
            holder.targetText.setText(R.string.text_translating);
        } else if (item.isTranslated()) {
            holder.targetText.setText(words.get(position).getTargetText());
        } else if (item.isError()) {
            holder.targetText.setText("");
        }

        if (item.isExpanded()) {
            holder.img_arrow.setRotation(180f);
            holder.layout_expand.setVisibility(View.VISIBLE);
        } else {
            holder.img_arrow.setRotation(0f);
            holder.layout_expand.setVisibility(View.GONE);
        }

        holder.card_source.setOnClickListener(view -> {
            item.setExpanded(!words.get(position).isExpanded());
            notifyItemChanged(position);

            if (words.get(position).isExpanded() && !words.get(position).isTranslated()) {
                DataTranslator.translate(sourceCode, targetCode, words.get(position).getSourceText(), new TranslateListener() {
                    @Override
                    public void onStart() {
                        item.setTranslating(true);
                        notifyItemChanged(position);
                    }

                    @Override
                    public void onFailed(String message) {
                        item.setTranslating(false);
                        item.setTranslated(false);
                        notifyItemChanged(position);
                    }

                    @Override
                    public void onResult(String result) {
                        item.setTranslating(false);
                        item.setTranslated(true);
                        item.setTargetText(result);
                        notifyItemChanged(position);
                    }
                });
            }
        });

        holder.img_copy_target.setOnClickListener(view -> {
            ClipData clip = ClipData.newPlainText("sourceText", item.getTargetText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(holder.itemView.getContext(), R.string.text_copied, Toast.LENGTH_SHORT).show();
        });

        holder.img_source_speak.setOnClickListener(view -> {
            if (item.isTranslated()) {
                convertTextToSpeech.textToSpeak(item.getTargetText());
//                new ConvertTextToSpeech(view.getContext(), targetCode, item.getTargetText());
            }
        });
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView sourceText;
        TextView targetText;
        TextView txt_show_target_language;
        ImageView img_arrow;
        ImageView img_source_speak;
        ImageView img_copy_target;
        ConstraintLayout layout_expand;
        MaterialCardView card_source;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            sourceText = itemView.findViewById(R.id.sourceText);
            targetText = itemView.findViewById(R.id.targetText);
            txt_show_target_language = itemView.findViewById(R.id.txt_show_target_language);
            img_arrow = itemView.findViewById(R.id.img_arrow);
            img_source_speak = itemView.findViewById(R.id.img_source_speak);
            img_copy_target = itemView.findViewById(R.id.img_copy_target);
            layout_expand = itemView.findViewById(R.id.layout_expand);
            card_source = itemView.findViewById(R.id.card_source);
        }
    }

}

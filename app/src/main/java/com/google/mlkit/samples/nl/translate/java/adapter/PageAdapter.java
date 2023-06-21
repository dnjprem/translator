package com.google.mlkit.samples.nl.translate.java.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.mlkit.samples.nl.translate.java.fragment.PhraseCategoryFragment;
import com.google.mlkit.samples.nl.translate.java.model.PhraseBook;


public class PageAdapter extends FragmentStateAdapter {

    Context context;
    PhraseBook phraseBook;

    public PageAdapter(@NonNull FragmentActivity fragmentActivity, PhraseBook phraseBook) {
        super(fragmentActivity);
        this.phraseBook = phraseBook;
        this.context = fragmentActivity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return PhraseCategoryFragment.newInstance(phraseBook.getCategories().get(position),phraseBook.getSourceLanguageCode(),phraseBook.getTargetLanguageCode());
    }

    @Override
    public int getItemCount() {
        return phraseBook.getCategories().size();
    }
}

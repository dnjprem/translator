package com.google.mlkit.samples.nl.translate.java.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.mlkit.samples.nl.translate.R;
import com.google.mlkit.samples.nl.translate.java.adapter.PageAdapter;
import com.google.mlkit.samples.nl.translate.java.listener.TranslateListener;
import com.google.mlkit.samples.nl.translate.java.model.Category;
import com.google.mlkit.samples.nl.translate.java.model.Language;
import com.google.mlkit.samples.nl.translate.java.model.PhraseBook;
import com.google.mlkit.samples.nl.translate.java.model.PhraseWord;
import com.google.mlkit.samples.nl.translate.java.utils.DataTranslator;
import com.google.mlkit.samples.nl.translate.java.utils.GeneralPreference;
import com.preference.PowerPreference;
import com.preference.Preference;

import java.util.ArrayList;

public class EssentialActivity extends RootActivity {
    ImageView img_back;
    TabLayout layout_tab;
    ViewPager2 view_pager_category;
    PageAdapter fragmentAdapter;
    Language sourceLanguagePhrase = preference.getObject("sourceLanguagePhrase", Language.class, GeneralPreference.getDefaultSource());
    Language targetLanguagePhrase = preference.getObject("targetLanguagePhrase", Language.class, GeneralPreference.getDefaultTarget());
    PhraseBook phraseBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_essential);
        phraseBook = getEnglishPhraseBook();

        GeneralPreference.showStatusBar(this, R.color.colorPrimaryLight);
        // to make status bar icon dark
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        img_back = findViewById(R.id.img_back);
        layout_tab = findViewById(R.id.layout_tab);
        view_pager_category = findViewById(R.id.view_pager_category);

        fragmentAdapter = new PageAdapter(this, phraseBook);
        view_pager_category.setAdapter(fragmentAdapter);

        new TabLayoutMediator(layout_tab, view_pager_category, (tab, position) -> tab.setText(phraseBook.getCategories().get(position).getCategoryName())).attach();

        img_back.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });

    }

    private PhraseBook getEnglishPhraseBook() {
        //Greetings
        ArrayList<PhraseWord> greetingWords = new ArrayList<>();
        greetingWords.add(new PhraseWord("Hello", false, ""));
        greetingWords.add(new PhraseWord("Hi", false, ""));
        greetingWords.add(new PhraseWord("Good Morning", false, ""));
        greetingWords.add(new PhraseWord("Good Afternoon", false, ""));
        greetingWords.add(new PhraseWord("Good Evening", false, ""));
        greetingWords.add(new PhraseWord("How are you?", false, ""));
        greetingWords.add(new PhraseWord("What's up?", false, ""));
        greetingWords.add(new PhraseWord("Nice to meet you", false, ""));
        greetingWords.add(new PhraseWord("It's a pleasure to meet you", false, ""));
        greetingWords.add(new PhraseWord("How have you been?", false, ""));
        ArrayList<Category> categoryArrayList = new ArrayList<>();

        categoryArrayList.add(new Category(getString(R.string.text_greetings), greetingWords));

        //Basic
        ArrayList<PhraseWord> basicWords = new ArrayList<>();
        basicWords.add(new PhraseWord("Please", false, ""));
        basicWords.add(new PhraseWord("Thank you", false, ""));
        basicWords.add(new PhraseWord("I'm sorry", false, ""));
        basicWords.add(new PhraseWord("Excuse me", false, ""));
        categoryArrayList.add(new Category(getString(R.string.text_basic), basicWords));

        return new PhraseBook(categoryArrayList, sourceLanguagePhrase.getCode(), targetLanguagePhrase.getCode());
    }

}
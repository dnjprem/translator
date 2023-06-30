package com.voicetranslator.translate.languagetranslator.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.voicetranslator.translate.R;
import com.voicetranslator.translate.languagetranslator.adapter.PhraseCategoryAdapter;
import com.voicetranslator.translate.languagetranslator.listener.TranslateListener;
import com.voicetranslator.translate.languagetranslator.model.Category;
import com.voicetranslator.translate.languagetranslator.model.PhraseWord;
import com.voicetranslator.translate.languagetranslator.utils.ConvertTextToSpeech;
import com.voicetranslator.translate.languagetranslator.utils.DataTranslator;

public class PhraseCategoryFragment extends Fragment {

    RecyclerView essential_rcv;
    ShimmerFrameLayout shimmerFrameLayout;
    Category category;
    public String sourceLanguageCode, targetLanguageCode;
    Handler mainHandler = new Handler(Looper.getMainLooper());
    ConvertTextToSpeech convertTextToSpeech;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String strCategory = getArguments().getString("basic");
            sourceLanguageCode = getArguments().getString("sourceLanguageCode");
            targetLanguageCode = getArguments().getString("targetLanguageCode");
            category = new Gson().fromJson(strCategory, Category.class);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_phrase_category, container, false);
        essential_rcv = view.findViewById(R.id.essential_rcv);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        shimmerFrameLayout.startShimmer();
        translateLanguage();
        mainHandler.postDelayed(() -> bindDataToAdapter(view.getContext()), 3000);
        return view;
    }

    public static PhraseCategoryFragment newInstance(Category category, String sourceLanguageCode, String targetLanguageCode) {
        Bundle args = new Bundle();
        PhraseCategoryFragment fragment = new PhraseCategoryFragment();
        args.putString("basic", new Gson().toJson(category));
        args.putString("sourceLanguageCode", sourceLanguageCode);
        args.putString("targetLanguageCode", targetLanguageCode);
        fragment.setArguments(args);
        return fragment;
    }


    private void bindDataToAdapter(Context context) {
        convertTextToSpeech = new ConvertTextToSpeech(context, targetLanguageCode);
        essential_rcv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        PhraseCategoryAdapter adapter = new PhraseCategoryAdapter(convertTextToSpeech, category.getWords(), sourceLanguageCode, targetLanguageCode);
        shimmerFrameLayout.setVisibility(View.GONE);
        essential_rcv.setVisibility(View.VISIBLE);
        essential_rcv.setAdapter(adapter);
    }

    private void translateLanguage() {
        new Thread(() -> {
            try {
                if (!sourceLanguageCode.equalsIgnoreCase("en")) {
                    for (PhraseWord item : category.getWords()) {
                        DataTranslator.translate("en", sourceLanguageCode, item.getSourceText(), new TranslateListener() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onFailed(String message) {

                            }

                            @Override
                            public void onResult(String result) {
                                item.setTranslatedSourceText(result);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

//    @Override
//    public void onDestroyView() {
//        convertTextToSpeech.destroyCovertTextToSpeech();
//        Log.d("============> DestView ", "TextToSpeech onDestroyView() ");
//        super.onDestroyView();
//    }

    @Override

    public void onDestroy() {
        convertTextToSpeech.destroyCovertTextToSpeech();
        Log.d("============> Destroy ", "TextToSpeech onDestroy() ");
        super.onDestroy();
    }

//    @Override
//    public void onPause() {
//        convertTextToSpeech.destroyCovertTextToSpeech();
//        Log.d("============> Pause ", "TextToSpeech onPause() ");
//        super.onPause();
//    }
//
//    @Override
//    public void onResume() {
//        convertTextToSpeech = new ConvertTextToSpeech(this.getContext(),targetLanguageCode);
//        Log.d("============> Resume ", "TextToSpeech onResume() ");
//        super.onResume();
//    }
}


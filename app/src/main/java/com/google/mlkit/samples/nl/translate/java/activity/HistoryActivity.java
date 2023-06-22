package com.google.mlkit.samples.nl.translate.java.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.mlkit.samples.nl.translate.R;
import com.google.mlkit.samples.nl.translate.java.adapter.HistoryAdapter;
import com.google.mlkit.samples.nl.translate.java.database.DataBaseHelper;
import com.google.mlkit.samples.nl.translate.java.listener.FavoriteClickListener;
import com.google.mlkit.samples.nl.translate.java.model.History;
import com.google.mlkit.samples.nl.translate.java.utils.GeneralPreference;
import com.theophrast.ui.widget.SquareImageView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements FavoriteClickListener {

    ImageView img_back;
    TextView text_screen_title;
    LinearLayout layout_background;
    SquareImageView img_fore_ground;
    TextView text_title;
    TextView text_sub_title;
    DataBaseHelper helper;
    ArrayList<History> histories = new ArrayList<>();
    ArrayList<History> favoriteList = new ArrayList<>();
    RecyclerView history_rcv;
    HistoryAdapter adapter;
    boolean isFavoriteScreen = false; // [False] 0 -> No Favorite, [True] 1 -> Favorite


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        helper = new DataBaseHelper(this);
        GeneralPreference.showStatusBar(this, R.color.colorPrimaryLight);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);  // to make status bar icon dark
        }
        isFavoriteScreen = getIntent().getBooleanExtra("isFavoriteScreen", false);
        histories = helper.getAllHistory();
        img_back = findViewById(R.id.img_back);
        text_screen_title = findViewById(R.id.text_screen_title);
        layout_background = findViewById(R.id.layout_background);
        img_fore_ground = findViewById(R.id.img_fore_ground);
        text_title = findViewById(R.id.text_title);
        text_sub_title = findViewById(R.id.text_sub_title);
        history_rcv = findViewById(R.id.history_rcv);


        if (isFavoriteScreen) {
            setScreenData();
            for (History item : histories) {
                if (item.getIsFavorite() == 1) {
                    favoriteList.add(item);
                }
            }

            if (favoriteList.size() > 0) {
                layout_background.setVisibility(View.GONE);
            }
            adapter = new HistoryAdapter(favoriteList, this);

        } else {
            if (histories.size() > 0) {
                layout_background.setVisibility(View.GONE);
            }
            adapter = new HistoryAdapter(histories, this);
        }

        history_rcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        history_rcv.setAdapter(adapter);


        img_back.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });

    }

    private void setScreenData() {
        if (isFavoriteScreen) {
            // for Favorite Screen
            text_screen_title.setText(R.string.text_favorite);
            img_fore_ground.setImageResource(R.drawable.fg_favorite_screen);
            text_title.setText(R.string.text_no_favorite);
            text_sub_title.setText(R.string.text_make_translation_favorite);

        } else {
            // for History Screen
            text_screen_title.setText(R.string.text_history);
            img_fore_ground.setImageResource(R.drawable.fg_history_screen);
            text_title.setText(R.string.text_no_history);
            text_sub_title.setText(R.string.text_make_translation_history);
        }
    }

    @Override
    public void onClick(ArrayList<Object> historyObject, int id, int isFav, int position) {
        if (isFavoriteScreen) {
            removeFavoriteHistory(historyObject, id, isFav, position);
        } else {
            helper.updateHistoryData(id, isFav);
            adapter.notifyItemChanged(position);
        }
        if (adapter.getItemCount() == 0) {
            layout_background.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void removeFavoriteHistory(ArrayList<Object> historyObject, int id, int isFav, int position) {
        int favoriteItem = getFavoriteItemCount(historyObject, position);
        helper.updateHistoryData(id, isFav);
        if (favoriteItem == 1) {
            historyObject.remove(position);
            historyObject.remove(position - 1);
        } else if (favoriteItem > 1) {
            historyObject.remove(position);
        }
        adapter.notifyDataSetChanged();
    }

    private int getFavoriteItemCount(ArrayList<Object> historyObject, int position) {
        int dateCount = 0;
        if (historyObject.get(position) instanceof History) {
            History item = (History) historyObject.get(position);

            for (Object o : historyObject) {
                if (o instanceof History) {
                    History that = (History) o;
                    if (GeneralPreference.format(that.getSourceDate()).equals(GeneralPreference.format(item.getSourceDate()))) {
                        dateCount++;
                    }
                }
            }
            Log.d("=================> ", "Date Count is - " + dateCount);
        }
        return dateCount;
    }
}
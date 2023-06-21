package com.google.mlkit.samples.nl.translate.java.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.google.mlkit.samples.nl.translate.R;
import com.google.mlkit.samples.nl.translate.java.model.OnBoarding;

import java.util.ArrayList;

public class OnBoardingAdapter extends PagerAdapter {

    Context context;
    ArrayList<OnBoarding> onBoardings;

    public OnBoardingAdapter(Context context, ArrayList<OnBoarding> onBoardings) {
        this.context = context;
        this.onBoardings = onBoardings;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        OnBoarding item = onBoardings.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_view_on_boarding, null);

        ImageView img_onboard = itemView.findViewById(R.id.img_onboard);
        TextView text_onboard_title = itemView.findViewById(R.id.text_onboard_title);
        TextView text_first_sub_title = itemView.findViewById(R.id.text_first_sub_title);
        TextView text_second_sub_title = itemView.findViewById(R.id.text_second_sub_title);

        img_onboard.setImageResource(item.getImage());
        text_onboard_title.setText(item.getTitle());
        text_first_sub_title.setText(item.getFirstSubTitle());
        text_second_sub_title.setText(item.getSecondSubTitle());

        container.addView(itemView);
        return itemView;
    }

    @Override
    public int getCount() {
        return onBoardings.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

package com.google.mlkit.samples.nl.translate.java.listener;

import java.util.ArrayList;

public interface FavoriteClickListener {

    void onClick(ArrayList<Object> historyObject, int id, int isFav, int position);
}

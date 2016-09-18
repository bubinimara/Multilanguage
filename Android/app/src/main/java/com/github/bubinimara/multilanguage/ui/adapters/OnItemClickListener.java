package com.github.bubinimara.multilanguage.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

/**
 * Created by davide on 15/09/16.
 */
public interface OnItemClickListener<T extends RecyclerView.ViewHolder> {
    void onItemClick(@NonNull T holder);
}

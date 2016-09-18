package com.github.bubinimara.multilanguage.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.bubinimara.multilanguage.R;
import com.github.bubinimara.multilanguage.model.CategoryModel;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by davide on 15/09/16.
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesHolder> {

    private ArrayList<CategoryModel> items = new ArrayList<>();
    private LayoutInflater layoutInflater;

    OnItemClickListener<CategoriesHolder> listener;

    public CategoriesAdapter(Context context, OnItemClickListener<CategoriesHolder> listener) {
        layoutInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public CategoriesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_categories,parent,false);
        return new CategoriesHolder.Builder(view).setListener(listener).build();
    }

    @Override
    public void onBindViewHolder(CategoriesHolder holder, int position) {
        holder.set(getItem(position));
    }

    private CategoryModel getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    public void setItems(Collection<CategoryModel> categoryModels){
        items.clear();
        if(categoryModels == null )
            return;

        items.addAll(categoryModels);
    }

    public OnItemClickListener<CategoriesHolder> getOnItemClickListener() {
        return listener;
    }

    public void setOnItemClickListener(OnItemClickListener<CategoriesHolder> onItemClickListener) {
        this.listener = onItemClickListener;
    }
}

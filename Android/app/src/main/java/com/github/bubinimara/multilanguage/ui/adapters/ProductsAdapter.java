package com.github.bubinimara.multilanguage.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.bubinimara.multilanguage.R;
import com.github.bubinimara.multilanguage.model.ProductModel;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by davide on 15/09/16.
 */
public class ProductsAdapter extends RecyclerView.Adapter<ProducHolder> {
    ArrayList<ProductModel> items = new ArrayList<>();

    LayoutInflater layoutInflater;
    private OnItemClickListener<ProducHolder> onItemClickListener;

    public ProductsAdapter(Context context,OnItemClickListener<ProducHolder> onItemClickListener) {
        layoutInflater = LayoutInflater.from(context);
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ProducHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ProducHolder holder = new ProducHolder(layoutInflater.inflate(R.layout.row_products,parent,false));
        holder.setListener(onItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ProducHolder holder, int position) {
        holder.set(getItem(position));
    }

    private ProductModel getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(Collection<ProductModel> products) {
        items.clear();
        if(products == null)
            return;
        items.addAll(products);

    }
}

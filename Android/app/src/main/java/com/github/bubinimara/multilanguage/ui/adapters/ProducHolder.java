package com.github.bubinimara.multilanguage.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.bubinimara.multilanguage.R;
import com.github.bubinimara.multilanguage.model.ProductModel;

/**
 * Created by davide on 15/09/16.
 */
public class ProducHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    View root;
    TextView textViewName;
    TextView textViewDecsription;

    private OnItemClickListener<ProducHolder> listener;
    private ProductModel product;

    @Override
    public void onClick(View view) {
        listener.onItemClick(this);
    }

    public ProducHolder(View itemView) {
        super(itemView);
        root = itemView;
        textViewName = (TextView) itemView.findViewById(R.id.textViewName);
        textViewDecsription = (TextView) itemView.findViewById(R.id.textViewDescritpion);
    }

    public void setListener(OnItemClickListener<ProducHolder> onItemClickListener) {
        this.listener = onItemClickListener;
        root.setOnClickListener(this);
    }

    void set(ProductModel product){
        this.product = product;
        textViewName.setText(product.getName());
        textViewDecsription.setText(product.getDescription());
    }

    public ProductModel getProduct() {
        return product;
    }

    public static class Builder{
        ProducHolder holder;

        public Builder(View view) {
            holder = new ProducHolder(view);
        }
        public Builder setListener(OnItemClickListener<ProducHolder> listener){
            holder.setListener(listener);
            return this;

        }
        public ProducHolder build(){
            return holder;
        }
    }
}

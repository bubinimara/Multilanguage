package com.github.bubinimara.multilanguage.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.bubinimara.multilanguage.R;
import com.github.bubinimara.multilanguage.model.LanguageModel;

import java.util.ArrayList;

/**
 * Created by davide on 17/09/16.
 */
public class LanguageAdapter extends BaseAdapter {

    private ArrayList<LanguageModel> items =  new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    public LanguageAdapter(@NonNull  Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public Context getContext() {
        return context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public LanguageModel getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return items.get(i).getId();
    }

    public void setItems(ArrayList<LanguageModel> languages) {
        items.clear();
        if(languages==null)
            return;
        items.addAll(languages);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if(view == null){
            view = layoutInflater.inflate(R.layout.row_language,viewGroup,false);
            holder = new Holder(view);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }
        holder.set(getItem(i));

        return view;
    }

    private static class Holder{
        View root;
        TextView textView;

        public Holder(View view) {
            this.root = view;

            textView = (TextView) view.findViewById(R.id.textView);
        }

        void set(LanguageModel item){
            textView.setText(item.getName());
        }
    }
}

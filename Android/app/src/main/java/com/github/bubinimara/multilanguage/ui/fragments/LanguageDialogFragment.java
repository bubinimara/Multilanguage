package com.github.bubinimara.multilanguage.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.bubinimara.multilanguage.App;
import com.github.bubinimara.multilanguage.R;
import com.github.bubinimara.multilanguage.model.LanguageModel;
import com.github.bubinimara.multilanguage.task.LanguageListLoader;
import com.github.bubinimara.multilanguage.ui.adapters.LanguageAdapter;

import java.util.ArrayList;

/**
 * Created by davide on 17/09/16.
 */
public class LanguageDialogFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<ArrayList<LanguageModel>>, AdapterView.OnItemClickListener {
    private LanguageAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new LanguageAdapter(getContext());
        getLoaderManager().initLoader(3,null,this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_languages,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle("Select Language");
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

    }

    @Override
    public Loader<ArrayList<LanguageModel>> onCreateLoader(int id, Bundle args) {
        return new LanguageListLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<LanguageModel>> loader, ArrayList<LanguageModel> data) {
        adapter.setItems(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<LanguageModel>> loader) {
        adapter.setItems(null);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        App.getConfigurations().setLanguage(adapter.getItem(i).getId());
        dismiss();
    }
}

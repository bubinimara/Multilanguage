package com.github.bubinimara.multilanguage.ui.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.bubinimara.multilanguage.R;
import com.github.bubinimara.multilanguage.model.CategoryModel;
import com.github.bubinimara.multilanguage.model.ProductModel;
import com.github.bubinimara.multilanguage.task.ProductListLoader;
import com.github.bubinimara.multilanguage.ui.adapters.OnItemClickListener;
import com.github.bubinimara.multilanguage.ui.adapters.ProducHolder;
import com.github.bubinimara.multilanguage.ui.adapters.ProductsAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<ProductModel>>,OnItemClickListener<ProducHolder> {
    private static final String ARG_CATEGORY_ID = "categoryId";

    private RecyclerView recyclerView;

    private int categoryId;
    private ProductsAdapter adapter;


    public ProductsFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment
     * @param category Category
     * @return A new instance of fragment ProductsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductsFragment newInstance(CategoryModel category) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_ID, category.getId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getInt(ARG_CATEGORY_ID);
        }
        adapter = new ProductsAdapter(getContext(),this);
        getLoaderManager().initLoader(categoryId,null,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

    }

    @Override
    public Loader<ArrayList<ProductModel>> onCreateLoader(int id, Bundle args) {
        return new ProductListLoader(getContext(),categoryId);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ProductModel>> loader, ArrayList<ProductModel> data) {
        adapter.setItems(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ProductModel>> loader) {
        adapter.setItems(null);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(@NonNull ProducHolder holder) {
        //
    }
}

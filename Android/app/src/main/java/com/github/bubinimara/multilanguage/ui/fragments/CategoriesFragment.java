package com.github.bubinimara.multilanguage.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.bubinimara.multilanguage.R;
import com.github.bubinimara.multilanguage.model.CategoryModel;
import com.github.bubinimara.multilanguage.task.CategoryListLoader;
import com.github.bubinimara.multilanguage.ui.adapters.CategoriesAdapter;
import com.github.bubinimara.multilanguage.ui.adapters.CategoriesHolder;
import com.github.bubinimara.multilanguage.ui.adapters.OnItemClickListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoriesFragmentListener} interface
 * to handle interaction events.
 */
public class CategoriesFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<CategoryModel>>,OnItemClickListener<CategoriesHolder> {

    public static final String TAG = CategoriesFragment.class.getSimpleName();

    private CategoriesFragmentListener mListener;

    private RecyclerView recyclerView;
    private CategoriesAdapter adapter;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CategoriesAdapter(getContext(),this);

        getLoaderManager().initLoader(1, null, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CategoriesFragmentListener) {
            mListener = (CategoriesFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CategoriesFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<ArrayList<CategoryModel>> onCreateLoader(int id, Bundle args) {
        return new CategoryListLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<CategoryModel>> loader, ArrayList<CategoryModel> data) {
        Log.d(TAG, "onLoadFinished: "+data.size());
        adapter.setItems(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<CategoryModel>> loader) {
        adapter.setItems(null);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(CategoriesHolder holder) {
        Log.d(TAG, "onItemClick: " +holder.getCategory().getName());
        mListener.onCategorySelected(holder.getCategory());
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface CategoriesFragmentListener {
        // TODO: Update argument type and name
        void onCategorySelected(CategoryModel category);
    }
}

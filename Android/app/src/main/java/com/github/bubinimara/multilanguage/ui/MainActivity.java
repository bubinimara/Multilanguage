package com.github.bubinimara.multilanguage.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.bubinimara.multilanguage.R;
import com.github.bubinimara.multilanguage.database.DatabaseManager;
import com.github.bubinimara.multilanguage.database.Session;
import com.github.bubinimara.multilanguage.model.CategoryModel;
import com.github.bubinimara.multilanguage.ui.fragments.CategoriesFragment;
import com.github.bubinimara.multilanguage.ui.fragments.LanguageDialogFragment;
import com.github.bubinimara.multilanguage.ui.fragments.ProductsFragment;

public class MainActivity extends AppCompatActivity implements CategoriesFragment.CategoriesFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState == null){
            showCategories();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                showLanguagesDialog();
                break;
            case R.id.action_refresh:
                showRefreshDialog();
                break;
            default:
                return super.onOptionsItemSelected(item);

        }
        return true;
    }

    private void showRefreshDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title_refresh);
        builder.setMessage(R.string.dialog_message_refresh)
                .setPositiveButton(R.string.dialog_button_clear_and_downloaded, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clearAndExit();
                    }
                })
                .setNegativeButton(R.string.dialog_button_cancel,null)
                .show();
    }

    private void showLanguagesDialog() {
        DialogFragment dialogFragment = new LanguageDialogFragment();
        dialogFragment.show(getSupportFragmentManager(),LanguageDialogFragment.class.getSimpleName());
    }

    private void showCategories() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content,new CategoriesFragment())
                .commit();
    }

    private void showProducts(CategoryModel category) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, ProductsFragment.newInstance(category))
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onCategorySelected(@NonNull CategoryModel category) {
        showProducts(category);
    }



    private void clearAndExit() {
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseManager databaseManager = new DatabaseManager(getApplicationContext());
                databaseManager.clearAll();

                Session.clearAll(getApplicationContext());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent(MainActivity.this,SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                MainActivity.this.finish();

            }
        }.execute();
    }


}

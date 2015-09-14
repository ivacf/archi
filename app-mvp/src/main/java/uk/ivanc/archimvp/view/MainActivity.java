package uk.ivanc.archimvp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import uk.ivanc.archimvp.R;
import uk.ivanc.archimvp.model.Repository;
import uk.ivanc.archimvp.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity {

    private MainPresenter presenter;

    private RecyclerView reposRecycleView;
    private Toolbar toolbar;
    private EditText editTextUsername;
    private ProgressBar progressBar;
    private TextView infoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set up presenter
        presenter = new MainPresenter();
        presenter.attachView(this);

        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        infoTextView = (TextView) findViewById(R.id.text_info);
        //Set up ToolBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Set up RecyclerView
        reposRecycleView = (RecyclerView) findViewById(R.id.repos_recycler_view);
        setupRecyclerView(reposRecycleView);
        //Set up username EditText
        editTextUsername = (EditText) findViewById(R.id.edit_text_username);
        editTextUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String username = editTextUsername.getText().toString();
                    if (username.length() > 0) presenter.loadRepositories(username);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    public void onLoading() {
        progressBar.setVisibility(View.VISIBLE);
        reposRecycleView.setVisibility(View.GONE);
        infoTextView.setVisibility(View.GONE);
    }

    public void onRepositoriesLoaded(List<Repository> repositories) {
        progressBar.setVisibility(View.GONE);
        if (!repositories.isEmpty()) {
            RepositoryAdapter adapter =
                    (RepositoryAdapter) reposRecycleView.getAdapter();
            adapter.setRepositories(repositories);
            adapter.notifyDataSetChanged();
            reposRecycleView.requestFocus();
            hideSoftKeyboard();
            reposRecycleView.setVisibility(View.VISIBLE);
        } else {
            infoTextView.setText(R.string.text_empty_repos);
            infoTextView.setVisibility(View.VISIBLE);
        }
    }

    public void onUsernameNotFound() {
        progressBar.setVisibility(View.GONE);
        infoTextView.setText(R.string.error_username_not_found);
        infoTextView.setVisibility(View.VISIBLE);
    }

    public void onError() {
        progressBar.setVisibility(View.GONE);
        infoTextView.setText(R.string.error_loading_repos);
        infoTextView.setVisibility(View.VISIBLE);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        RepositoryAdapter adapter = new RepositoryAdapter();
        adapter.setCallback(new RepositoryAdapter.Callback() {
            @Override
            public void onItemClick(Repository repository) {
                startActivity(RepositoryActivity.newIntent(MainActivity.this, repository));
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextUsername.getWindowToken(), 0);
    }

}

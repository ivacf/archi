package uk.ivanc.archi;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import uk.ivanc.archi.model.GithubService;
import uk.ivanc.archi.model.Repository;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Subscription subscription;
    private RecyclerView reposRecycleView;
    private Toolbar toolbar;
    private EditText editTextUsername;
    private ProgressBar progressBar;
    private TextView infoTextView;
    private ImageButton searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        infoTextView = (TextView) findViewById(R.id.text_info);
        //Set up ToolBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Set up RecyclerView
        reposRecycleView = (RecyclerView) findViewById(R.id.repos_recycler_view);
        setupRecyclerView(reposRecycleView);
        // Set up search button
        searchButton = (ImageButton) findViewById(R.id.button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGithubRepos(editTextUsername.getText().toString());
            }
        });
        //Set up username EditText
        editTextUsername = (EditText) findViewById(R.id.edit_text_username);
        editTextUsername.addTextChangedListener(mHideShowButtonTextWatcher);
        editTextUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String username = editTextUsername.getText().toString();
                    if (username.length() > 0) loadGithubRepos(username);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) subscription.unsubscribe();
    }

    public void loadGithubRepos(String username) {
        progressBar.setVisibility(View.VISIBLE);
        reposRecycleView.setVisibility(View.GONE);
        infoTextView.setVisibility(View.GONE);
        ArchiApplication application = ArchiApplication.get(this);
        GithubService githubService = application.getGithubService();
        subscription = githubService.publicRepositories(username)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new Subscriber<List<Repository>>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.GONE);
                        if (reposRecycleView.getAdapter().getItemCount() > 0) {
                            reposRecycleView.requestFocus();
                            hideSoftKeyboard();
                            reposRecycleView.setVisibility(View.VISIBLE);
                        } else {
                            infoTextView.setText(R.string.text_empty_repos);
                            infoTextView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.e(TAG, "Error loading GitHub repos ", error);
                        progressBar.setVisibility(View.GONE);
                        if (error instanceof HttpException
                                && ((HttpException) error).code() == 404) {
                            infoTextView.setText(R.string.error_username_not_found);
                        } else {
                            infoTextView.setText(R.string.error_loading_repos);
                        }
                        infoTextView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(List<Repository> repositories) {
                        Log.i(TAG, "Repos loaded " + repositories);
                        RepositoryAdapter adapter =
                                (RepositoryAdapter) reposRecycleView.getAdapter();
                        adapter.setRepositories(repositories);
                        adapter.notifyDataSetChanged();
                    }
                });
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

    private TextWatcher mHideShowButtonTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            searchButton.setVisibility(charSequence.length() > 0 ? View.VISIBLE : View.GONE);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

}

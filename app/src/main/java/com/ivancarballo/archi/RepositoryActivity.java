package com.ivancarballo.archi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.ivancarballo.archi.model.Repository;

public class RepositoryActivity extends AppCompatActivity {

    private static final String EXTRA_REPOSITORY = "EXTRA_REPOSITORY";

    private Toolbar toolbar;
    private TextView descriptionTextView;

    public static Intent newIntent(Context context, Repository repository) {
        Intent intent = new Intent(context, RepositoryActivity.class);
        intent.putExtra(EXTRA_REPOSITORY, repository);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        descriptionTextView = (TextView) findViewById(R.id.text_repo_description);

        Repository repository = getIntent().getParcelableExtra(EXTRA_REPOSITORY);

        setTitle(repository.name);
        descriptionTextView.setText(repository.description);
    }
}

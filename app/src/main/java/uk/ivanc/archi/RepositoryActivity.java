package uk.ivanc.archi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import uk.ivanc.archi.model.GithubService;
import uk.ivanc.archi.model.Repository;
import uk.ivanc.archi.model.User;

public class RepositoryActivity extends AppCompatActivity {

    private static final String EXTRA_REPOSITORY = "EXTRA_REPOSITORY";
    private static final String TAG = "RepositoryActivity";

    private Toolbar toolbar;
    private TextView descriptionText;
    private TextView homepageText;
    private TextView languageText;
    private TextView forkText;
    private TextView ownerNameText;
    private TextView ownerEmailText;
    private TextView ownerLocationText;
    private ImageView ownerImage;
    private View ownerLayout;

    private Subscription subscription;

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
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        descriptionText = (TextView) findViewById(R.id.text_repo_description);
        homepageText = (TextView) findViewById(R.id.text_homepage);
        languageText = (TextView) findViewById(R.id.text_language);
        forkText = (TextView) findViewById(R.id.text_fork);
        ownerNameText = (TextView) findViewById(R.id.text_owner_name);
        ownerEmailText = (TextView) findViewById(R.id.text_owner_email);
        ownerLocationText = (TextView) findViewById(R.id.text_owner_location);
        ownerImage = (ImageView) findViewById(R.id.image_owner);
        ownerLayout = findViewById(R.id.layout_owner);

        Repository repository = getIntent().getParcelableExtra(EXTRA_REPOSITORY);
        bindRepositoryData(repository);
        loadFullUser(repository.owner.url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) subscription.unsubscribe();
    }

    private void bindRepositoryData(final Repository repository) {
        setTitle(repository.name);
        descriptionText.setText(repository.description);
        homepageText.setText(repository.homepage);
        homepageText.setVisibility(repository.hasHomepage() ? View.VISIBLE : View.GONE);
        languageText.setText(getString(R.string.text_language, repository.language));
        languageText.setVisibility(repository.hasLanguage() ? View.VISIBLE : View.GONE);
        forkText.setVisibility(repository.isFork() ? View.VISIBLE : View.GONE);
        //Preload image for user because we already have it before loading the full user
        Picasso.with(this)
                .load(repository.owner.avatarUrl)
                .placeholder(R.drawable.placeholder)
                .into(ownerImage);
    }

    private void bindOwnerData(final User owner) {
        ownerNameText.setText(owner.name);
        ownerEmailText.setText(owner.email);
        ownerEmailText.setVisibility(owner.hasEmail() ? View.VISIBLE : View.GONE);
        ownerLocationText.setText(owner.location);
        ownerLocationText.setVisibility(owner.hasLocation() ? View.VISIBLE : View.GONE);
    }


    private void loadFullUser(String url) {
        ArchiApplication application = ArchiApplication.get(this);
        GithubService githubService = application.getGithubService();
        subscription = githubService.userFromUrl(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        Log.i(TAG, "Full user data loaded " + user);
                        bindOwnerData(user);
                        ownerLayout.setVisibility(View.VISIBLE);
                    }
                });
    }
}

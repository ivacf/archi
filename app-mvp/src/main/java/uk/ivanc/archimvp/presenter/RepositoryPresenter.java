package uk.ivanc.archimvp.presenter;

import android.util.Log;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import uk.ivanc.archimvp.ArchiApplication;
import uk.ivanc.archimvp.model.GithubService;
import uk.ivanc.archimvp.model.User;
import uk.ivanc.archimvp.view.RepositoryActivity;

public class RepositoryPresenter implements Presenter<RepositoryActivity> {

    private static final String TAG = "RepositoryPresenter";

    private RepositoryActivity repositoryActivity;
    private Subscription subscription;

    @Override
    public void attachView(RepositoryActivity view) {
        this.repositoryActivity = view;
    }

    @Override
    public void detachView() {
        this.repositoryActivity = null;
        if (subscription != null) subscription.unsubscribe();
    }

    public void loadOwner(String userUrl) {
        GithubService githubService = ArchiApplication.get(repositoryActivity).getGithubService();
        subscription = githubService.userFromUrl(userUrl)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        Log.i(TAG, "Full user data loaded " + user);
                        repositoryActivity.showOwner(user);
                    }
                });
    }
}

package uk.ivanc.archimvp.presenter;

import android.util.Log;

import java.util.List;

import retrofit.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.ivanc.archimvp.view.MainActivity;
import uk.ivanc.archimvp.model.GithubService;
import uk.ivanc.archimvp.model.Repository;

public class MainPresenter implements Presenter<MainActivity> {

    public static String TAG = "MainPresenter";

    private GithubService githubService;
    private MainActivity mainActivity;
    private Subscription subscription;
    private List<Repository> repositories;

    public MainPresenter() {
        githubService = GithubService.Factory.create();
    }

    @Override
    public void attachView(MainActivity view) {
        this.mainActivity = view;
    }

    @Override
    public void detachView() {
        this.mainActivity = null;
        if (subscription != null) subscription.unsubscribe();
    }

    public void loadRepositories(String username) {
        mainActivity.onLoading();
        if (subscription != null) subscription.unsubscribe();
        subscription = githubService.publicRepositories(username)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Repository>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "Repos loaded " + repositories);
                        mainActivity.onRepositoriesLoaded(repositories);
                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.e(TAG, "Error loading GitHub repos ", error);
                        if (isHttp404(error)) {
                            mainActivity.onUsernameNotFound();
                        } else {
                            mainActivity.onError();
                        }
                    }

                    @Override
                    public void onNext(List<Repository> repositories) {
                        MainPresenter.this.repositories = repositories;
                    }
                });
    }

    private static boolean isHttp404(Throwable error) {
        return error instanceof HttpException && ((HttpException) error).code() == 404;
    }

}

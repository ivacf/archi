package uk.ivanc.archimvp.presenter;

import uk.ivanc.archimvp.view.RepositoryActivity;

public class RepositoryPresenter implements Presenter<RepositoryActivity> {

    private RepositoryActivity repositoryActivity;

    @Override
    public void attachView(RepositoryActivity view) {
        this.repositoryActivity = view;
    }

    @Override
    public void detachView() {
        this.repositoryActivity = null;
    }
}

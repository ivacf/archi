package uk.ivanc.archimvp.view;

import java.util.List;

import uk.ivanc.archi.model.Repository;

public interface MainMvpView extends MvpView {

    void showRepositories(List<Repository> repositories);

    void showMessage(int stringId);

    void showProgressIndicator();
}

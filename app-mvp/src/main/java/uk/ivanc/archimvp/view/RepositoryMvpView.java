package uk.ivanc.archimvp.view;

import uk.ivanc.archimvp.model.User;

public interface RepositoryMvpView extends MvpView {

    void showOwner(final User owner);

}

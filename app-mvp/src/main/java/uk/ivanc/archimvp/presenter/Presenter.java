package uk.ivanc.archimvp.presenter;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

}

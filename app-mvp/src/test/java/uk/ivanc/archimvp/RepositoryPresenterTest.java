package uk.ivanc.archimvp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import rx.Observable;
import rx.schedulers.Schedulers;
import uk.ivanc.archimvp.model.GithubService;
import uk.ivanc.archimvp.model.User;
import uk.ivanc.archimvp.presenter.RepositoryPresenter;
import uk.ivanc.archimvp.util.MockModelFabric;
import uk.ivanc.archimvp.view.RepositoryMvpView;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RepositoryPresenterTest {
    RepositoryPresenter repositoryPresenter;
    RepositoryMvpView repositoryMvpView;
    GithubService githubService;

    @Before
    public void setUp() {
        ArchiApplication application = (ArchiApplication) RuntimeEnvironment.application;
        githubService = mock(GithubService.class);
        // Mock the retrofit service so we don't call the API directly
        application.setGithubService(githubService);
        // Change the default subscribe schedulers so all observables
        // will now run on the same thread
        application.setDefaultSubscribeScheduler(Schedulers.immediate());
        repositoryPresenter = new RepositoryPresenter();
        repositoryMvpView = mock(RepositoryMvpView.class);
        when(repositoryMvpView.getContext()).thenReturn(application);
        repositoryPresenter.attachView(repositoryMvpView);
    }

    @After
    public void tearDown() {
        repositoryPresenter.detachView();
    }

    @Test
    public void loadOwnerCallsShowOwner() {
        User owner = MockModelFabric.newUser("ivan");
        String userUrl = "http://user.com/more";
        when(githubService.userFromUrl(userUrl))
                .thenReturn(Observable.just(owner));

        repositoryPresenter.loadOwner(userUrl);
        verify(repositoryMvpView).showOwner(owner);
    }
}

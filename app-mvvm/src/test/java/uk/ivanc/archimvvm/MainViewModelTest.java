package uk.ivanc.archimvvm;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.schedulers.Schedulers;
import uk.ivanc.archimvvm.model.GithubService;
import uk.ivanc.archimvvm.model.Repository;
import uk.ivanc.archimvvm.util.MockModelFabric;
import uk.ivanc.archimvvm.viewmodel.MainViewModel;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MainViewModelTest {

    GithubService githubService;
    ArchiApplication application;
    MainViewModel mainViewModel;
    MainViewModel.DataListener dataListener;

    @Before
    public void setUp() {
        githubService = mock(GithubService.class);
        dataListener = mock(MainViewModel.DataListener.class);
        application = (ArchiApplication) RuntimeEnvironment.application;
        // Mock the retrofit service so we don't call the API directly
        application.setGithubService(githubService);
        // Change the default subscribe schedulers so all observables
        // will now run on the same thread
        application.setDefaultSubscribeScheduler(Schedulers.immediate());
        mainViewModel = new MainViewModel(application, dataListener);
    }


    @Test
    public void shouldSearchUsernameWithRepos() {
        String username = "usernameWithRepos";
        TextView textView = new TextView(application);
        textView.setText(username);
        List<Repository> mockRepos = MockModelFabric.newListOfRepositories(10);
        doReturn(rx.Observable.just(mockRepos)).when(githubService).publicRepositories(username);

        mainViewModel.onSearchAction(textView, EditorInfo.IME_ACTION_SEARCH, null);
        verify(dataListener).onRepositoriesChanged(mockRepos);
        assertEquals(mainViewModel.infoMessageVisibility.get(), View.INVISIBLE);
        assertEquals(mainViewModel.progressVisibility.get(), View.INVISIBLE);
        assertEquals(mainViewModel.recyclerViewVisibility.get(), View.VISIBLE);
    }

    @Test
    public void shouldSearchInvalidUsername() {
        String username = "invalidUsername";
        TextView textView = new TextView(application);
        textView.setText(username);
        HttpException mockHttpException =
                new HttpException(Response.error(404, mock(ResponseBody.class)));
        when(githubService.publicRepositories(username))
                .thenReturn(Observable.<List<Repository>>error(mockHttpException));

        mainViewModel.onSearchAction(textView, EditorInfo.IME_ACTION_SEARCH, null);
        verify(dataListener, never()).onRepositoriesChanged(anyListOf(Repository.class));
        assertEquals(mainViewModel.infoMessage.get(),
                application.getString(R.string.error_username_not_found));
        assertEquals(mainViewModel.infoMessageVisibility.get(), View.VISIBLE);
        assertEquals(mainViewModel.progressVisibility.get(), View.INVISIBLE);
        assertEquals(mainViewModel.recyclerViewVisibility.get(), View.INVISIBLE);
    }

    @Test
    public void shouldSearchUsernameWithNoRepos() {
        String username = "usernameWithoutRepos";
        TextView textView = new TextView(application);
        textView.setText(username);
        when(githubService.publicRepositories(username))
                .thenReturn(Observable.just(Collections.<Repository>emptyList()));

        mainViewModel.onSearchAction(textView, EditorInfo.IME_ACTION_SEARCH, null);
        verify(dataListener).onRepositoriesChanged(Collections.<Repository>emptyList());
        assertEquals(mainViewModel.infoMessage.get(),
                application.getString(R.string.text_empty_repos));
        assertEquals(mainViewModel.infoMessageVisibility.get(), View.VISIBLE);
        assertEquals(mainViewModel.progressVisibility.get(), View.INVISIBLE);
        assertEquals(mainViewModel.recyclerViewVisibility.get(), View.INVISIBLE);
    }

}

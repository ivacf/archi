package uk.ivanc.archi2017

import android.app.Application
import android.content.Context

import rx.Scheduler
import rx.schedulers.Schedulers
import uk.ivanc.archi2017.model.GithubService

class ArchiApplication : Application() {

    //For setting mocks during testing
    val githubService: GithubService by lazy { GithubService.create() }
    val defaultSubscribeScheduler: Scheduler by lazy { Schedulers.io() }
    val viewModelFactory: ViewModelFactory by lazy { ViewModelFactory(githubService) }

    companion object {

        operator fun get(context: Context): ArchiApplication {
            return context.applicationContext as ArchiApplication
        }
    }
}

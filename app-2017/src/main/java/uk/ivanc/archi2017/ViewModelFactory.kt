package uk.ivanc.archi2017

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import uk.ivanc.archi2017.model.GithubService


class ViewModelFactory(private val githubService: GithubService) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            MainActivityViewModel::class.java -> MainActivityViewModel(githubService) as T
            else -> modelClass.newInstance()
        }
    }

}
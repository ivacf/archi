package uk.ivanc.archi2017

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.ivanc.archi2017.model.GithubService
import uk.ivanc.archi2017.model.Repository

class MainActivityViewModel(private val githubService: GithubService) : ViewModel() {

    fun getPublicRepositories(username: String): LiveData<List<Repository>> {
        val mutableData = MutableLiveData<List<Repository>>()
        githubService.publicRepositories(username).enqueue(object : Callback<List<Repository>> {
            override fun onFailure(call: Call<List<Repository>>, t: Throwable) {

            }

            override fun onResponse(call: Call<List<Repository>>,
                                    response: Response<List<Repository>>) {
                if (response.isSuccessful) {
                    mutableData.value = response.body()
                }
            }
        })
        return mutableData
    }
}
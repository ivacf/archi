package uk.ivanc.archi2017.model

import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url
import rx.Observable

interface GithubService {

    @GET("users/{username}/repos")
    fun publicRepositories(@Path("username") username: String): Observable<List<Repository>>

    @GET
    fun userFromUrl(@Url userUrl: String): Observable<User>

    companion object {
        fun create(): GithubService {
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build()
            return retrofit.create(GithubService::class.java)
        }
    }
}

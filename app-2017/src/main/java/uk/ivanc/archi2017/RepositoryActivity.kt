package uk.ivanc.archi2017

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_repository.*

import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import uk.ivanc.archi.R
import uk.ivanc.archi2017.model.Repository
import uk.ivanc.archi2017.model.User

class RepositoryActivity : AppCompatActivity() {
    private var subscription: Subscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val repository = intent.getParcelableExtra<Repository>(EXTRA_REPOSITORY)
        bindRepositoryData(repository)
        loadFullUser(repository.owner.url)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (subscription != null) {
            subscription!!.unsubscribe()
        }
    }

    private fun bindRepositoryData(repository: Repository) {
        title = repository.name
        descriptionText.text = repository.description
        homepageText.text = repository.homepage
        homepageText.visibility = if (repository.homepage.isNullOrBlank()) View.GONE else View.VISIBLE
        languageText.text = getString(R.string.text_language, repository.language)
        languageText.visibility = if (repository.language.isNullOrBlank()) View.GONE else View.VISIBLE
        forkText.visibility = if (repository.isFork) View.VISIBLE else View.GONE
        //Preload image for user because we already have it before loading the full user
        Picasso.with(this)
                .load(repository.owner.avatarUrl)
                .placeholder(R.drawable.placeholder)
                .into(ownerImage)
    }

    private fun bindOwnerData(owner: User) {
        ownerNameText.text = owner.name
        ownerEmailText.text = owner.email
        ownerEmailText.visibility = if (owner.email.isNullOrBlank()) View.GONE else View.VISIBLE
        ownerLocationText.text = owner.location
        ownerLocationText.visibility = if (owner.location.isNullOrBlank()) View.GONE else View.VISIBLE
    }


    private fun loadFullUser(url: String) {
        val application = ArchiApplication[this]
        val githubService = application.githubService
        subscription = githubService.userFromUrl(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler)
                .subscribe { user ->
                    Log.i(TAG, "Full user data loaded " + user)
                    bindOwnerData(user)
                    ownerLayout.visibility = View.VISIBLE
                }
    }

    companion object {

        private const val EXTRA_REPOSITORY = "EXTRA_REPOSITORY"
        private const val TAG = "RepositoryActivity"

        fun newIntent(context: Context, repository: Repository): Intent {
            val intent = Intent(context, RepositoryActivity::class.java)
            intent.putExtra(EXTRA_REPOSITORY, repository)
            return intent
        }
    }
}

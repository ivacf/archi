package uk.ivanc.archi2017

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import uk.ivanc.archi.R
import uk.ivanc.archi2017.model.Repository

class RepositoryAdapter(
        var repositories: List<Repository> = emptyList(),
        private val onItemClicked: ((Repository) -> Unit)
) : RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_repo, parent, false)
        return RepositoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val repository = repositories[position]
        val context = holder.titleTextView.context
        holder.titleTextView.text = repository.name
        holder.descriptionTextView.text = repository.description
        holder.watchersTextView.text = context.getString(R.string.text_watchers, repository.watchers)
        holder.starsTextView.text = context.getString(R.string.text_stars, repository.stars)
        holder.forksTextView.text = context.getString(R.string.text_forks, repository.forks)
    }

    override fun getItemCount(): Int {
        return repositories.size
    }

    inner class RepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentLayout: View = itemView.findViewById<View>(R.id.layout_content)
        val titleTextView: TextView = itemView.findViewById<TextView>(R.id.text_repo_title)
        val descriptionTextView: TextView = itemView.findViewById<TextView>(R.id.text_repo_description)
        val watchersTextView: TextView = itemView.findViewById<TextView>(R.id.text_watchers)
        var starsTextView: TextView = itemView.findViewById<TextView>(R.id.text_stars)
        var forksTextView: TextView = itemView.findViewById<TextView>(R.id.text_forks)

        init {
            contentLayout.setOnClickListener {
                onItemClicked(repositories[adapterPosition])
            }
        }
    }
}

package com.helios.maxwage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.helios.maxwage.R
import com.helios.maxwage.databinding.ItemJobBinding
import com.helios.maxwage.models.Job
import com.squareup.picasso.Picasso
import java.util.*

/**
 * Created by Helios on 4/18/2021.
 */
class ListJobAdapter(private var jobs: List<Job>) :
    RecyclerView.Adapter<ListJobAdapter.ViewHolder>(), Filterable {

    fun setData(jobs: List<Job>) {
        this.jobs = jobs
        notifyDataSetChanged()
    }

    fun getData(): List<Job> = searchedJobs

    private var searchedJobs = jobs.toMutableList()

    inner class ViewHolder(var layout: ItemJobBinding) : RecyclerView.ViewHolder(layout.root) {

        private val jobAvatar = layout.ivJobAvatar

        fun setupJobAvatar(imageUrl: String) {
            Picasso.get().load(imageUrl).into(jobAvatar)
        }
    }

    var onClick: ((jobId: String, isFavorite: Boolean) -> Unit)? = null
    var onAddFavoriteJob: ((String) -> Unit)? = null
    var onRemoveFavoriteJob: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemJobBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_job,
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val job = searchedJobs[position]

        holder.layout.job = job
        holder.layout.root.setOnClickListener {
            onClick?.invoke(job._id, job.isFavorite)
        }
        holder.layout.btnFavorite.setOnClickListener {
            if (job.isFavorite) {
                onRemoveFavoriteJob?.invoke(job._id)
            } else {
                onAddFavoriteJob?.invoke(job._id)
            }
        }

        if (job.avatar.isNotBlank()) {
            holder.setupJobAvatar(job.avatar)
        }

        if (job.isFavorite) {
            holder.layout.btnFavorite.setImageResource(R.drawable.ic_favorite_24)
        } else {
            holder.layout.btnFavorite.setImageResource(R.drawable.ic_favorite_border)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return searchedJobs.size
    }

    @Suppress("UNCHECKED_CAST")
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var filteredJobs = arrayListOf<Job>()

                if (constraint.isNullOrEmpty()) {
                    filteredJobs.addAll(jobs)
                } else {
                    val strPattern = constraint.toString().toLowerCase(Locale.getDefault()).trim()
                    filteredJobs = jobs.filter {
                        it.name.toLowerCase(Locale.getDefault()).contains(strPattern)
                    } as ArrayList<Job>
                }

                val filterResult = FilterResults()
                filterResult.values = filteredJobs
                return filterResult
            }

            override fun publishResults(p0: CharSequence?, result: FilterResults?) {
                searchedJobs.clear()
                searchedJobs.addAll(result!!.values as ArrayList<Job>)
                notifyDataSetChanged()
            }

        }
    }
}
package com.helios.maxwage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.helios.maxwage.R
import com.helios.maxwage.databinding.ItemJobBinding
import com.helios.maxwage.models.Job
import com.squareup.picasso.Picasso

/**
 * Created by Helios on 4/18/2021.
 */
class ListJobAdapter(var jobs: List<Job>) : RecyclerView.Adapter<ListJobAdapter.ViewHolder>() {

    inner class ViewHolder(var layout: ItemJobBinding): RecyclerView.ViewHolder(layout.root){

        private val jobAvatar = layout.ivJobAvatar

        fun setupJobAvatar(imageUrl: String) {
            Picasso.get().load(imageUrl).into(jobAvatar)
        }
    }

    var onClick : ((String) -> Unit)? = null
    var onClickFavorite: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemJobBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_job, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val job = jobs[position]

        holder.layout.job = job
        holder.layout.root.setOnClickListener {
            onClick?.invoke(job._id)
        }
        holder.layout.btnFavorite.setOnClickListener {
            onClickFavorite?.invoke(job._id)
        }

        if(job.avatar.isNotBlank()){
            holder.setupJobAvatar(job.avatar)
        }
    }

    override fun getItemCount(): Int {
        return jobs.size
    }
}
package com.example.covid19project.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.covid19project.databinding.ItemStateBinding
import com.example.covid19project.model.Details
import com.example.covid19project.R
import com.example.covid19project.util.getPeriod
import com.example.covid19project.util.toDateFormat
import java.sql.Date


@Suppress("DEPRECATION")
class ItemAdapter(val clickListener: (stateDetail: Details) -> Unit = {})
    : ListAdapter<Details, ItemAdapter.StateViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateViewHolder  = StateViewHolder(
        ItemStateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: StateViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class StateViewHolder(private val binding: ItemStateBinding):
       RecyclerView.ViewHolder(binding.root){
       fun bind(details: Details){
           binding.textState.text = details.state
           binding.textLastUpdatedView.text = itemView.context.getString(
               R.string.text_last_updated,
               getPeriod(
                   details.lastUpdatedTime.toDateFormat() as Date
               )
           )

           binding.textConfirmed.text = details.confirmed
           binding.textActive.text = details.active
           binding.textRecovered.text = details.recovered
           binding.textDeath.text = details.deaths

           details.deltaConfirmed.let {
               if (it == "0") {
                   binding.groupStateNewConfirm.visibility = View.GONE
               } else {
                   binding.groupStateNewConfirm.visibility = View.VISIBLE
                   binding.textStateNewConfirm.text = it
               }
           }

           details.deltaRecovered.let {
               if (it == "0") {
                   binding.groupStateNewRecover.visibility = View.GONE
               } else {
                   binding.groupStateNewRecover.visibility = View.VISIBLE
                   binding.textStateNewRecover.text = it
               }
           }

           details.deltaDeaths.let {
               if (it == "0") {
                   binding.groupStateNewDeaths.visibility = View.GONE
               } else {
                   binding.groupStateNewDeaths.visibility = View.VISIBLE
                   binding.textStateNewDeath.text = it
               }
           }

           binding.root.setOnClickListener {
               if (bindingAdapterPosition == RecyclerView.NO_POSITION){
                   return@setOnClickListener
               }
               val item = getItem(bindingAdapterPosition)
               item.let {
                   clickListener.invoke(it)
               }
           }
       }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Details>() {
            override fun areItemsTheSame(oldItem: Details, newItem: Details): Boolean =
                oldItem.state == newItem.state


            override fun areContentsTheSame(oldItem: Details, newItem: Details): Boolean =
                oldItem == newItem

        }
    }


}
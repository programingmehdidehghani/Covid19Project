package com.example.covid19project.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.covid19project.databinding.ItemTotalBinding
import com.example.covid19project.model.Details

class TotalAdapter: ListAdapter<Details,TotalAdapter.TotalViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TotalViewHolder {
        I
    }

    override fun onBindViewHolder(holder: TotalViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    class TotalViewHolder(private val binding: ItemTotalBinding):
            RecyclerView.ViewHolder(binding.root){
                fun bind(details: Details){
                    binding.textConfirmed.text = details.confirmed
                    binding.textActive.text = details.active
                    binding.textRecovered.text = details.recovered
                    binding.textDeceased.text = details.deaths

                    details.deltaConfirmed.let {
                        if (it == "0"){
                            binding.groupNewConfirmed.visibility = View.GONE
                        } else {
                            binding.groupNewRecovered.visibility = View.VISIBLE
                            binding.textNewRecovered.text = details.deltaRecovered
                        }
                    }

                    details.deltaDeaths.let {
                        if (it == "0"){
                             binding.groupNewDeaths.visibility = View.GONE
                        } else {
                            binding.groupNewDeaths.visibility= View.VISIBLE
                            binding.textNewDeaths.text = details.deltaDeaths
                        }
                    }

                }
            }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Details>(){
            override fun areItemsTheSame(oldItem: Details, newItem: Details): Boolean =
                oldItem.state == newItem.state


            override fun areContentsTheSame(oldItem: Details, newItem: Details): Boolean =
                oldItem == newItem
        }
    }


}
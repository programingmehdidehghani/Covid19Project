package com.example.covid19project.ui.detail.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.covid19project.databinding.ItemDistrictBinding
import com.example.covid19project.model.DistrictData

class DistrictsAdapter : ListAdapter<DistrictData,DistrictsAdapter.TotalViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TotalViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: TotalViewHolder, position: Int) {
        TODO("Not yet implemented")
    }


    class TotalViewHolder(private val binding: ItemDistrictBinding)
        : RecyclerView.ViewHolder(binding.root){
            fun bind(details: DistrictData){
                binding.textDistrictName.text = details.district
                binding.textConfirmed.text = details.confirmed.toString()
                binding.textActive.text = details.active.toString()
                binding.textDeath.text = details.deceased.toString()
                binding.textRecovered.text = details.recovered.toString()

                details.delta.confirmed.let {
                    if (it == 0){
                        binding.groupStateNewConfirm.visibility = View.GONE
                    } else {
                        binding.groupStateNewRecover.visibility = View.VISIBLE
                        binding.textDistrictNewRecover.text = details.delta.recovered.toString()
                    }
                }

                details.delta.deceased.let {
                    if (it == 0){
                        binding.groupStateNewDeaths.visibility = View.GONE
                    } else {
                        binding.groupStateNewDeaths.visibility = View.VISIBLE
                        binding.textDistrictNewDeath.text = details.delta.deceased.toString()
                    }
                }

                details.notes.let {
                    if (it.isBlank()) {
                        binding.textNotes.visibility = View.GONE
                    } else {
                        binding.textNotes.visibility = View.VISIBLE
                        binding.textNotes.text = details.notes
                    }
                }
            }

        companion object {
            private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DistrictData>() {
                override fun areItemsTheSame(
                    oldItem: DistrictData,
                    newItem: DistrictData
                ): Boolean {
                    TODO("Not yet implemented")
                }

                override fun areContentsTheSame(
                    oldItem: DistrictData,
                    newItem: DistrictData
                ): Boolean {
                    TODO("Not yet implemented")
                }

            }
        }

    }


}
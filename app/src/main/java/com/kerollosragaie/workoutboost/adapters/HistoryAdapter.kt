package com.kerollosragaie.workoutboost.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kerollosragaie.workoutboost.R
import com.kerollosragaie.workoutboost.database.HistoryEntity
import com.kerollosragaie.workoutboost.databinding.ItemHistoryRowBinding

class HistoryAdapter(private val datesList:ArrayList<HistoryEntity>)
    :RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(binding: ItemHistoryRowBinding)
        : RecyclerView.ViewHolder(binding.root) {
        val llHistoryItemMain = binding.llHistoryItemMain
        val tvItem= binding.tvItem
        val tvPosition = binding.tvPosition
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHistoryRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val singleDate = datesList[position]
        holder.tvItem.text = singleDate.date
        holder.tvPosition.text = "${(position+1)}"

        if(position%2==0){
            holder.llHistoryItemMain.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,
                R.color.lightGray))
        }else{
            holder.llHistoryItemMain.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,
                R.color.white))
        }
    }

    override fun getItemCount(): Int {
        return datesList.size
    }


}
package com.kerollosragaie.workoutboost.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kerollosragaie.workoutboost.R
import com.kerollosragaie.workoutboost.databinding.ItemExerciseStatusBinding
import com.kerollosragaie.workoutboost.models.Exercise

class ExerciseStatusAdapter(var items:ArrayList<Exercise>):
    RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>(){

    inner class ViewHolder(binding: ItemExerciseStatusBinding)
            :RecyclerView.ViewHolder(binding.root){
                val tvItem = binding.tvItem
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemExerciseStatusBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val singleItem:Exercise = items[position]
        holder.tvItem.text = singleItem.getId().toString()

        when{
            singleItem.getIsSelected()->{
                holder.tvItem.background =
                    ContextCompat.getDrawable(holder.tvItem.context, R.drawable.item_circular_white_background)
                holder.tvItem.setTextColor(Color.parseColor("#212121"))
            }
            singleItem.getIsCompleted()->{
                holder.tvItem.background =
                    ContextCompat.getDrawable(holder.tvItem.context, R.drawable.item_circular_green_background)
                holder.tvItem.setTextColor(Color.parseColor("#FFFFFF"))
            }
            else->{
                holder.tvItem.background =
                    ContextCompat.getDrawable(holder.tvItem.context, R.drawable.item_circular_gray_background)
                holder.tvItem.setTextColor(Color.parseColor("#212121"))
            }
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }
}
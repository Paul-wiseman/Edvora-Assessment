package com.example.edvoraassessment.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.edvoraassessment.databinding.ChildItemLayoutBinding
import com.example.edvoraassessment.models.ProductItem

class ChildItemAdapter() : RecyclerView
.Adapter<ChildItemAdapter.ChildViewHolder>() {

    private var listItem = listOf<ProductItem>()
    class ChildViewHolder(private val binding: ChildItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(productItem: ProductItem) {
            binding.product = productItem
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ChildViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ChildItemLayoutBinding.inflate(layoutInflater, parent, false)
                return ChildViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChildViewHolder {
        return ChildViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {

        holder.bind(listItem[position])
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    fun setData(list:List<ProductItem>){
        this.listItem = list
    }


}
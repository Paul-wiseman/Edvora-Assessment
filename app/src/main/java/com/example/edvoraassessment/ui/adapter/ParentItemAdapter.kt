package com.example.edvoraassessment.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edvoraassessment.databinding.ChildItemLayoutBinding
import com.example.edvoraassessment.databinding.ItemLayoutBinding
import com.example.edvoraassessment.models.ParentItem
import com.example.edvoraassessment.models.ProductItem
import com.example.edvoraassessment.util.ProductListDiffUtill

class ParentItemAdapter: RecyclerView.Adapter<ParentItemAdapter.ParentItemViewHolder>() {
    private var listItem = listOf<ParentItem>()
    class ParentItemViewHolder(private val binding:ItemLayoutBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(parentItems: ParentItem) {

            Log.d("parentitem", "bind: $")

                binding.parentItemTitle.text = parentItems.title
                val childMembersAdapter = ChildItemAdapter()
                childMembersAdapter.setData(parentItems.listItem)

                binding.childRecyclerview.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL,false)
                binding.childRecyclerview.adapter = childMembersAdapter


        }


        companion object {
            fun from(parent: ViewGroup): ParentItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemLayoutBinding.inflate(layoutInflater, parent, false)
                return ParentItemViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ParentItemViewHolder {
        return ParentItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ParentItemViewHolder, position: Int) {
        holder.bind(listItem[position])
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    fun setData(list: List<ParentItem>){
val productDiffUtil = ProductListDiffUtill(listItem, list)
        val diffResult = DiffUtil.calculateDiff(productDiffUtil)
        listItem = list
        diffResult.dispatchUpdatesTo(this)
        Log.d("AdapterData", "setData: $list")
    }
}
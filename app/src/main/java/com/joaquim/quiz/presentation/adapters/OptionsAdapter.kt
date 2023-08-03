package com.joaquim.quiz.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.joaquim.quiz.R
import com.joaquim.quiz.databinding.ItemQuestionBinding


class OptionsAdapter : RecyclerView.Adapter<OptionsAdapter.OptionViewHolder>() {

    private var onItemClickListener: ((String) -> Unit)? = null
    inner class OptionViewHolder(val binding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<String>() {

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    var options: List<String>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        return OptionViewHolder(
            ItemQuestionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = options.size

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        val option = options[position]
        holder.binding.apply {
            btOption.text = option
            btOption.setOnClickListener{
                onItemClickListener?.let {
                    setColorOnClick(holder)
                    it(option)
                }
            }
        }
    }

    fun setOnClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    private fun setColorOnClick(holder: OptionViewHolder) {
        holder.binding.btOption.backgroundTintList = ContextCompat.getColorStateList(holder.binding.btOption.context, R.color.purple_200)
    }
}


package com.softsquared.niceduck.android.sparky.view.main.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.model.Tag

class TagRecyclerviewAdapter(private val tags: List<Tag>) :
    RecyclerView.Adapter<TagRecyclerviewAdapter.TagViewHolder>() {

    class TagViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val linearLayout: LinearLayout

        init {
            textView = itemView.findViewById(R.id.tag_item3_txt)
            linearLayout = itemView.findViewById(R.id.tag_item3_LL)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tag_item3, parent, false)

        return TagViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.textView.text = tags[position].name
        holder.linearLayout.backgroundTintList = ColorStateList.valueOf(Color.parseColor(tags[position].color))
    }

    override fun getItemCount() = tags.size
}

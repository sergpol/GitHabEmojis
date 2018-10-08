package ru.githubapiemojis

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

/**
 * Created by Полицинский Сергей on 08.10.2018.
 */
class EmojisRecyclerViewAdapter(val emojis: ArrayList<Emoji>): RecyclerView.Adapter<EmojisRecyclerViewAdapter.ViewHolder>() {
    private var listener: AdapterView.OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return emojis.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        emojis.get(position).let { emoji ->
            holder.bind(emoji)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(emoji: Emoji) {
            val title = itemView.findViewById(R.id.textView) as TextView
            val image = itemView.findViewById(R.id.imageView) as ImageView

            title.text = emoji.name
            Glide.with(image.getContext()).load(emoji.url).into(image)
        }
    }
}
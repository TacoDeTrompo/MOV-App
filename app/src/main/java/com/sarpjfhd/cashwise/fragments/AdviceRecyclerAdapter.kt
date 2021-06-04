package com.sarpjfhd.cashwise.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.sarpjfhd.cashwise.ImageUtilities
import com.sarpjfhd.cashwise.R
import com.sarpjfhd.cashwise.models.*

class AdviceRecyclerAdapter(
    val context: Context,
    var advices: MutableList<Advice>
) : RecyclerView.Adapter<AdviceRecyclerAdapter.ViewHolder>(), Filterable {

    private val layoutInflater = LayoutInflater.from(context)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var txtTitle = itemView.findViewById<TextView>(R.id.textView22)
        var txtDescription = itemView.findViewById<TextView>(R.id.textView23)
        var imgImage = itemView.findViewById<ImageView>(R.id.imageView2)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            when (v!!.id) {
                R.id.idFrameLayoutCardProfile2 -> {
                    //Lanzamos el intent para abrir el detall
                    //val  activityIntent =  Intent(context, MainActivity::class.java)
                    //activityIntent.putExtra(ALBUM_POSITION,this.albumPosition)
                    //context.startActivity(activityIntent)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = this.layoutInflater.inflate(R.layout.item_advice_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return this.advices.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val advice = advices[position]
        holder.txtTitle.text = advice.title
        holder.txtDescription.text = advice.content
        holder.imgImage.setImageBitmap(ImageUtilities.getBitMapFromByteArray(advice.imgArray))
    }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }
}
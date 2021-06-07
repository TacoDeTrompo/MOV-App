package com.sarpjfhd.cashwise.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sarpjfhd.cashwise.R
import com.sarpjfhd.cashwise.UserApplication
import com.sarpjfhd.cashwise.models.Profile
import java.math.BigDecimal
import java.time.temporal.ChronoUnit

class ProfileRecyclerAdapter(val context: Context, var profiles:MutableList<Profile>, cardOnClickListener: CardOnClickListener, isDraft: Boolean): RecyclerView.Adapter<ProfileRecyclerAdapter.ViewHolder>(), Filterable {

    private  val layoutInflater =  LayoutInflater.from(context)
    private val listener: CardOnClickListener = cardOnClickListener
    private val isDraft = isDraft

    inner class  ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnClickListener{

        val txtName = itemView?.findViewById<TextView>(R.id.textTranName)
        val txtDayRange = itemView?.findViewById<TextView>(R.id.textTrDescripcion)
        val txtStatus = itemView?.findViewById<TextView>(R.id.textStatus)
        val txtTotal = itemView?.findViewById<TextView>(R.id.textTrAmount)
        val cardColor = itemView?.findViewById<CardView>(R.id.cardColor)

        init{
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {

            when(v!!.id){
                R.id.idFrameLayoutCardProfile->{
                    //Lanzamos el intent para abrir el detall
                    //val  activityIntent =  Intent(context, MainActivity::class.java)
                    //activityIntent.putExtra(ALBUM_POSITION,this.albumPosition)
                    //context.startActivity(activityIntent)
                }
            }

        }
    }

    fun <T : RecyclerView.ViewHolder> T.onClick(event: (view: View, position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(it, getAdapterPosition(), getItemViewType())
        }
        return this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =  this.layoutInflater.inflate(R.layout.item_profile_list,parent,false)
        val viewHolder = ViewHolder(itemView)
        return viewHolder
    }

    override fun getItemCount(): Int  =  this.profiles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profile = profiles[position]
        holder.txtName.text = profile.profileName
        val dayRangeStr = "${profile.dayRange.toString()} Dias"
        holder.txtDayRange.text = dayRangeStr
        val endDate = profile.startDate.plusDays(profile.dayRange.toLong())
        val difference = ChronoUnit.DAYS.between(profile.startDate, endDate)
        if(isDraft){
            holder.txtStatus.text = "Borrador"
            holder.txtTotal.text = "Total: $0"
        } else {
            if (difference <= profile.dayRange) {
                holder.txtStatus.text = "En curso"
            } else {
                holder.txtStatus.text = "Concluido"
                val list = UserApplication.dbHelper.getListOfIngress(profile.idBD)
                val exList = UserApplication.dbHelper.getListOfExpense(profile.idBD)
                var amount: BigDecimal = BigDecimal(0)
                var expenses: BigDecimal = BigDecimal(0)
                for (ingress in list) {
                    amount += ingress.amount
                }
                for (expense in exList) {
                    expenses += expense.amount
                }
                val diff = amount - expenses
                val total = "Total: $${diff.toString()}"
                holder.txtTotal.text = total
            }
        }
        holder.cardColor.backgroundTintList = ColorStateList.valueOf(profile.color.toInt())
        listener.getTotalAmount(profile.idBD, holder.txtTotal)

        holder.cardColor.setOnClickListener {
            listener.onCardClickListener(profile.idBD)
        }
    }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }

    fun getItemPosition(idDB: Int): Int {
        return profiles.indexOfFirst {
            a -> a.idBD == idDB
        }
    }
}
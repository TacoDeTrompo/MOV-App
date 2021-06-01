package com.sarpjfhd.cashwise.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sarpjfhd.cashwise.R
import com.sarpjfhd.cashwise.models.Expense

class TransactionRecyclerAdapter(val context: Context, var expense:MutableList<Expense>): RecyclerView.Adapter<TransactionRecyclerAdapter.ViewHolder>(), Filterable {

    private  val layoutInflater =  LayoutInflater.from(context)

    inner class  ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnClickListener{

        val txtName = itemView?.findViewById<TextView>(R.id.textTranName)
        val txtDescription = itemView?.findViewById<TextView>(R.id.textTrDescripcion)
        val txtAmount = itemView?.findViewById<TextView>(R.id.textTrAmount)

        init{
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {

            when(v!!.id){
                R.id.idFrameLayoutCardProfile2->{
                    //Lanzamos el intent para abrir el detall
                    //val  activityIntent =  Intent(context, MainActivity::class.java)
                    //activityIntent.putExtra(ALBUM_POSITION,this.albumPosition)
                    //context.startActivity(activityIntent)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =  this.layoutInflater.inflate(R.layout.item_transaction_list,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int  =  this.expense.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val expense = expense[position]
        holder.txtName.text = expense.name
        holder.txtDescription.text = expense.description
        val total = "$${expense.amount.toString()}"
        holder.txtAmount.text = total
    }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }
}
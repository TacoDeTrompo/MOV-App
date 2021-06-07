package com.sarpjfhd.cashwise.fragments

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.RecyclerView
import com.sarpjfhd.cashwise.R
import com.sarpjfhd.cashwise.models.Expense
import com.sarpjfhd.cashwise.models.ExpenseType
import com.sarpjfhd.cashwise.models.Ingress
import com.sarpjfhd.cashwise.models.Transaction

class TransactionRecyclerAdapter(
    val context: Context,
    var expense: MutableList<Expense>,
    var ingress: MutableList<Ingress>,
    val isExpense: Boolean,
    val moveInterface: MoveToEditTransactionFragment
) : RecyclerView.Adapter<TransactionRecyclerAdapter.ViewHolder>(), Filterable {

    private val layoutInflater = LayoutInflater.from(context)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        lateinit var txtType: TextView
        var txtName: TextView
        var txtDescription: TextView
        var txtAmount: TextView
        var btnDelete: ImageButton
        var btnUpdate:ImageButton

        init {
            when (isExpense) {
                true -> {
                    txtType = itemView?.findViewById<TextView>(R.id.textExpType)
                    txtName = itemView?.findViewById<TextView>(R.id.textTranName)
                    txtDescription = itemView?.findViewById<TextView>(R.id.textTrDescripcion)
                    txtAmount = itemView?.findViewById<TextView>(R.id.textTrAmount)
                    btnDelete = itemView?.findViewById<ImageButton>(R.id.imageBdelete)
                    btnUpdate = itemView?.findViewById<ImageButton>(R.id.imageBupdate)
                }
                false -> {
                    txtName = itemView?.findViewById<TextView>(R.id.textTranNameIn)
                    txtDescription = itemView?.findViewById<TextView>(R.id.textTrDescripcionIn)
                    txtAmount = itemView?.findViewById<TextView>(R.id.textTrAmountIn)
                    btnDelete = itemView?.findViewById<ImageButton>(R.id.imageBdeleteIn)
                    btnUpdate = itemView?.findViewById<ImageButton>(R.id.imageBupdateIn)
                }
            }
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
        val itemView = when (isExpense) {
            true -> this.layoutInflater.inflate(R.layout.item_transaction_list, parent, false)
            false -> this.layoutInflater.inflate(R.layout.item_ingress_list, parent, false)
        }
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int{
        return when (isExpense){
            true -> this.expense.size
            false -> this.ingress.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction: Transaction
        var transId: Int
        when (isExpense) {
            true -> {
                val expense = expense[position]
                holder.txtName.text = expense.name
                holder.txtDescription.text = expense.description
                val total = "$${expense.amount.toString()}"
                holder.txtAmount.text = total
                when (expense.type) {
                    ExpenseType.FOOD -> holder.txtType.text = "Comida"
                    ExpenseType.SERVICES -> holder.txtType.text = "Servicios"
                    ExpenseType.ENTERTAINMENT -> holder.txtType.text = "Entretenimiento"
                    ExpenseType.INVESTMENT -> holder.txtType.text = "Inversion"
                }
                transaction = expense
                transId = expense.idBD
            }
            false -> {
                val ingress = ingress[position]
                holder.txtName.text = ingress.name
                holder.txtDescription.text = ingress.description
                val total = "$${ingress.amount.toString()}"
                holder.txtAmount.text = total
                transaction = ingress
                transId = ingress.idBD
            }
        }
        holder.btnUpdate.setOnClickListener {
            moveInterface.onUpdateClick(isExpense, transId)
        }
        holder.btnDelete.setOnClickListener {
            moveInterface.onDeleteClick(transaction.idBD, arrayListOf(), position)
        }
    }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }

    fun deleteItem(position: Int) {
        var transactions: MutableList<Transaction>
        when (isExpense) {
            true -> {
                expense.removeAt(position)
                transactions = expense.toMutableList()
            }
            false -> {
                ingress.removeAt(position)
                transactions = ingress.toMutableList()
            }
        }
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, transactions.size)
    }
}
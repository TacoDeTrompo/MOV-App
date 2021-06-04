package com.sarpjfhd.cashwise.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TransactionPagerAdapater(
    fragmentManager: FragmentManager, lifecycle: Lifecycle,
    var expenseFragment: TransactionListFragment,
    var ingressFragment: TransactionListFragment
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private var profilesFragmentState: Int = 0
    fun changeProfileFragment(state: Int = 0) {
        profilesFragmentState = state
    }

    //Constante a nivel de clase
    companion object {
        private const val ARG_OBJECT = "object"
    }

    //cuantos fragments va a tener el swipe
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        //Vamos a crear el fragmente

        //Tenemos 2 formas de pasar información a ese fragment
        //Una pasar los datos por medio de un constructor que no es recomendable
        //La segunda usando los arguments, setar argumentos al adaptador que vamos a
        //mandar a cada instancia
        expenseFragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_OBJECT, position + 1)
        }
        ingressFragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_OBJECT, position + 1)
        }
        return when (position) {
            0 -> {
                expenseFragment
            }
            1 -> {
                ingressFragment
            }
            else -> {
                expenseFragment
            }
        }

    }

}
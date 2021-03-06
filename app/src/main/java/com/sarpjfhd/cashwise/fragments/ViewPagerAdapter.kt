package com.sarpjfhd.cashwise.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapater(fragmentManager: FragmentManager, lifecycle: Lifecycle, draftFragment: ProfilesDraftFragment) : FragmentStateAdapter(fragmentManager, lifecycle)  {
    private var profilesFragmentState: Int = 0
    fun changeProfileFragment(state: Int = 0){
        profilesFragmentState = state
    }
    var draftFragment: ProfilesDraftFragment = draftFragment
    //Constante a nivel de clase
    companion object{
        private  const val ARG_OBJECT = "object"
    }

    //cuantos fragments va a tener el swipe
    override fun getItemCount(): Int  = 4

    override fun createFragment(position: Int): Fragment {
        //Vamos a crear el fragmente
        val profilesFragment =  ProfilesFragment()
        val profileFragment = ProfileFragment()
        val adviceFragment = AdviceFragment()
        val userFragment = UserFragment()

        //Tenemos 2 formas de pasar información a ese fragment
        //Una pasar los datos por medio de un constructor que no es recomendable
        //La segunda usando los arguments, setar argumentos al adaptador que vamos a
        //mandar a cada instancia
        profilesFragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_OBJECT, position + 1)
        }
        adviceFragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_OBJECT, position + 1)
        }
        draftFragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_OBJECT, position + 1)
        }
        userFragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_OBJECT, position + 1)
        }
        return when(position){
            0 -> {
                when(profilesFragmentState) {
                    0 -> {
                        profilesFragment
                    }
                    1 -> {
                        profileFragment
                    }
                    else -> {
                        profilesFragment
                    }
                }
            }
            1 -> {
                adviceFragment
            }
            2 -> {
                draftFragment
            }
            3 -> {
                userFragment
            }
            else -> {
                profilesFragment
            }
        }

    }

}
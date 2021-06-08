package com.sarpjfhd.cashwise.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sarpjfhd.cashwise.MainViewModel
import com.sarpjfhd.cashwise.R
import com.sarpjfhd.cashwise.UserApplication
import com.sarpjfhd.cashwise.models.User
import com.sarpjfhd.cashwise.navigateSafe

class HomeFragment : Fragment() {
//    private lateinit var buttonCreateProfile: Button
//    private lateinit var buttonProfiles: Button
//    private lateinit var buttonAdvice: Button
//    private lateinit var buttonDeleteProfile: Button
    private lateinit var tabLayout: TabLayout
    private lateinit var pager: ViewPager2
    private lateinit var pagerAdapater: ViewPagerAdapater
    private lateinit var profileAdapter: ProfileRecyclerAdapter
    private lateinit var draftFragment: ProfilesDraftFragment
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_tabs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabLayout = view.findViewById(R.id.tabLayoutMain)
        pager = view.findViewById(R.id.pager)
        pager.offscreenPageLimit = 4
        pagerAdapater = ViewPagerAdapater(requireActivity().supportFragmentManager, lifecycle, ProfilesDraftFragment())
        pager.adapter = pagerAdapater

        //Aqui ya sabe quien es nuestro tab, y quien nuestro pager
        val tabLayoutMediator =  TabLayoutMediator(tabLayout, pager
            , TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when(position){
                    0-> {
                        tab.text =  "Perfiles"
                    }
                    1-> {
                        tab.text =  "Consejos de la Semana"
                    }
                    2-> {
                        tab.text =  "Borradores"
                    }
                    3-> {
                        tab.text =  "Usuario"
                    }
                }
            })
        tabLayoutMediator.attach()

        setFragmentResultListener(DraftUpdateFragment.REQUEST_KEY_SAVED) { _, bundle ->
            val idProf = bundle.getInt(DraftUpdateFragment.BUNDLE_KEY_SAVED, -1)
            if (idProf != -1) {
                pagerAdapater = ViewPagerAdapater(requireActivity().supportFragmentManager, lifecycle, ProfilesDraftFragment())
                pager.adapter = pagerAdapater
            }
        }
    }

    override fun onResume() {
        super.onResume()
        pagerAdapater = ViewPagerAdapater(requireActivity().supportFragmentManager, lifecycle, ProfilesDraftFragment())
        pager.adapter = pagerAdapater
    }
}
package com.sarpjfhd.cashwise.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sarpjfhd.cashwise.MainViewModel
import com.sarpjfhd.cashwise.R
import com.sarpjfhd.cashwise.UserApplication
import com.sarpjfhd.cashwise.models.Profile
import com.sarpjfhd.cashwise.navigateSafe

class ProfilesDraftFragment() : Fragment(), CardOnClickListener {
    lateinit var profileAdapter: ProfileRecyclerAdapter
    private var context2: Context? = null
    private lateinit var floatingButtonCreate: FloatingActionButton
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var rcListProfile: RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)

        this.context2 = context

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profiles_draft, container, false)

        rcListProfile = root.findViewById<RecyclerView>(R.id.recyclerViewDraft)
        rcListProfile.layoutManager = LinearLayoutManager(this.context2!!)
        this.profileAdapter = ProfileRecyclerAdapter(
            this.context2!!,
            UserApplication.dbHelper.getListOfProfiles(),
            this,
            true
        )
        rcListProfile.adapter = this.profileAdapter

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        floatingButtonCreate = view.findViewById(R.id.floatingActionButtonCreateDraft)

        floatingButtonCreate.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToCreateProfileFragment()
            findNavController().navigateSafe(action)
        }

        setFragmentResultListener(DraftUpdateFragment.REQUEST_KEY_SAVED) { _, bundle ->
            val idProf = bundle.getInt(DraftUpdateFragment.BUNDLE_KEY_SAVED, -1)
            if (idProf != -1) {
                val profiles = this.profileAdapter?.profiles ?: return@setFragmentResultListener
                val position = profiles.find {
                    it.idBD == idProf
                }?.let { profile ->
                    profileAdapter.getItemPosition(idProf)
                }
                if (position != null) {
                    profileAdapter.notifyItemChanged(position)
                }
            }
        }
    }

    override fun onCardClickListener(profileId: Int) {
        viewModel.profileId = profileId
        val action = HomeFragmentDirections.actionHomeFragmentToDraftUpdateFragment()
        findNavController().navigateSafe(action)
    }

    override fun getTotalAmount(profileId: Int, text: TextView) {

    }

    fun updateRecycler(interfaceUP: UpdateFragment) {
        interfaceUP.onFragmentResult(profileAdapter)
    }

    interface UpdateFragment {
        fun onFragmentResult(adapter: ProfileRecyclerAdapter)
    }
}
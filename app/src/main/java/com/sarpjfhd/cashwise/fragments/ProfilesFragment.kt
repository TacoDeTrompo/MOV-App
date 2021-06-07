package com.sarpjfhd.cashwise.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sarpjfhd.cashwise.MainViewModel
import com.sarpjfhd.cashwise.R
import com.sarpjfhd.cashwise.UserApplication
import com.sarpjfhd.cashwise.models.AmountData
import com.sarpjfhd.cashwise.models.Profile
import com.sarpjfhd.cashwise.models.RestEngine
import com.sarpjfhd.cashwise.models.Service
import com.sarpjfhd.cashwise.navigateSafe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.math.BigDecimal
import java.time.LocalDate

class ProfilesFragment : Fragment(), CardOnClickListener {
    private var profileAdapter:ProfileRecyclerAdapter? = null
    private var context2: Context? = null
    private lateinit var floatingButtonCreate: FloatingActionButton
    lateinit var rcListProfile: RecyclerView
    private val viewModel: MainViewModel by activityViewModels()



    val self = this

    override fun onAttach(context: Context) {
        super.onAttach(context)

        this.context2 =  context

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profiles, container, false)



        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        floatingButtonCreate = view.findViewById(R.id.floatingActionButtonCreateProfile)
        rcListProfile = view.findViewById<RecyclerView>(R.id.recyclerView)
        getProfiles(object : ServiceCallback {
            override fun onSuccess(result: MutableList<Profile>) {
                rcListProfile.layoutManager = LinearLayoutManager(context)
                profileAdapter = ProfileRecyclerAdapter(requireContext(), result, self, false)
                rcListProfile.adapter = profileAdapter
            }
        })

        floatingButtonCreate.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToCreateProfileFragment()
            findNavController().navigateSafe(action)
        }
    }

    override fun onCardClickListener(profileId: Int) {
        viewModel.profileId = profileId
        val action = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
        findNavController().navigateSafe(action)
    }

    override fun getTotalAmount(profileId: Int, textView: TextView) {
        getTotalTransaction(object: ProfileFragment.ServiceCallbackAmount{
            override fun onSuccess(result: AmountData) {
                textView.text = "$${result.amount}"
            }
        }, profileId)
    }

    private fun getProfiles(apiServiceInterface: ServiceCallback){
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val user = UserApplication.dbHelper.getUserData(viewModel.userId)!!
        user.imgArray = ByteArray(0)
        val result: Call<List<Profile>> = service.getProfiles(user)

        result.enqueue(object: Callback<List<Profile>>{
            override fun onFailure(call: Call<List<Profile>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error obteniendo los mensajes: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Profile>>, response: Response<List<Profile>>) {
                val arrayItems = response.body()
                var profile: Profile
                val profiles: MutableList<Profile> = arrayItems!!.toMutableList()
                apiServiceInterface.onSuccess(profiles)
            }
        })
    }

    fun getTotalTransaction(apiServiceInterface: ProfileFragment.ServiceCallbackAmount, profileId: Int) {
        val profile = Profile("", 0, LocalDate.now(), "", "")
        profile.idBD = profileId
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<AmountData> = service.getTotalTransaction(profile)

        result.enqueue(object: Callback<AmountData> {
            override fun onFailure(call: Call<AmountData>, t: Throwable) {
                Toast.makeText(requireContext(), "Error subiendo el perfil: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<AmountData>, response: Response<AmountData>) {
                val amountData = response.body()
                if (amountData != null) {
                    apiServiceInterface.onSuccess(amountData)
                }
            }
        })
    }

    public interface ServiceCallback {
        fun onSuccess(result: MutableList<Profile>)
    }
}
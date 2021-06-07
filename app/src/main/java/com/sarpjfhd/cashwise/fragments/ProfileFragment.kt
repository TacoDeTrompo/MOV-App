package com.sarpjfhd.cashwise.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sarpjfhd.cashwise.MainViewModel
import com.sarpjfhd.cashwise.R
import com.sarpjfhd.cashwise.models.*
import com.sarpjfhd.cashwise.navigateSafe
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProfileFragment : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var pager: ViewPager2
    private var expensesFragment: TransactionListFragment? = null
    private var ingressesFragment: TransactionListFragment? = null
    private lateinit var buttonAddTransaction: FloatingActionButton
    private lateinit var btnUpdateProfile: ImageButton
    private lateinit var btnDeleteProfile: ImageButton
    private lateinit var txtTotalAmount: TextView
    private lateinit var txtDate: TextView
    private lateinit var txtDescription: TextView
    private lateinit var txtName: TextView
    private var transactionAdapter:TransactionPagerAdapater? = null
    private var context2: Context? = null
    private val viewModel: MainViewModel by activityViewModels()

    companion object {
        const val REQUEST_KEY_SAVED = "REQUEST_KEY_SAVED"
        const val BUNDLE_KEY_SAVED = "BUNDLE_KEY_SAVED"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        this.context2 =  context

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonAddTransaction = view.findViewById(R.id.buttonAddTransaction)
        txtTotalAmount = view.findViewById(R.id.textTotalExpenses)
        txtDate = view.findViewById(R.id.textFecha)
        txtDescription = view.findViewById(R.id.textDescripcion)
        btnUpdateProfile = view.findViewById(R.id.imageButtonEditPr)
        btnDeleteProfile = view.findViewById(R.id.buttonPrRegresar2)
        tabLayout = view.findViewById(R.id.tabLayout)
        pager = view.findViewById(R.id.pager2)
        txtName = view.findViewById(R.id.textView10)
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        var profile: Profile = Profile("", 0, LocalDate.now(), "", "")
        profile.idBD = viewModel.profileId
        val list: MutableList<Ingress> = mutableListOf<Ingress>()
        val exList: MutableList<Expense> = mutableListOf<Expense>()
        getProfile(object: ServiceCallbackProfile{
            override fun onSuccess(result: Profile) {
                profile = result
                txtName.text = profile.profileName
                txtDate.text = profile?.startDate?.format(formatter)
                txtDescription.text = profile?.descrption
            }
        }, viewModel.profileId, profile)
        getExpenses(object: ServiceCallbackExpenses{
            override fun onSuccess(result: MutableList<Expense>) {
                for (item in result) {
                    exList.add(item)
                }
                expensesFragment = TransactionListFragment(exList, arrayListOf(), true)
                getIngresses(object: ServiceCallbackIngresses{
                    override fun onSuccess(result: MutableList<Ingress>) {
                        for (item in result) {
                            list.add(item)
                        }
                        ingressesFragment = TransactionListFragment(arrayListOf(), list, false)
                        transactionAdapter = TransactionPagerAdapater(requireActivity().supportFragmentManager, lifecycle, expensesFragment!!, ingressesFragment!!)
                        pager.adapter = transactionAdapter
                        //pager.offscreenPageLimit = 2
                        val tabLayoutMediator =  TabLayoutMediator(tabLayout, pager
                            , TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                                when(position){
                                    0-> {
                                        tab.text =  "Gastos"
                                    }
                                    1-> {
                                        tab.text =  "Ingresos"
                                    }
                                }
                            })
                        tabLayoutMediator.attach()
                    }
                }, viewModel.profileId)
            }
        }, viewModel.profileId)
        getTotalTransaction(object: ServiceCallbackAmount {
            override fun onSuccess(result: AmountData) {
                txtTotalAmount.text = "$${result.amount}"
            }
        }, viewModel.profileId)

        buttonAddTransaction.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToCreateTransactionFragment()
            findNavController().navigateSafe(action)
        }

        btnUpdateProfile.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToProfileEditFragment()
            findNavController().navigateSafe(action)
        }

        btnDeleteProfile.setOnClickListener {
            deleteTransaction(object: ServiceCallback {
                override fun onSuccess(result: Boolean) {
                    findNavController().navigateUp()
                    setFragmentResult(REQUEST_KEY_SAVED, bundleOf(BUNDLE_KEY_SAVED to viewModel.profileId))
                }
            }, profile)
        }
    }

    override fun onResume() {
        super.onResume()
        var profile: Profile = Profile("", 0, LocalDate.now(), "", "")
        profile.idBD = viewModel.profileId
        val list: MutableList<Ingress> = mutableListOf<Ingress>()
        val exList: MutableList<Expense> = mutableListOf<Expense>()
        getProfile(object: ServiceCallbackProfile {
            override fun onSuccess(result: Profile) {
                profile = result
                val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                txtName.text = result.profileName
                txtDate.text = result?.startDate?.format(formatter)
                txtDescription.text = result?.descrption
            }
        }, viewModel.profileId, profile)
        getExpenses(object: ServiceCallbackExpenses{
            override fun onSuccess(result: MutableList<Expense>) {
                for (item in result) {
                    exList.add(item)
                }
                expensesFragment = TransactionListFragment(exList, arrayListOf(), true)
                getIngresses(object: ServiceCallbackIngresses{
                    override fun onSuccess(result: MutableList<Ingress>) {
                        for (item in result) {
                            list.add(item)
                        }
                        ingressesFragment = TransactionListFragment(arrayListOf(), list, false)
                        transactionAdapter = TransactionPagerAdapater(requireActivity().supportFragmentManager, lifecycle, expensesFragment!!, ingressesFragment!!)
                        pager.adapter = transactionAdapter
                        //pager.offscreenPageLimit = 2
                        val tabLayoutMediator =  TabLayoutMediator(tabLayout, pager
                            , TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                                when(position){
                                    0-> {
                                        tab.text =  "Gastos"
                                    }
                                    1-> {
                                        tab.text =  "Ingresos"
                                    }
                                }
                            })
                        tabLayoutMediator.attach()
                    }
                }, viewModel.profileId)
            }
        }, viewModel.profileId)
        getTotalTransaction(object: ServiceCallbackAmount {
            override fun onSuccess(result: AmountData) {
                txtTotalAmount.text = "$${result.amount}"
            }
        }, viewModel.profileId)
    }


    private fun getProfile(apiServiceInterface: ServiceCallbackProfile, profileId: Int, profile: Profile){
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<Profile> = service.getProfile(profile)

        result.enqueue(object: Callback<Profile> {
            override fun onFailure(call: Call<Profile>, t: Throwable) {
                Toast.makeText(requireContext(), "Error obteniendo los mensajes", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                var profile: Profile = response.body()!!
                apiServiceInterface.onSuccess(profile)
            }
        })
    }

    private fun getExpenses(apiServiceInterface: ServiceCallbackExpenses, profileId: Int){
        val profile = Profile("", 0, LocalDate.now(), "", "")
        profile.idBD = profileId
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<Expense>> = service.getExpensesByProfile(profile)

        result.enqueue(object: Callback<List<Expense>>{
            override fun onFailure(call: Call<List<Expense>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error obteniendo los mensajes", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Expense>>, response: Response<List<Expense>>) {
                val arrayItems = response.body()
                val expenses: MutableList<Expense> = arrayItems!!.toMutableList()
                apiServiceInterface.onSuccess(expenses)
            }
        })
    }

    private fun getIngresses(apiServiceInterface: ServiceCallbackIngresses, profileId: Int){
        val profile = Profile("", 0, LocalDate.now(), "", "")
        profile.idBD = profileId
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<Ingress>> = service.getIngressesById(profile)

        result.enqueue(object: Callback<List<Ingress>>{
            override fun onFailure(call: Call<List<Ingress>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error obteniendo los mensajes", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Ingress>>, response: Response<List<Ingress>>) {
                val arrayItems = response.body()
                val ingresses: MutableList<Ingress> = arrayItems!!.toMutableList()
                apiServiceInterface.onSuccess(ingresses)
            }
        })
    }

    fun deleteTransaction(apiServiceInterface: ServiceCallback, profile: Profile) {
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<ResponseBody> = service.deleteProfile(profile)

        result.enqueue(object: Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(requireContext(), "Error subiendo el perfil: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

            }

        })
    }

    fun getTotalTransaction(apiServiceInterface: ServiceCallbackAmount, profileId: Int) {
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

    public interface ServiceCallbackProfile {
        fun onSuccess(result: Profile)
    }
    public interface ServiceCallbackExpenses {
        fun onSuccess(result: MutableList<Expense>)
    }
    public interface ServiceCallbackIngresses {
        fun onSuccess(result: MutableList<Ingress>)
    }
    public interface ServiceCallbackAmount {
        fun onSuccess(result: AmountData)
    }
    public interface ServiceCallback {
        fun onSuccess(result: Boolean)
    }
}
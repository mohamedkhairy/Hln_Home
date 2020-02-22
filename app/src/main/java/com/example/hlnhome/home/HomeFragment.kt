package com.example.hlnhome.home

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.size
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.eazypermissions.common.model.PermissionResult
import com.eazypermissions.livedatapermission.PermissionManager
import com.example.hlnhome.R
import com.example.hlnhome.adapter.ParentAdapter
import com.example.hlnhome.database.entity.Service
import com.example.hlnhome.factory.ViewModelProviderFactory
import com.example.hlnhome.home.serviceInfo.ServiceInfoFragment
import com.example.hlnhome.util.hideView
import com.example.hlnhome.util.isNotNullOrEmpty
import com.example.hlnhome.util.showView
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.squareup.seismic.ShakeDetector
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragment : DaggerFragment(), PermissionManager.PermissionObserver,
    ShakeDetector.Listener {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    lateinit var homeRecycler: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var errorMessage: View
    lateinit var slidingUpPanelLayout: SlidingUpPanelLayout
    lateinit var termsTitle: TextView
    lateinit var terms: TextView
    lateinit var agreeButton: Button
    lateinit var disagreeButton: Button
    private var jobs: Job? = null
    lateinit var shakeDetector: ShakeDetector


    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this, providerFactory).get(HomeViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSensor()
        checkPermission()
        initView(view)
        observeHomeData()
        observeLoading()
        initRecycler()
        setRefreshing()


    }

    private fun initSensor() {
        val sensorManager = getSystemService(context!!, SensorManager::class.java) as SensorManager
        shakeDetector = ShakeDetector(this)
        shakeDetector.setSensitivity(ShakeDetector.SENSITIVITY_HARD)
        shakeDetector.start(sensorManager)
    }


    private fun checkPermission() {

        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {

            observeLocation()

        } else {
            PermissionManager.requestPermissions(
                this, 1,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

            // Show rationale and request permission.
        }
    }


    private fun initView(view: View) {
        with(view) {
            homeRecycler = findViewById(R.id.parent_recycler)
            swipeRefreshLayout = findViewById(R.id.swipe_layout)
            errorMessage = findViewById(R.id.error_message)
            slidingUpPanelLayout = findViewById(R.id.sliding_up_layout)
            terms = findViewById(R.id.terms)
            termsTitle = findViewById(R.id.terms_title)
            agreeButton = findViewById(R.id.agree)
            disagreeButton = findViewById(R.id.disagree)

        }
    }

    private fun initRecycler() {

        val snapHelper = LinearSnapHelper()
        homeRecycler.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            snapHelper.attachToRecyclerView(this)
            if (size <= 2)
                this.stopScroll()
        }

    }

    private fun setRefreshing() {
        swipeRefreshLayout.setOnRefreshListener {
            checkPermission()
        }
    }

    private fun observeHomeData() {
        viewModel.getHomeData().observe(this, Observer { homeList ->

            if (homeList.isNotNullOrEmpty()) {
                homeRecycler.adapter =
                    ParentAdapter(homeList, context!!) { service: Service -> serviceItemCliced(service) }
                errorMessage.hideView()

            } else
                errorMessage.showView()


        })
    }

    private fun serviceItemCliced(service: Service) {
        if (service.terms.isNullOrEmpty()) {
            startFragment(service)
        } else {
            slidingUpPanelLayout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
            terms.text = service.terms
            termsTitle.text = service.name

            agreeButton.setOnClickListener {
                slidingUpPanelLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
                startFragment(service)
            }

            disagreeButton.setOnClickListener {
                slidingUpPanelLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED

            }
        }

    }

    private fun startFragment(service: Service) {
        val serviceFragment = ServiceInfoFragment.getInstance(service)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.main_Constraint, serviceFragment)
            ?.addToBackStack(ServiceInfoFragment.TAG)?.commit()
    }


    private fun observeLocation() {
        jobs = GlobalScope.launch(Dispatchers.Main) {
            viewModel.getLocation(context).observe(this@HomeFragment, Observer { pairData ->
                pairData?.let {
                    viewModel.getHalanHomeData(it.first, it.second)
                }
            })
        }
    }

    private fun observeLoading() {
        viewModel.getLoading().observe(this, Observer {
            if (it)
                showLoading()
            else
                hideLoading()
        })
    }


    override fun hearShake() {
        checkPermission()
        Toast.makeText(context, "Refreshing...", Toast.LENGTH_SHORT).show()
    }

    override fun setupObserver(permissionResultLiveData: LiveData<PermissionResult>) {
        permissionResultLiveData.observe(this, Observer {
            when (it) {
                is PermissionResult.PermissionGranted -> {
                    observeLocation()
                }
                is PermissionResult.PermissionDenied -> {
                    errorMessage.showView()
                    hideLoading()
                }

                is PermissionResult.ShowRational -> {
                    AlertDialog.Builder(requireContext())
                        .setMessage("We need Locatin permission")
                        .setTitle("Halan")
                        .setNegativeButton("Cancel") { dialog, _ ->
                            errorMessage.showView()
                            hideLoading()
                            dialog.dismiss()
                        }
                        .setPositiveButton("OK") { _, _ ->
                            PermissionManager.requestPermissions(
                                this,
                                1,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        }.create().show()

                }
                is PermissionResult.PermissionDeniedPermanently -> {
                    errorMessage.showView()
                    hideLoading()
                }

            }
        })
    }

    private fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
    }

    private fun showLoading() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun onDestroy() {
        super.onDestroy()
        clear()
    }

    private fun clear() {
        jobs?.let { it.cancel() }
        shakeDetector?.let { it.stop() }
    }


    companion object {

        const val TAG = "HomeFragment"

    }


}
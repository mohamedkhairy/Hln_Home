package com.example.hlnhome.home.serviceInfo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hlnhome.R
import com.example.hlnhome.database.entity.Service
import com.example.hlnhome.util.setAction
import kotlinx.android.synthetic.main.service_layout.*


class ServiceInfoFragment : Fragment() {


    var serviceInfo: Service? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        serviceInfo = this.arguments?.getParcelable<Service>(SERVICE_KEY)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.service_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        watchBackButton(view)
        name.text = serviceInfo?.name
        (activity as AppCompatActivity).supportActionBar.setAction(serviceInfo!!.name, false)

    }


    private fun watchBackButton(view: View) {
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { v, keyCode, event ->
            (activity as AppCompatActivity).supportActionBar.setAction(
                getString(R.string.app_name),
                false
            )
            return@setOnKeyListener false
        }
    }

    companion object {

        const val SERVICE_KEY = "key_service"
        const val TAG = "ServiceFragment"

        fun getInstance(service: Service?): Fragment {
            val fragment = ServiceInfoFragment()
            val args = Bundle()
            args.putParcelable(SERVICE_KEY, service)
            fragment.arguments = args
            return fragment
        }
    }
}
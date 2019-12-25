package com.component.module2

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.kubi.router.annotation.Route
import com.kubi.router.core.IService
import com.kubi.router.core.Router
import com.kubi.sdk.BaseFragment
import kotlinx.android.synthetic.main.fragment_module2.*

/**
 * author:  hedongjin
 * date:  2019-12-25
 * description: Please contact me if you have any questions
 */
@Route(module = "module2", path = "home")
class Module2Fragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_module2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        name.setOnClickListener {
            (Router.navigation(Uri.parse("service://app/home")) as? IService)?.let {
                Toast.makeText(activity!!, it.call<String>("getModuleName"), Toast.LENGTH_SHORT).show()
            }
        }
    }


}
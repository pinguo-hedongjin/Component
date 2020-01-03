package com.component.demo

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
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * author:  hedongjin
 * date:  2019-12-25
 * description: Please contact me if you have any questions
 */
@Route(module = "app", path = "home")
class MainFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        action.setOnClickListener {
            Router.with(activity).uri(Uri.parse("fragment://module1/home")).navigation()
        }


        name.setOnClickListener {
            (Router.with(activity).uri(Uri.parse("service://module1/home")).navigation() as? IService)?.let {
                Toast.makeText(activity!!, it.call<String>("getModuleName"), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
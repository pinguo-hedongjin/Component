package com.component.demo

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.component.module1.Module1Method
import com.component.module1.Module1Route
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
@Route(module = "app", path = "home", desc = "home页面")
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
            Router.build(Module1Route.MODULE1_HOME).navigation()
        }


        name.setOnClickListener {
            Router.getService(Module1Route.MODULE2_SERVICE).let {
                Toast.makeText(activity!!, it.call<String>(Module1Method.GET_MODULE_NAME), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
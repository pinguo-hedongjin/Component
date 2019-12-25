package com.kubi.router.launch

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.kubi.router.core.RegisterTransform
import com.kubi.router.utils.Logger
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * author:  hedongjin
 * date:  2019-09-18
 * description: Please contact me if you have any questions
 */
class PluginLaunch implements Plugin<Project> {

    @Override
    void apply(Project project) {
        if (project.plugins.hasPlugin(AppPlugin)) {
            Logger.make(project)
            Logger.i "RouterLaunch init ..."

            def android = project.extensions.getByType(AppExtension)
            android.registerTransform(new RegisterTransform(project))
        }
    }

}

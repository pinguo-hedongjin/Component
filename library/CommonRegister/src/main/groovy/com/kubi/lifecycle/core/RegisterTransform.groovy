package com.kubi.lifecycle.core


import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.kubi.lifecycle.utils.Logger
import org.gradle.api.Project

/**
 * author:  hedongjin
 * date:  2019-09-18
 * description: Please contact me if you have any questions
 */
class RegisterTransform extends Transform {

    private Project project

    RegisterTransform(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        return "Lifecycle_Transform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)

        def startTime = System.currentTimeMillis()
        def generator = new RegisterCodeGenerator(project)

        Logger.i "开始扫描Lifecycle信息..."
        generator.scanRegister(transformInvocation)
        Logger.i "完成扫描Lifecycle信息..."

        Logger.i "================================================"

        Logger.i "开始插入Lifecycle代码..."
        generator.insertRegister(transformInvocation)
        Logger.i "完成插入Lifecycle代码..."

        def useTime = System.currentTimeMillis() - startTime
        Logger.i "注册耗时：${useTime}毫秒"


    }


}
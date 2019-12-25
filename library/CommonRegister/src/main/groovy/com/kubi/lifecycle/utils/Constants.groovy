package com.kubi.lifecycle.utils

/**
 * author:  hedongjin
 * date:  2019-09-19
 * description: Please contact me if you have any questions
 */
class Constants {

    /***
     * 生成类文件后缀
     */
    static final def SUFFIX_CLASS_NAME = ".class"

    /***
     * 生成类
     */
    static final def GENERATE_TO_CLASS_NAME = "com.kubi.sdk.delegate.RegisterLifecycle"

    /***
     * 生成类
     */
    static final def GENERATE_TO_FILE_NAME = "${GENERATE_TO_CLASS_NAME}${SUFFIX_CLASS_NAME}"

    /***
     * 注册app生命周期的方法名
     */
    static final def REGISTER_APP_METHOD_NAME = "injectAppLifecycle"

    /***
     * 注册activity生命周期的方法名
     */
    static final def REGISTER_ACTIVITY_METHOD_NAME = "injectActivityLifecycle"

    /***
     * 注册fragment生命周期的方法名
     */
    static final def REGISTER_FRAGMENT_METHOD_NAME = "injectFragmentLifecycle"

    /***
     * 注册app生命周期的注解名
     */
    static final def REGISTER_APP_ANNOTATION_NAME = "com.kubi.sdk.annotations.AppLifecycle"

    /***
     * 注册activity生命周期的注解名
     */
    static final def REGISTER_ACTIVITY_ANNOTATION_NAME = "com.kubi.sdk.annotations.ActivityLifecycle"

    /***
     * 注册fragment生命周期的注解名
     */
    static final def REGISTER_FRAGMENT_ANNOTATION_NAME = "com.kubi.sdk.annotations.FragmentLifecycle"
}
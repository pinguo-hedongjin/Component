package com.kubi.router.utils

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
     * activity的scheme
     */
    static final def SCHEME_ACTIVITY_NAME = "activity"

    /***
     * fragment的scheme
     */
    static final def SCHEME_FRAGMENT_NAME = "fragment"

    /***
     * service的scheme
     */
    static final def SCHEME_SERVICE_NAME = "service"

    /***
     * Activity类
     */
    static final def SUPER_ACTIVITY_CLASS_NAME = "android.app.Activity"

    /***
     * Fragment类
     */
    static final def SUPER_FRAGMENT_CLASS_NAME = "androidx.fragment.app.Fragment"


    /***
     * 生成类
     */
    static final def GENERATE_TO_CLASS_NAME = "com.kubi.router.core.Register"

    /***
     * 生成类
     */
    static final def GENERATE_TO_FILE_NAME = "${GENERATE_TO_CLASS_NAME}${SUFFIX_CLASS_NAME}"

    /***
     * 注册route的方法名
     */
    static final def REGISTER_ROUTE_METHOD_NAME = "injectRoute"

    /***
     * 注册service的方法名
     */
    static final def REGISTER_SERVICE_METHOD_NAME = "injectService"

    /***
     * 注册interceptor的方法名
     */
    static final def REGISTER_INTERCEPTOR_METHOD_NAME = "injectInterceptor"

    /***
     * 注册route的注解名
     */
    static final def REGISTER_ROUTE_ANNOTATION_NAME = "com.kubi.router.annotation.Route"

    /***
     * 注册interceptor的注解名
     */
    static final def REGISTER_INTERCEPTOR_ANNOTATION_NAME = "com.kubi.router.annotation.Interceptor"

}
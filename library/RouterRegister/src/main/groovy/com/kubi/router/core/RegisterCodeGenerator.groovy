package com.kubi.router.core

import com.android.build.api.transform.Format
import com.android.build.api.transform.TransformInvocation
import com.android.utils.FileUtils
import com.kubi.router.utils.Constants
import com.kubi.router.utils.Logger
import com.kubi.router.utils.Utils
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import javassist.NotFoundException
import javassist.bytecode.annotation.IntegerMemberValue
import javassist.bytecode.annotation.StringMemberValue
import org.gradle.api.Project

import java.util.jar.JarFile
import java.util.zip.ZipOutputStream

/**
 * author:  hedongjin
 * date:  2019-09-19
 * description: Please contact me if you have any questions
 */
class RegisterCodeGenerator {

    private ClassPool scanPool = ClassPool.getDefault()

    private File registerFile
    private HashMap<String, String> routeMap = new HashMap<String, String>()
    private HashMap<String, String> serviceMap = new HashMap<String, String>()
    private HashMap<Integer, String> interceptorMap = new LinkedHashMap<Integer, String>()

    RegisterCodeGenerator(Project project) {
        scanPool.appendClassPath(Utils.getPlatform(project))
    }

    void scanRegister(TransformInvocation transformInvocation) {
        transformInvocation.inputs.each {
            it.jarInputs.each {

                def src = it.file
                def dest = transformInvocation.outputProvider.getContentLocation(
                        Utils.getJarName(it),
                        it.contentTypes,
                        it.scopes,
                        Format.JAR
                )

                scanPool.appendClassPath(it.file.absolutePath)
                scanJar(src, dest)

                FileUtils.copyFile(src, dest)
            }

            it.directoryInputs.each {

                def src = it.file
                def dest = transformInvocation.outputProvider.getContentLocation(
                        it.name,
                        it.contentTypes,
                        it.scopes,
                        Format.DIRECTORY
                )

                scanPool.appendClassPath(it.file.absolutePath)
                scanDirectory(it.file)

                FileUtils.copyDirectory(src, dest)
            }
        }
    }

    void insertRegister(TransformInvocation transformInvocation) {
        def jarFile = registerFile
        if (jarFile) {
            def registerPool = ClassPool.getDefault()
            registerPool.appendClassPath(jarFile.absolutePath)

            def optJar = new File(jarFile.getParent(), jarFile.name + ".opt")
            if (optJar.exists()) {
                optJar.delete()
            }

            com.kubi.router.utils.FileUtils.copyJar(jarFile, optJar, new com.kubi.router.utils.FileUtils.Visitor() {

                @Override
                void visitEntry(String entryName, byte[] data, ZipOutputStream outputJar) {
                    def className = Utils.getDotClassPath(entryName)
                    if (className.endsWith(Constants.GENERATE_TO_FILE_NAME)) {
                        com.kubi.router.utils.FileUtils.saveEntry(entryName, getRegisterCode(registerPool), outputJar)
                    } else {
                        com.kubi.router.utils.FileUtils.saveEntry(entryName, data, outputJar)
                    }
                }
            })

            if (jarFile.exists()) {
                jarFile.delete()
            }
            optJar.renameTo(jarFile)
        }
    }

    /***
     * 扫描jar文件
     * @return List<String>
     */
    private void scanJar(File srcJar, File destJar) {
        def jarFile = new JarFile(srcJar)
        def iterator = jarFile.entries()
        while (iterator.hasMoreElements()) {
            def jarEntry = iterator.nextElement()
            if (!isIgnoreJar(jarEntry.name)) {

                scanFile(jarEntry.name)

                if (isRegisterJar(jarEntry.name)) {
                    registerFile = destJar
                }
            }
        }
    }

    /***
     * 扫描源文件
     * @return List<String>
     */
    private void scanDirectory(File directory) {
        directory.eachFileRecurse {
            if (it.isFile() && !isIgnoreJar(it.absolutePath)) {
                scanFile(it.absolutePath.substring(directory.absolutePath.length() + 1))
            }
        }
    }


    private void scanFile(String path) {
        String className = Utils.excludeSuffix(Utils.getDotClassPath(path), Constants.SUFFIX_CLASS_NAME)
        try {
            CtClass ct = scanPool.get(className)

            Utils.getAnnotations(ct).each {

                if (it.typeName == Constants.REGISTER_ROUTE_ANNOTATION_NAME) {
                    def _module = ((StringMemberValue)it.getMemberValue("module")).getValue()
                    def _path = ((StringMemberValue)it.getMemberValue("path")).getValue()
                    def uri = "${_module}/${_path}"
                    if (routeMap.containsKey(uri)) {
                        throw new RuntimeException("存在重复uri = ${uri}")
                    } else {
                        routeMap[uri] = className
                    }

                    Logger.i "scan route = " + className + ", uri = " + uri
                }

                if (it.typeName == Constants.REGISTER_SERVICE_ANNOTATION_NAME) {
                    def _module = ((StringMemberValue)it.getMemberValue("module")).getValue()
                    def _path = ((StringMemberValue)it.getMemberValue("path")).getValue()
                    def uri = "${_module}/${_path}"
                    if (serviceMap.containsKey(uri)) {
                        throw new RuntimeException("存在重复uri = ${uri}")
                    } else {
                        serviceMap[uri] = className
                    }

                    Logger.i "scan service = " + className + ", uri = " + uri
                }

                if (it.typeName == Constants.REGISTER_INTERCEPTOR_ANNOTATION_NAME) {
                    def priority = ((IntegerMemberValue)it.getMemberValue("priority")).getValue()
                    if (interceptorMap.containsKey(priority)) {
                        throw new RuntimeException("存在重复${uri}")
                    } else {
                        interceptorMap[priority] = className
                    }
                    Logger.i "scan interceptor = " + className + ", priority = " + priority
                }
            }


        } catch (NotFoundException e) {
            // ignore
        } catch (ClassNotFoundException e) {
            // ignore
        } catch (Throwable e) {
            // ignore
        }

    }

    private boolean isIgnoreJar(String path) {
        return !path.endsWith(Constants.SUFFIX_CLASS_NAME) || path.contains("com.android.support") || path.contains("/android/m2repository") || path.startsWith("android") || path.startsWith("androidx")
    }

    private boolean isRegisterJar(String path) {
        return Utils.getDotClassPath(path).endsWith(Constants.GENERATE_TO_FILE_NAME)
    }

    private byte[] getRegisterCode(ClassPool pool) {
        CtClass ct = pool.get(Constants.GENERATE_TO_CLASS_NAME)
        if (ct.isFrozen()) {
            ct.defrost()
        }

        if (!routeMap.isEmpty()) {
            Logger.i "${Constants.REGISTER_ROUTE_METHOD_NAME}:${getRouteLog(routeMap)}"
            CtMethod method = ct.getDeclaredMethod(Constants.REGISTER_ROUTE_METHOD_NAME)
            method.setBody(getRouteCode(routeMap))
        }

        if (!serviceMap.isEmpty()) {
            Logger.i "${Constants.REGISTER_SERVICE_METHOD_NAME}:${getServiceLog(serviceMap)}"
            CtMethod method = ct.getDeclaredMethod(Constants.REGISTER_SERVICE_METHOD_NAME)
            method.setBody(getServiceCode(serviceMap))
        }

        if (!interceptorMap.isEmpty()) {
            Logger.i "${Constants.REGISTER_INTERCEPTOR_METHOD_NAME}:${getInterceptorLog(interceptorMap)}"
            CtMethod method = ct.getDeclaredMethod(Constants.REGISTER_INTERCEPTOR_METHOD_NAME)
            method.setBody(getInterceptorCode(interceptorMap))
        }

        return ct.toBytecode()
    }

    private String getRouteCode(HashMap<String, String> map) {
        StringBuffer buffer = new StringBuffer()

        buffer.append("{")
        map.entrySet().each {
            buffer.append("\$1.put(\"${it.key}\", ${it.value}.class);")
        }
        buffer.append("}")

        return buffer.toString()
    }

    private String getRouteLog(HashMap<String, String> map) {
        StringBuffer buffer = new StringBuffer()

        buffer.append("[")
        map.entrySet().each {
            buffer.append("${it.value},")
        }

        return buffer.toString().substring(0, buffer.length() - 1) + "]"
    }

    private String getServiceCode(HashMap<String, String> map) {
        StringBuffer buffer = new StringBuffer()

        buffer.append("{")
        map.entrySet().each {
            buffer.append("\$1.put(\"${it.key}\", new ${it.value}());")
        }
        buffer.append("}")

        return buffer.toString()
    }

    private String getServiceLog(HashMap<String, String> map) {
        return getRouteLog(map)
    }

    private String getInterceptorCode(HashMap<Integer, String> map) {
        // 排序
        List<Map.Entry<Integer, String>> list = sort(map)

        // 插入代码
        StringBuffer buffer = new StringBuffer()

        buffer.append("{")
        list.each {
            buffer.append("\$1.add(new ${it.value}());")
        }

        buffer.append("}")

        return buffer.toString()
    }

    private String getInterceptorLog(HashMap<Integer, String> map) {
        // 排序
        List<Map.Entry<Integer, String>> list = sort(map)

        // 日志
        StringBuffer buffer = new StringBuffer()

        buffer.append("[")
        list.each {
            buffer.append("${it.value},")
        }

        return buffer.toString().substring(0, buffer.length() - 1) + "]"
    }

    private List<Map.Entry<Integer, String>> sort(HashMap<Integer, String> map) {
        List<Map.Entry<Integer, String>> list = new ArrayList<Map.Entry<Integer, String>>(map.entrySet())
        Collections.sort(list, new Comparator<Map.Entry<Integer, String>>() {
            @Override
            int compare(Map.Entry<Integer, String> o1, Map.Entry<Integer, String> o2) {
                return o1.key - o2.key
            }
        })

        return list
    }

}
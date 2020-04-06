package com.kubi.lifecycle.core

import com.android.build.api.transform.Format
import com.android.build.api.transform.TransformInvocation
import com.android.utils.FileUtils
import com.kubi.lifecycle.utils.Logger
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import javassist.NotFoundException
import javassist.bytecode.annotation.IntegerMemberValue
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
    private HashMap<Integer, String> appLifecycleMap = new LinkedHashMap<Integer, String>()
    private Set activityLifecycleSet = new HashSet()
    private Set fragmentLifecycleSet = new HashSet()

    RegisterCodeGenerator(Project project) {
        scanPool.appendClassPath(com.kubi.lifecycle.utils.Utils.getPlatform(project))
    }

    void scanRegister(TransformInvocation transformInvocation) {
        transformInvocation.inputs.each {
            it.jarInputs.each {

                def src = it.file
                def dest = transformInvocation.outputProvider.getContentLocation(
                        com.kubi.lifecycle.utils.Utils.getJarName(it),
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

            com.kubi.lifecycle.utils.FileUtils.copyJar(jarFile, optJar, new com.kubi.lifecycle.utils.FileUtils.Visitor() {

                @Override
                void visitEntry(String entryName, byte[] data, ZipOutputStream outputJar) {
                    def className = com.kubi.lifecycle.utils.Utils.getDotClassPath(entryName)
                    if (className.endsWith(com.kubi.lifecycle.utils.Constants.GENERATE_TO_FILE_NAME)) {
                        com.kubi.lifecycle.utils.FileUtils.saveEntry(entryName, getRegisterCode(registerPool), outputJar)
                    } else {
                        com.kubi.lifecycle.utils.FileUtils.saveEntry(entryName, data, outputJar)
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
        String className = com.kubi.lifecycle.utils.Utils.excludeSuffix(com.kubi.lifecycle.utils.Utils.getDotClassPath(path), com.kubi.lifecycle.utils.Constants.SUFFIX_CLASS_NAME)
        try {
            CtClass ct = scanPool.get(className)
            com.kubi.lifecycle.utils.Utils.getAnnotations(ct).each {
                if (it.typeName == com.kubi.lifecycle.utils.Constants.REGISTER_APP_ANNOTATION_NAME) {
                    Logger.i "scan app = " + className
                    def priority = ((IntegerMemberValue)it.getMemberValue("priority")).getValue()
                    appLifecycleMap[priority] = className
                }
                if (it.typeName == com.kubi.lifecycle.utils.Constants.REGISTER_ACTIVITY_ANNOTATION_NAME) {
                    Logger.i "scan activity = " + className
                    activityLifecycleSet.add(className)
                }
                if (it.typeName == com.kubi.lifecycle.utils.Constants.REGISTER_FRAGMENT_ANNOTATION_NAME) {
                    Logger.i "scan fragment = " + className
                    fragmentLifecycleSet.add(className)
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
        return !path.endsWith(com.kubi.lifecycle.utils.Constants.SUFFIX_CLASS_NAME) || path.contains("com.android.support") || path.contains("/android/m2repository") || path.startsWith("android") || path.startsWith("androidx")
    }

    private boolean isRegisterJar(String path) {
        return com.kubi.lifecycle.utils.Utils.getDotClassPath(path).endsWith(com.kubi.lifecycle.utils.Constants.GENERATE_TO_FILE_NAME)
    }

    private byte[] getRegisterCode(ClassPool pool) {
        CtClass ct = pool.get(com.kubi.lifecycle.utils.Constants.GENERATE_TO_CLASS_NAME)
        if (ct.isFrozen()) {
            ct.defrost()
        }

        if (!appLifecycleMap.isEmpty()) {
            Logger.i "${com.kubi.lifecycle.utils.Constants.REGISTER_APP_METHOD_NAME}:${getRegisterClass(appLifecycleMap)}"
            CtMethod method = ct.getDeclaredMethod(com.kubi.lifecycle.utils.Constants.REGISTER_APP_METHOD_NAME)
            method.setBody(getRegisterCode(appLifecycleMap))
        }

        if (!activityLifecycleSet.isEmpty()) {
            Logger.i "${com.kubi.lifecycle.utils.Constants.REGISTER_ACTIVITY_METHOD_NAME}:${getRegisterClass(activityLifecycleSet)}"
            CtMethod method = ct.getDeclaredMethod(com.kubi.lifecycle.utils.Constants.REGISTER_ACTIVITY_METHOD_NAME)
            method.setBody(getRegisterCode(activityLifecycleSet))
        }

        if (!fragmentLifecycleSet.isEmpty()) {
            CtMethod method = ct.getDeclaredMethod(com.kubi.lifecycle.utils.Constants.REGISTER_FRAGMENT_METHOD_NAME)
            method.setBody(getRegisterCode(fragmentLifecycleSet))
            Logger.i "${com.kubi.lifecycle.utils.Constants.REGISTER_FRAGMENT_METHOD_NAME}:${getRegisterClass(fragmentLifecycleSet)}"
        }

        return ct.toBytecode()
    }

    private String getRegisterCode(Set<String> set) {
        StringBuffer buffer = new StringBuffer()

        buffer.append("{")
        set.each {
            buffer.append("\$1.add(new $it());")
        }
        buffer.append("}")

        return buffer.toString()
    }

    private String getRegisterClass(Set<String> set) {
        StringBuffer buffer = new StringBuffer()

        buffer.append("[")
        set.each {
            buffer.append("$it,")
        }

        return buffer.toString().substring(0, buffer.length() - 1) + "]"
    }

    private String getRegisterCode(HashMap<Integer, String> map) {
        // 插入代码
        StringBuffer buffer = new StringBuffer()

        buffer.append("{")
        sort(map).each {
            buffer.append("\$1.add(new ${it.value}());")
        }

        buffer.append("}")

        return buffer.toString()
    }

    private String getRegisterClass(HashMap<Integer, String> map) {
        // 插入代码
        StringBuffer buffer = new StringBuffer()

        buffer.append("[")
        sort(map).each {
            buffer.append("${it.key}-${it.value},")
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
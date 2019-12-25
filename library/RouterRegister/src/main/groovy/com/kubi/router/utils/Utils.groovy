package com.kubi.router.utils

import com.android.build.api.transform.JarInput
import javassist.CtClass
import javassist.bytecode.AnnotationsAttribute
import javassist.bytecode.ClassFile
import javassist.bytecode.annotation.Annotation
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project

/**
 * author:  hedongjin
 * date:  2019-09-20
 * description: Please contact me if you have any questions
 */
class Utils {

    static String getJarName(JarInput jarInput) {
        def hexName = DigestUtils.md5Hex(jarInput.file.absolutePath)
        def destName = jarInput.name
        if (jarInput.name.endsWith(".jar")) {
            jarInput.name.substring(0, jarInput.name.length - ".jar".length)
        }
        return "${destName}_$hexName"
    }

    static String getSlashClassPath(String path) {
        return path.replaceAll(".", "/")
    }

    static String getDotClassPath(String path) {
        return path.replaceAll("/", ".")
    }

    static String excludeSuffix(String path, String suffix) {
        if (path.endsWith(suffix)) {
            return path.substring(0, path.length() - suffix.length())
        }
        return path
    }

    static String getStackTrace(Throwable throwable) {
        StringBuffer buffer = new StringBuffer()

        throwable.getStackTrace().each {
            buffer.append(it.toString())
        }

        return buffer.toString()
    }

    static List<Annotation> getAnnotations(CtClass ct) {
        ClassFile cf = ct.getClassFile2()

        List<Annotation> result = new ArrayList<Annotation>()

        result.addAll(
                getAnnotations(
                        (AnnotationsAttribute) cf.getAttribute(AnnotationsAttribute.invisibleTag)
                )
        )

        result.addAll(
                getAnnotations(
                        (AnnotationsAttribute) cf.getAttribute(AnnotationsAttribute.visibleTag)
                )
        )

        return result

    }

    static List<Annotation> getAnnotations(AnnotationsAttribute attr) {
        List<Annotation> result = new ArrayList<Annotation>()

        if (attr != null && attr.getAnnotations() != null) {
            attr.getAnnotations().each { result.add(it) }
        }

        return result
    }

    static String getPlatform(Project project) {
        return "${project.android.getSdkDirectory()}${File.separator}platforms${File.separator}" +
                "${project.android.getCompileSdkVersion()}${File.separator}android.jar"
    }
}

package com.kubi.lifecycle.utils

import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * author:  hedongjin
 * date:  2019-09-23
 * description: Please contact me if you have any questions
 */
class FileUtils {

    /***
     * 拷贝jar文件
     * @param srcFile
     * @param destFile
     * @param visitor
     */
    static void copyJar(File srcFile, File destFile, Visitor visitor) {

        checkDestFile(destFile)

        ZipOutputStream zos = null
        ZipInputStream zis = null
        try {

            zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destFile)))
            zis = new ZipInputStream(new FileInputStream(srcFile))

            ZipEntry entry
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    continue
                }


                visitor.visitEntry(entry.name, getEntryData(zis), zos)
            }

        } finally {
            IoUtils.close(zos, zis)
        }
    }

    /***
     * 拷贝目录
     * @param srcFile
     * @param destFile
     * @param visitor
     */
    static void copyDirectory(File srcFile, File destFile, Visitor visitor) {
        checkDestFile(destFile)
        createMetaFile(srcFile.absolutePath)

        com.android.utils.FileUtils

        ZipOutputStream zos = null
        try {
            zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destFile)))

            srcFile.eachFileRecurse {
                if (it.isFile()) {
                    String entryName = it.absolutePath.substring(srcFile.absolutePath.length())
                    visitor.visitEntry(entryName, getEntryData(it), zos)
                }
            }

        } finally {
            IoUtils.close(zos)
        }
    }

    static void checkDestFile(File destFile) {
        if (destFile.exists()) {
            destFile.delete()
        }

        if (null == destFile.getParentFile() || !destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs()
        }
    }

    static void saveEntry(String entryName, byte[] data, ZipOutputStream zos) {
        zos.putNextEntry(new ZipEntry(entryName))
        zos.write(data, 0, data.length)
        zos.closeEntry()
    }

    private static byte[] getEntryData(ZipInputStream zis) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream()
        try {
            byte[] buffer = new byte[1024 * 50]

            int readLen
            while ((readLen = zis.read(buffer, 0, buffer.length)) != -1) {
                bos.write(buffer, 0, readLen)
            }
            zis.closeEntry()

            return bos.toByteArray()
        } finally {
            IoUtils.close(bos)
        }

    }

    private static byte[] getEntryData(File file) {
        FileInputStream fis = null
        ByteArrayOutputStream bos = null

        try {
            fis = new FileInputStream(file)
            bos = new ByteArrayOutputStream()

            byte[] buffer = new byte[1024 * 5]
            int readLen
            while ((readLen = fis.read(buffer, 0, buffer.length)) != -1) {
                bos.write(buffer, 0, readLen)
            }

            return bos.toByteArray()
        } finally {
            IoUtils.close(fis, bos)
        }

    }

    private static void createMetaFile(String path) {
        File metaDir = new File(path, "META-INF")
        if (!metaDir.exists() || !metaDir.isDirectory()) {
            metaDir.mkdirs()
        }

        File metaFile = new File(metaDir, "MANIFEST.MF")
        if (metaFile.exists()) {
            return
        }

        metaFile.createNewFile()

        FileOutputStream fos = new FileOutputStream(metaFile)
        String metaData = "Manifest-Version: 1.0\n"
        fos.write(metaData.getBytes())
        IoUtils.close(fos)
    }

    static interface Visitor {
        /**
         * 访问Jar文件中的每个entry
         *
         * @param entry 当前被访问的entry
         * @param inputJar 读取Jar的inputStream
         * @param outputJar Jar输出到的outputStream
         */
        void visitEntry(String entryName, byte[] data, ZipOutputStream outputJar)
    }
}
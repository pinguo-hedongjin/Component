package com.kubi.router.utils

import org.gradle.api.Project
import org.gradle.api.logging.LogLevel

/**
 * author:  hedongjin
 * date:  2019-09-18
 * description: Please contact me if you have any questions
 */
class Logger {

    private static def TAG = "Router >> "
    private static def debug = true
    private static org.gradle.api.logging.Logger logger

    static make(Project project) {
        logger = project.logger
        logger.isEnabled(LogLevel.DEBUG)
    }

    static setDebug(Boolean debug) {
        this.debug = debug
    }

    static i(String msg) {
        if (debug) {
            logger.warn("$TAG $msg")
        }
    }

    static e(String msg) {
        if (debug) {
            logger.error("$TAG $msg")
        }
    }
}
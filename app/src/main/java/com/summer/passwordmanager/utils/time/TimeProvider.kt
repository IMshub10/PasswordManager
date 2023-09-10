package com.summer.passwordmanager.utils.time

interface TimeProvider {
    fun getCurrentTimeSecs(): Long
}
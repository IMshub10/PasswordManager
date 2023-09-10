package com.summer.passwordmanager.utils.time

object AppTimeProvider : TimeProvider {
    override fun getCurrentTimeSecs() =
        System.currentTimeMillis() / 1000
}
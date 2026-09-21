package com.tylerdev.vitaforge

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
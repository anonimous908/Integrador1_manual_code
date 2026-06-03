package org.example.project.data

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

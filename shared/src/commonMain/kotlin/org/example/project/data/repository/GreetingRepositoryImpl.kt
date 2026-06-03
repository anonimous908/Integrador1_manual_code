package org.example.project.data.repository

import org.example.project.data.getPlatform
import org.example.project.domain.sayHello
import org.example.project.domain.repository.GreetingRepository

class GreetingRepositoryImpl : GreetingRepository {
    override fun getGreeting(): String {
        val platform = getPlatform()
        return sayHello(platform.name)
    }
}

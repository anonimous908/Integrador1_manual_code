package org.example.project.presentation

import org.example.project.domain.repository.GreetingRepository

class Greeting(private val repository: GreetingRepository) {
    fun greet(): String {
        return repository.getGreeting()
    }
}

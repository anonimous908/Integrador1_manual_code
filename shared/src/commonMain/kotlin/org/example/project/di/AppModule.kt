package org.example.project.di

import org.example.project.data.repository.AuthRepositoryImpl
import org.example.project.domain.repository.AuthRepository
import org.example.project.domain.usecase.LoginWithEmailUseCase
import org.example.project.domain.usecase.RegisterUseCase
import org.example.project.presentation.login.LoginViewModel
import org.example.project.presentation.register.RegisterViewModel
import org.example.project.presentation.AppViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import com.russhwolf.settings.Settings

import org.example.project.domain.usecase.ValidateEmailUseCase
import org.example.project.domain.usecase.ValidatePasswordUseCase
import org.example.project.domain.service.HashService
import org.example.project.data.security.HashServiceImpl
import org.example.project.data.network.GithubVersionChecker
import org.example.project.data.network.AiConfigRepository
import org.example.project.data.network.AiApiService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.example.project.domain.service.SyntaxHighlighter
import org.example.project.data.formatter.DraculaSyntaxHighlighter
import org.example.project.domain.repository.RecipeRepository
import org.example.project.data.repository.MockRecipeRepositoryImpl

val appModule = module {
    single { Settings() }
    
    single<SyntaxHighlighter> { DraculaSyntaxHighlighter() }
    single<RecipeRepository> { MockRecipeRepositoryImpl(settings = get()) }
    
    single<HashService> { HashServiceImpl() }
    
    single { 
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }
    
    single { GithubVersionChecker(client = get()) }
    single { AiConfigRepository(settings = get()) }
    single { AiApiService(client = get(), configRepo = get()) }

    single<AuthRepository> { AuthRepositoryImpl(settings = get(), hashService = get()) }
    
    factoryOf(::LoginWithEmailUseCase)
    factoryOf(::ValidateEmailUseCase)
    factoryOf(::ValidatePasswordUseCase)
    factoryOf(::RegisterUseCase)
    factoryOf(::LoginViewModel)
    factoryOf(::RegisterViewModel)
    factoryOf(::AppViewModel)
}

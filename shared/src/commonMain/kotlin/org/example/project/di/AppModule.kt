package org.example.project.di

import org.example.project.data.repository.AuthRepositoryImpl
import org.example.project.data.repository.FirebaseAuthRepositoryImpl
import org.example.project.domain.repository.AuthRepository
import org.example.project.domain.usecase.LoginWithEmailUseCase
import org.example.project.domain.usecase.LoginWithGoogleUseCase
import org.example.project.domain.usecase.RegisterUseCase
import org.example.project.presentation.login.LoginViewModel
import org.example.project.presentation.register.RegisterViewModel
import org.example.project.presentation.AppViewModel
import org.example.project.presentation.tabs.MyRecipesViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import com.russhwolf.settings.Settings

import org.example.project.domain.usecase.ValidateEmailUseCase
import org.example.project.domain.usecase.ValidatePasswordUseCase
import org.example.project.domain.service.HashService
import org.example.project.data.security.HashServiceImpl
import org.example.project.data.network.GithubVersionChecker
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.example.project.domain.service.SyntaxHighlighter
import org.example.project.domain.service.getSyntaxHighlighter
import org.example.project.domain.repository.RecipeRepository
import org.example.project.data.repository.SettingsRecipeRepositoryImpl
import org.example.project.data.firebase.FirebaseAuthClient
import org.example.project.data.firebase.FirebaseAuthClientApi

val appModule = module {
    single { Settings() }
    
    single<SyntaxHighlighter> { getSyntaxHighlighter() }
    single<RecipeRepository> { SettingsRecipeRepositoryImpl(settings = get()) }
    
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
    single<FirebaseAuthClientApi> { FirebaseAuthClient(httpClient = get()) }

    single<AuthRepository> { FirebaseAuthRepositoryImpl(firebaseAuthClient = get()) }
    single<AuthRepository>(named("local")) { AuthRepositoryImpl(settings = get(), hashService = get()) }
    
    factoryOf(::LoginWithEmailUseCase)
    factoryOf(::LoginWithGoogleUseCase)
    factoryOf(::ValidateEmailUseCase)
    factoryOf(::ValidatePasswordUseCase)
    factoryOf(::RegisterUseCase)
    factoryOf(::LoginViewModel)
    factoryOf(::RegisterViewModel)
    factoryOf(::AppViewModel)
    factoryOf(::MyRecipesViewModel)
}

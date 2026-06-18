package org.example.project.di

import org.example.project.data.repository.AuthRepositoryImpl
import org.example.project.domain.repository.AuthRepository
import org.example.project.domain.usecase.LoginWithEmailUseCase
import org.example.project.domain.usecase.RegisterUseCase
import org.example.project.presentation.login.LoginViewModel
import org.example.project.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import com.russhwolf.settings.Settings

val appModule = module {
    single { Settings() }
    single<AuthRepository> { AuthRepositoryImpl(settings = get()) }
    factoryOf(::LoginWithEmailUseCase)
    factoryOf(::RegisterUseCase)
    factoryOf(::LoginViewModel)
    factoryOf(::RegisterViewModel)
}

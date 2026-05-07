package com.currencyexchange.di

import com.currencyexchange.data.repository.ExchangeRateRepository
import com.currencyexchange.data.repository.ExchangeRateRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindExchangeRateRepository(
        impl: ExchangeRateRepositoryImpl,
    ): ExchangeRateRepository
}
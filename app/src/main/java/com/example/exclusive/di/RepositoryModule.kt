package com.example.exclusive.di

import com.example.exclusive.data.local.ILocalDataSource
import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.remote.CurrencyRemoteDataSource
import com.example.exclusive.data.remote.ICurrencyRemoteDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindShopifyRemoteDataSource(
        shopifyRemoteDataSourceImpl: ShopifyRemoteDataSourceImpl
    ): ShopifyRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindCurrencyRemoteDataSource(
        currencyRemoteDataSource: CurrencyRemoteDataSource
    ): ICurrencyRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindLocalDataSource(
        localDataSource: LocalDataSource
    ): ILocalDataSource
}
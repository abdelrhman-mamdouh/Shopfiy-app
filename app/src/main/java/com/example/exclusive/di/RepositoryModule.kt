package com.example.exclusive.di

import com.example.exclusive.data.remote.CartDataSourceImpl
import com.example.exclusive.data.remote.CheckoutDataSourceImpl
import com.example.exclusive.data.remote.CustomerDataSourceImpl
import com.example.exclusive.data.remote.OrderDataSourceImpl
import com.example.exclusive.data.remote.ProductDataSourceImpl
import com.example.exclusive.data.remote.RealtimeDatabaseDataSourceImpl
import com.example.exclusive.data.remote.interfaces.CartDataSource
import com.example.exclusive.data.remote.interfaces.CheckoutDataSource
import com.example.exclusive.data.remote.interfaces.CustomerDataSource
import com.example.exclusive.data.remote.interfaces.OrderDataSource
import com.example.exclusive.data.remote.interfaces.ProductDataSource
import com.example.exclusive.data.remote.interfaces.RealtimeDatabaseDataSource
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
    abstract fun bindProductRemoteDataSource(
        productRemoteDataSourceImpl: ProductDataSourceImpl
    ): ProductDataSource

    @Binds
    @Singleton
    abstract fun bindCartRemoteDataSource(
        cartDataSourceImpl: CartDataSourceImpl
    ): CartDataSource

    @Binds
    @Singleton
    abstract fun bindOrderRemoteDataSource(
        orderDataSourceImpl: OrderDataSourceImpl
    ): OrderDataSource

    @Binds
    @Singleton
    abstract fun bindRealtimeRemoteDataSource(
        realtimeDatabaseDataSourceImpl: RealtimeDatabaseDataSourceImpl
    ): RealtimeDatabaseDataSource

    @Binds
    @Singleton
    abstract fun bindCustomerRemoteDataSource(
        customerDataSourceImpl: CustomerDataSourceImpl
    ): CustomerDataSource

    @Binds
    @Singleton
    abstract fun bindCheckoutRemoteDataSource(
        checkoutDataSourceImpl: CheckoutDataSourceImpl
    ): CheckoutDataSource
}
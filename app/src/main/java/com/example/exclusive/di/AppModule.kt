package com.example.exclusive.di

import com.apollographql.apollo3.ApolloClient
import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.network.ApiService
import com.example.exclusive.data.network.DiscountApi
import com.example.exclusive.data.remote.ApolloService

import com.example.exclusive.data.remote.CurrencyRemoteDataSource

import com.example.exclusive.data.remote.DiscountDataSourceImpl

import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.data.repository.AddressRepository
import com.example.exclusive.data.repository.AddressRepositoryImpl
import com.example.exclusive.data.repository.CartRepository
import com.example.exclusive.data.repository.CartRepositoryImpl

import com.example.exclusive.data.repository.CurrencyRepository
import com.example.exclusive.data.repository.CurrencyRepositoryImpl

import com.example.exclusive.ui.home.repository.HomeRepository
import com.example.exclusive.ui.home.repository.HomeRepositoryImpl
import com.example.exclusive.ui.products.repository.ProductsRepository
import com.example.exclusive.ui.products.repository.ProductsRepositoryImpl

import com.example.exclusive.utilities.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseUrl1

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseUrl2


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS).build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    @BaseUrl1
    fun provideRetrofitInstanceForBaseUrl1(
        okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder().baseUrl(Constants.CURRENCY_BASE_URL).client(okHttpClient)
            .addConverterFactory(gsonConverterFactory).build()
    }


    @Singleton
    @Provides
    fun provideApiService(@BaseUrl1 retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }


    @Provides
    @BaseUrl2
    fun provideMyRetrofitInstance(): Retrofit {
        return Retrofit.Builder().baseUrl("https://mad44-android-sv-2.myshopify.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    fun provideDiscountApi(@BaseUrl2 retrofit: Retrofit): DiscountApi {
        return retrofit.create(DiscountApi::class.java)
    }

    @Singleton
    @Provides
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder().serverUrl(Constants.BASE_URL)
            .addHttpHeader("X-Shopify-Storefront-Access-Token", Constants.API_KEY).build()
    }

    @Singleton
    @Provides
    fun provideApolloService(apolloClient: ApolloClient): ApolloService {
        return ApolloService(apolloClient)
    }

    @Provides
    @Singleton
    fun provideCartRepository(
        remoteDataSource: ShopifyRemoteDataSource, localDataSource: LocalDataSource
    ): CartRepository {
        return CartRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Provides
    @Singleton
    fun provideAddressRepository(
        remoteDataSource: ShopifyRemoteDataSource, localDataSource: LocalDataSource
    ): AddressRepository {
        return AddressRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Provides
    @Singleton

    fun provideCurrenciesRepository(
        currencyRemoteDataSource: CurrencyRemoteDataSource, localDataSource: LocalDataSource
    ): CurrencyRepository {
        return CurrencyRepositoryImpl(currencyRemoteDataSource, localDataSource)
    }
    @Provides
    @Singleton
    fun provideHomeRepository(
        remoteDataSource: ShopifyRemoteDataSource, myRemoteDataSource: DiscountDataSourceImpl
    ): HomeRepository {
        return HomeRepositoryImpl(remoteDataSource, myRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideProductsRepository(
        remoteDataSource: ShopifyRemoteDataSource, localDataSource: LocalDataSource
    ): ProductsRepository {
        return ProductsRepositoryImpl(remoteDataSource, localDataSource)
    }

}
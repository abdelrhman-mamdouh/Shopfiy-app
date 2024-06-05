package com.example.exclusive.di

import com.apollographql.apollo3.ApolloClient
import com.example.exclusive.data.network.ApiService
import com.example.exclusive.data.network.DiscountApi
import com.example.exclusive.data.remote.ApolloService
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
    fun provideHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
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
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.CURRENCY_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }


    @Singleton
    @Provides
    fun provideApiService(@BaseUrl1 retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }


    @Provides
    @BaseUrl2
    fun provideMyRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://mad44-android-sv-2.myshopify.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideDiscountApi(@BaseUrl2 retrofit: Retrofit): DiscountApi {
        return retrofit.create(DiscountApi::class.java)
    }
    @Singleton
    @Provides
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(Constants.BASE_URL)
            .addHttpHeader("X-Shopify-Storefront-Access-Token", Constants.API_KEY)
            .build()
    }

    @Singleton
    @Provides
    fun provideApolloService(apolloClient: ApolloClient): ApolloService {
        return ApolloService(apolloClient)
    }
}

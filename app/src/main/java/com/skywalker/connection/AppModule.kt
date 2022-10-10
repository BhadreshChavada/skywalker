package com.skywalker.connection

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.prefs.Preferences
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRemoteApiService(retrofit: Retrofit): RemoteApiService {
        return retrofit.create(RemoteApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://18.235.75.7:5000/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }


    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        okHttpClientBuilder.addInterceptor(logging)
        okHttpClientBuilder.addInterceptor(Interceptor {
            val request = it.request().newBuilder().addHeader("device-type", "mobile").build()
            it.proceed(request)
        })
        return okHttpClientBuilder.build()
    }

}
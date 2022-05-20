package cat.oscarromero.randomuser.data.api

import okhttp3.logging.HttpLoggingInterceptor

fun provideOkHttpInterceptors() =
    listOf(HttpLoggingInterceptor().apply { this.level = HttpLoggingInterceptor.Level.BODY })

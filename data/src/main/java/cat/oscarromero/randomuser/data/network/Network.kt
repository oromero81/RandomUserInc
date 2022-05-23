package cat.oscarromero.randomuser.data.network

import cat.oscarromero.randomuser.data.api.provideOkHttpInterceptors
import cat.oscarromero.randomuser.data.dto.GenericErrorDto
import cat.oscarromero.randomuser.data.network.adapter.NetworkResponse
import cat.oscarromero.randomuser.data.network.adapter.NetworkResponseAdapterFactory
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object Network {
    private const val CACHE_SIZE: Long = 24 * 1024 * 1024
    private const val TIMEOUT_REQUEST: Long = 45

    fun createRetrofitBuilder(
        converter: Converter.Factory = GsonConverterFactory.create(),
        adapter: CallAdapter.Factory = NetworkResponseAdapterFactory(),
        baseUrl: String
    ): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converter)
            .addCallAdapterFactory(adapter)

    fun createOkHttpBuilder(
        cacheDir: File,
        interceptors: List<Interceptor>? = provideOkHttpInterceptors()
    ): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
            .cache(Cache(cacheDir, CACHE_SIZE))
            .connectTimeout(TIMEOUT_REQUEST, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_REQUEST, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_REQUEST, TimeUnit.SECONDS)

        interceptors?.let {
            it.forEach { interceptor ->
                builder.addNetworkInterceptor(interceptor)
            }
        }

        return builder
    }

    fun createOkHttpClient(builder: OkHttpClient.Builder): OkHttpClient = builder.build()

    fun createRetrofit(client: OkHttpClient, builder: Retrofit.Builder): Retrofit =
        builder.client(client).build()
}

typealias GenericResponse<S> = NetworkResponse<S, GenericErrorDto>

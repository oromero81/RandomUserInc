package cat.oscarromero.randomuser.di

import android.content.Context
import cat.oscarromero.randomuser.data.network.Network
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun okHttpBuilderProvider(@ApplicationContext context: Context): OkHttpClient.Builder =
        Network.createOkHttpBuilder(context.cacheDir)

    @Provides
    fun okHttpClientProvider(okHttpBuilder: OkHttpClient.Builder): OkHttpClient =
        Network.createOkHttpClient(okHttpBuilder)

    @Provides
    fun retrofitBuilderProvider(): Retrofit.Builder =
        Network.createRetrofitBuilder(baseUrl = "https://api.randomuser.me/")

    @Provides
    fun retrofitProvider(client: OkHttpClient, builder: Retrofit.Builder): Retrofit =
        Network.createRetrofit(client, builder)
}

package cat.oscarromero.randomuser.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CommonModule {

    @Provides
    fun sharedPreferenceProvider(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)

    companion object {
        private const val KEY_SHARED_PREFERENCES = "shared_preferences_random_users"
    }
}

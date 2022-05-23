package cat.oscarromero.randomuser.di

import cat.oscarromero.randomuser.data.repository.UsersRepositoryLocal
import cat.oscarromero.randomuser.data.repository.UsersRepositoryNetwork
import cat.oscarromero.randomuser.domain.LocalImplementation
import cat.oscarromero.randomuser.domain.NetworkImplementation
import cat.oscarromero.randomuser.domain.repository.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UsersModule {

    @Binds
    @NetworkImplementation
    abstract fun usersRepositoryNetworkProvider(usersRepositoryNetwork: UsersRepositoryNetwork): UsersRepository

    @Binds
    @LocalImplementation
    abstract fun usersRepositoryLocalProvider(usersRepositoryLocal: UsersRepositoryLocal): UsersRepository
}

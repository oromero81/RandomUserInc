package cat.oscarromero.randomuser.domain.usecase

import cat.oscarromero.randomuser.domain.LocalImplementation
import cat.oscarromero.randomuser.domain.NetworkImplementation
import cat.oscarromero.randomuser.domain.model.User
import cat.oscarromero.randomuser.domain.repository.UsersRepository
import javax.inject.Inject

class ObtainMoreUsers @Inject constructor(
    @LocalImplementation private val usersRepositoryLocal: UsersRepository,
    @NetworkImplementation private val usersRepositoryNetwork: UsersRepository
) : UseCase<Unit, List<User>>() {

    override suspend fun run(params: Unit): Result<List<User>, FailureType> {
        val networkUsers = usersRepositoryNetwork.obtainUsers()
        val savedDeletedUsers = usersRepositoryLocal.obtainDeletedUsers()

        return if (networkUsers is Result.Success && savedDeletedUsers is Result.Success) {
            val users =
                networkUsers.successData.distinctBy { it.id } + savedDeletedUsers.successData
            val usersFiltered =
                users.groupBy { it.id }.filter { it.value.size == 1 }.flatMap { it.value }

            usersRepositoryLocal.saveUsers(usersFiltered)

            Result.Success(usersFiltered)
        } else {
            networkUsers
        }
    }
}

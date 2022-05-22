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

        return if (networkUsers is Result.Success) {
            val usersFiltered = networkUsers.successData.distinctBy { it.id }

            usersRepositoryLocal.saveUsers(usersFiltered)

            Result.Success(usersFiltered)
        } else {
            networkUsers
        }
    }
}

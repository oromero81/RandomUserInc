package cat.oscarromero.randomuser.domain.usecase

import cat.oscarromero.randomuser.domain.model.User
import cat.oscarromero.randomuser.domain.repository.UsersRepository

class ObtainUsers(
    private val usersRepositoryLocal: UsersRepository,
    private val usersRepositoryNetwork: UsersRepository
) : UseCase<Unit, List<User>>() {

    override suspend fun run(params: Unit): Result<List<User>, FailureType> {
        val localUsers = usersRepositoryLocal.obtainUsers()

        return if (localUsers is Result.Success) {
            if (localUsers.successData.isEmpty()) {
                val networkUsers = usersRepositoryNetwork.obtainUsers()

                if (networkUsers is Result.Success) {
                    val usersFiltered = networkUsers.successData.distinctBy { it.id }

                    usersRepositoryLocal.saveUsers(usersFiltered)

                    Result.Success(usersFiltered)
                } else {
                    networkUsers
                }
            } else {
                localUsers
            }
        } else {
            localUsers
        }
    }
}

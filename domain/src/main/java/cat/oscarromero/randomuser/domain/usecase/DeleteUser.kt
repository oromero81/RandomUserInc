package cat.oscarromero.randomuser.domain.usecase

import cat.oscarromero.randomuser.domain.LocalImplementation
import cat.oscarromero.randomuser.domain.repository.UsersRepository
import javax.inject.Inject

class DeleteUser @Inject constructor(@LocalImplementation private val usersRepository: UsersRepository) :
    UseCase<String, Unit>() {

    override suspend fun run(params: String): Result<Unit, FailureType> =
        usersRepository.deleteUser(params)
}

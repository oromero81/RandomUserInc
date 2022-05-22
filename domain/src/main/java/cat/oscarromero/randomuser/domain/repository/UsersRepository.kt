package cat.oscarromero.randomuser.domain.repository

import cat.oscarromero.randomuser.domain.model.User
import cat.oscarromero.randomuser.domain.usecase.FailureType
import cat.oscarromero.randomuser.domain.usecase.Result

interface UsersRepository {

    suspend fun obtainUsers(): Result<List<User>, FailureType>
    suspend fun saveUsers(users: List<User>): Result<Unit, FailureType>
    suspend fun deleteUser(userId: String): Result<Unit, FailureType>
    suspend fun obtainDeletedUsers(): Result<List<User>, FailureType>
}

package cat.oscarromero.randomuser.data.repository

import cat.oscarromero.randomuser.data.handleGenericResponseError
import cat.oscarromero.randomuser.data.network.adapter.NetworkResponse
import cat.oscarromero.randomuser.data.network.api.RandomUsersApi
import cat.oscarromero.randomuser.domain.model.Location
import cat.oscarromero.randomuser.domain.model.User
import cat.oscarromero.randomuser.domain.repository.UsersRepository
import cat.oscarromero.randomuser.domain.usecase.FailureType
import cat.oscarromero.randomuser.domain.usecase.Result
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class UsersRepositoryNetwork @Inject constructor(private val retrofit: Retrofit) : UsersRepository {

    override suspend fun obtainUsers(): Result<List<User>, FailureType> {
        val api = retrofit.create(RandomUsersApi::class.java)
        val response = api.getUsers(USERS_REQUESTED)

        return if (response is NetworkResponse.Success) {
            response.body?.let {
                Result.Success(
                    it.results
                        .filter { dto -> dto.id?.value != null }
                        .map { dto ->
                            with(dto) {
                                User(
                                    id!!.value!!,
                                    name?.first ?: "",
                                    name?.last ?: "",
                                    email ?: "",
                                    picture?.large ?: "",
                                    phone ?: "",
                                    Location(
                                        location?.street?.name ?: "",
                                        location?.city ?: "",
                                        location?.state ?: ""
                                    ),
                                    gender ?: "",
                                    if (registered?.date != null) {
                                        SimpleDateFormat(REGISTER_DATE_FORMAT).parse(registered.date)
                                    } else {
                                        Date()
                                    }
                                )
                            }
                        })
            } ?: Result.Failure(FailureType.ServerFailure("Body is null"))
        } else {
            handleGenericResponseError(response)
        }
    }

    override suspend fun saveUsers(users: List<User>): Result<Unit, FailureType> =
        Result.Failure(FailureType.UnknownFailure("Function not implemented, you should use Network implementation version"))

    override suspend fun deleteUser(userId: String): Result<Unit, FailureType> =
        Result.Failure(FailureType.UnknownFailure("Function not implemented, you should use Network implementation version"))

    override suspend fun obtainDeletedUsers(): Result<List<User>, FailureType> =
        Result.Failure(FailureType.UnknownFailure("Function not implemented, you should use Network implementation version"))

    companion object {
        private const val USERS_REQUESTED = 40
        private const val REGISTER_DATE_FORMAT = "yyyy-MM-dd"
    }
}

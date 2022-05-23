package cat.oscarromero.randomuser.data

import cat.oscarromero.randomuser.data.network.GenericResponse
import cat.oscarromero.randomuser.data.network.adapter.NetworkResponse
import cat.oscarromero.randomuser.domain.usecase.FailureType
import cat.oscarromero.randomuser.domain.usecase.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun handleGenericResponseError(response: GenericResponse<Any>): Result<Nothing, FailureType> {
    return when (response) {
        is NetworkResponse.ApiError -> Result.Failure(FailureType.ServerFailure("${response.code} - ${response.body.error}"))
        is NetworkResponse.NetworkError -> Result.Failure(
            FailureType.NetworkConnectionFailure(
                response.exception.message ?: "Error without message"
            )
        )
        is NetworkResponse.UnknownError -> Result.Failure(
            FailureType.ServerFailure(
                response.error.message ?: "Error without message"
            )
        )
        else -> Result.Failure(FailureType.UnknownFailure())
    }
}

fun toJson(src: Any): String = Gson().toJson(src)

inline fun <reified T> parseToList(json: String): List<T> {
    val typeOfT = TypeToken.getParameterized(MutableList::class.java, T::class.java).type
    return Gson().fromJson(json, typeOfT)
}

inline fun <reified T> parseToObject(json: String): T = Gson().fromJson(json, T::class.java)

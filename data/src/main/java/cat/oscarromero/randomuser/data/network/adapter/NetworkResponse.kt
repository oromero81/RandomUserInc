package cat.oscarromero.randomuser.data.network.adapter

import java.io.IOException

sealed class NetworkResponse<out T, out U> {
    data class Success<T>(val body: T?, val code: Int) : NetworkResponse<T, Nothing>()
    data class ApiError<U>(val body: U, val code: Int) : NetworkResponse<Nothing, U>()
    data class NetworkError(val exception: IOException) : NetworkResponse<Nothing, Nothing>()
    data class UnknownError(val error: Throwable) : NetworkResponse<Nothing, Nothing>()
}

package cat.oscarromero.randomuser.domain.usecase

sealed class Result<out R, out F : FailureType> {
    data class Success<out R>(val successData: R) : Result<R, Nothing>()
    data class Failure<out F : FailureType>(val failureData: F) : Result<Nothing, F>()
}

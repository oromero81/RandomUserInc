package cat.oscarromero.randomuser.domain.usecase

sealed class FailureType(val failureMessage: String) {
    data class NetworkConnectionFailure(val message: String = "") : FailureType(message)
    data class ServerFailure(val message: String = "") : FailureType(message)
    data class UnknownFailure(val message: String = "") : FailureType(message)
}

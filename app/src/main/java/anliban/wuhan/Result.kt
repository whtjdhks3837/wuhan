package anliban.wuhan

sealed class Result<out T, out R : ErrorStatus> {

    data class Success<T>(val data: T) : Result<T, Nothing>()

    data class Error<R : ErrorStatus>(val error: R) : Result<Nothing, R>()

    companion object {

        fun <T> success(data: T) = Success(data)

        fun <R : ErrorStatus> error(error: R) = Error(error)
    }
}
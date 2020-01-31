package anliban.wuhan

enum class ErrorStatus(val code: Int) {
    OK(200), BAD_REQUEST(400), UN_AUTHORIZATION(401), FORBIDDEN(403),
    SERVER_ERROR(500)
}
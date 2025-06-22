package clem.project.buzz.utils

enum class ResponseCode(val code: Int) {
    SUCCESS(0),
    NO_RESULTS(1),
    INVALID_PARAMETER(2),
    TOKEN_NOT_FOUND(3),
    TOKEN_EMPTY(4),
    RATE_LIMIT(5),
    UNKNOWN(-1);

    companion object {
        fun fromCode(code: Int): ResponseCode {
            return entries.find { it.code == code } ?: UNKNOWN
        }
    }
}
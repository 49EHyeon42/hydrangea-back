package dev.ehyeon.hydrangea.common.response

import dev.ehyeon.hydrangea.common.response.code.ResponseCode

class BaseResponse<T> private constructor(
    val code: String,
    val message: String,
    val data: T?,
) {
    companion object {
        fun <T> success(data: T? = null): BaseResponse<T> =
            BaseResponse(code = ResponseCode.SUCCESS.code, message = ResponseCode.SUCCESS.defaultMessage, data = data)

        fun <T> failure(code: ResponseCode = ResponseCode.FAILURE, message: String? = null): BaseResponse<T> =
            BaseResponse(code = code.code, message = message ?: code.defaultMessage, data = null)
    }
}

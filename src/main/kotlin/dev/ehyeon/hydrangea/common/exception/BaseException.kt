package dev.ehyeon.hydrangea.common.exception

open class BaseException(
    override val message: String,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)

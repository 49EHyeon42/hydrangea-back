package dev.ehyeon.hydrangea.advertising.exception

import dev.ehyeon.hydrangea.common.exception.BaseException

class SafetyDreamOpenApiException(
    cause: Throwable? = null,
) : BaseException("SAFETY_DREAM_OPEN_API", cause)

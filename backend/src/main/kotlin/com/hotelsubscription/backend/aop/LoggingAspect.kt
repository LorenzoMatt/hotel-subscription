package com.hotelsubscription.backend.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class LoggingAspect {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Around("execution(* com.hotelsubscription.backend..*(..)) && !within(is(FinalType))")
    fun logAroundAllMethods(joinPoint: ProceedingJoinPoint): Any? {
        val methodName = joinPoint.signature.toShortString()
        logger.info("Entering method: {}", methodName)
        logger.debug("Entering method: {} with arguments: {}", methodName, joinPoint.args)

        val startTime = System.currentTimeMillis()
        try {
            val result = joinPoint.proceed()
            logger.debug("Exiting method: {} with result: {}", methodName, result)
            return result
        } catch (ex: Exception) {
            logger.error("Exception in method: {} with message: {}", methodName, ex.message, ex)
            throw ex
        } finally {
            val endTime = System.currentTimeMillis()
            logger.info("Exiting method {}, execution time: {} ms", methodName, endTime - startTime)
        }
    }
}

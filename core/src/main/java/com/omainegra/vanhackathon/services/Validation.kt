package com.omainegra.vanhackathon.services

import arrow.core.Either

/**
 * Created by omainegra on 3/18/18.
 */

fun validateEmpty(text: String): Either<Exception, String> =
        if (text.isEmpty()) Either.Left(Exception("Can't be empty"))
        else Either.Right(text)

fun validateLength(text: String, length: Int): Either<Exception, String> =
        if (text.length < length) Either.Left(Exception("At least $length characters"))
        else Either.Right(text)

fun validateEmail(text: String): Either<Exception, String> =
        if (!emailPattern.matches(text)) Either.Left(Exception("Invalid emailChanged addressChanged"))
        else Either.Right(text)

val emailPattern =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$".toRegex()
package com.omainegra.vanhackathon.model

import arrow.core.Option
import java.time.ZonedDateTime

/**
 * Created by omainegra on 3/18/18.
 */
data class NewCustomer (val name: String, val address: String, val email: String, val password: String)
data class Customer (val id: Long, val name: String, val address: String, val email: String, val password: String, val createdAt: ZonedDateTime)


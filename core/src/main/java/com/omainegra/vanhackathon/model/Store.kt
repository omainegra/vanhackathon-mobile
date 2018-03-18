package com.omainegra.vanhackathon.model

import arrow.core.Option
import java.time.ZonedDateTime

/**
 * Created by omainegra on 3/18/18.
 */
data class Store (val id: Long, val name: String, val address: String)
data class Product (val id: Long, val storeId: Long, val name: String, val description: String, val price: Double)


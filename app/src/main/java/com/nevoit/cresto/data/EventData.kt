package com.nevoit.cresto.data

import kotlinx.serialization.Serializable

@Serializable
data class EventResponse(
    val quantity: Int,
    val items: List<EventItem>
)

@Serializable
data class EventItem(
    val title: String,
    val date: String
)

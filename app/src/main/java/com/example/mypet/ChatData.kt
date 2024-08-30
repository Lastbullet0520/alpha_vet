package com.example.mypet

data class Message(
    val userId: String = "",
    val text: String = "",
    val imageUrl: String? = null,
    val timestamp: String = ""
)

data class UserInfo(
    val userId: String = "",
    val password: String = ""
)
data class ChatRoom(
    val id: String = "",
    val name: String = "",
    val users: Map<String, Boolean> = emptyMap()
)
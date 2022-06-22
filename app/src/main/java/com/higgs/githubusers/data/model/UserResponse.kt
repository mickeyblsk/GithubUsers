package com.higgs.githubusers.data.model

data class UserResponse (
    val login: String,
    val id: Int,
    val followers: Int,
    val following: Int,
    val avatar_url: String,
    val name: String,
    val bio: String,
    val location: String,
    val blog: String,
    val site_admin: Boolean
)
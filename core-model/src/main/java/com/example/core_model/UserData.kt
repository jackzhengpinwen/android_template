package com.example.core_model

data class UserData(
    val bookmarkedNewsResources: Set<String>,
    val followedTopics: Set<String>,
    val followedAuthors: Set<String>
)

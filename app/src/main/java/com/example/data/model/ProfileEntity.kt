package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey
    val id: Int = 1,
    val username: String = "@my_insta_page",
    val displayName: String = "Creator Hub",
    val niche: String = "Tech & Gadgets",
    val followers: Int = 12400,
    val following: Int = 350,
    val totalPosts: Int = 48,
    val bio: String = "🚀 Helping you master Tech & AI\n💡 Daily tips, gadgets & reels\n👇 Grab the free toolkit below",
    val profileHealthScore: Int = 82,
    val bioAuditFeedback: String? = "Good clear value proposition. Add social proof (e.g. 'Trusted by 12K+' or 'Seen on...') to increase follow conversion.",
    val avatarUri: String? = null
)

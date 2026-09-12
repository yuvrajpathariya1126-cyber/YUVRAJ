package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val postType: String, // "Reel", "Carousel", "Single Photo", "Story"
    val caption: String,
    val hashtags: String,
    val mediaUri: String? = null,
    val likes: Int = 0,
    val comments: Int = 0,
    val shares: Int = 0,
    val saves: Int = 0,
    val reach: Int = 1,
    val impressions: Int = 1,
    val dateEpoch: Long = System.currentTimeMillis(),
    val engagementRate: Float = 0f,
    val saveToReachRatio: Float = 0f,
    val aiAuditScore: Int? = null,
    val aiHookFeedback: String? = null,
    val aiStrengths: String? = null,
    val aiImprovements: String? = null,
    val aiHashtagAdvice: String? = null,
    val aiViralRating: String? = null // "Low", "Moderate", "High", "Viral Potential"
)

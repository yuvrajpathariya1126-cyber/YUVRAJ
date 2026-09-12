package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.BioAuditResult
import com.example.ai.GeminiAnalyzer
import com.example.ai.PostAnalysisResult
import com.example.data.InstaDatabase
import com.example.data.InstaRepository
import com.example.data.model.PostEntity
import com.example.data.model.ProfileEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab(val titleEn: String, val titleHi: String) {
    OVERVIEW("Overview", "ओवरव्यू"),
    POST_ANALYZER("Post Analyzer", "पोस्ट एनालाइजर"),
    PAGE_GROWTH("Growth Guide", "ग्रोथ गाइड"),
    CAPTION_STUDIO("Caption Studio", "कैप्शन स्टूडियो")
}

class InstaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: InstaRepository

    init {
        val db = InstaDatabase.getDatabase(application, viewModelScope)
        repository = InstaRepository(db.instaDao())
    }

    val posts: StateFlow<List<PostEntity>> = repository.allPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val profile: StateFlow<ProfileEntity?> = repository.profileFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _selectedTab = MutableStateFlow(AppTab.OVERVIEW)
    val selectedTab: StateFlow<AppTab> = _selectedTab.asStateFlow()

    private val _isAnalyzingPost = MutableStateFlow(false)
    val isAnalyzingPost: StateFlow<Boolean> = _isAnalyzingPost.asStateFlow()

    private val _lastAnalysisResult = MutableStateFlow<PostAnalysisResult?>(null)
    val lastAnalysisResult: StateFlow<PostAnalysisResult?> = _lastAnalysisResult.asStateFlow()

    private val _bioAuditResult = MutableStateFlow<BioAuditResult?>(null)
    val bioAuditResult: StateFlow<BioAuditResult?> = _bioAuditResult.asStateFlow()

    private val _isAuditingBio = MutableStateFlow(false)
    val isAuditingBio: StateFlow<Boolean> = _isAuditingBio.asStateFlow()

    private val _generatedCaptions = MutableStateFlow<List<String>>(emptyList())
    val generatedCaptions: StateFlow<List<String>> = _generatedCaptions.asStateFlow()

    private val _isGeneratingCaptions = MutableStateFlow(false)
    val isGeneratingCaptions: StateFlow<Boolean> = _isGeneratingCaptions.asStateFlow()

    private val _userLanguage = MutableStateFlow("Hindi") // "Hindi" or "English"
    val userLanguage: StateFlow<String> = _userLanguage.asStateFlow()

    fun setTab(tab: AppTab) {
        _selectedTab.value = tab
    }

    fun toggleLanguage() {
        _userLanguage.value = if (_userLanguage.value == "Hindi") "English" else "Hindi"
    }

    fun analyzeAndSavePost(
        postType: String,
        caption: String,
        hashtags: String,
        mediaUri: String?,
        likes: Int,
        comments: Int,
        shares: Int,
        saves: Int,
        reach: Int,
        impressions: Int,
        onComplete: (Long) -> Unit = {}
    ) {
        viewModelScope.launch {
            _isAnalyzingPost.value = true
            val effectiveReach = if (reach <= 0) 1 else reach
            val totalInteractions = likes + comments + shares + saves
            val er = (totalInteractions.toFloat() / effectiveReach.toFloat()) * 100f
            val saveRatio = (saves.toFloat() / effectiveReach.toFloat()) * 100f

            val currentNiche = profile.value?.niche ?: "Creator"
            val audit = GeminiAnalyzer.analyzePost(
                postType = postType,
                caption = caption,
                hashtags = hashtags,
                likes = likes,
                comments = comments,
                shares = shares,
                saves = saves,
                reach = effectiveReach,
                niche = currentNiche
            )

            _lastAnalysisResult.value = audit

            val entity = PostEntity(
                postType = postType,
                caption = caption,
                hashtags = hashtags,
                mediaUri = mediaUri,
                likes = likes,
                comments = comments,
                shares = shares,
                saves = saves,
                reach = effectiveReach,
                impressions = if (impressions <= 0) effectiveReach else impressions,
                dateEpoch = System.currentTimeMillis(),
                engagementRate = er,
                saveToReachRatio = saveRatio,
                aiAuditScore = audit.score,
                aiHookFeedback = audit.hookFeedback,
                aiStrengths = audit.strengths,
                aiImprovements = audit.improvements,
                aiHashtagAdvice = audit.hashtagAdvice,
                aiViralRating = audit.viralRating
            )

            val insertedId = repository.insertPost(entity)
            _isAnalyzingPost.value = false
            onComplete(insertedId)
        }
    }

    fun deletePost(id: Long) {
        viewModelScope.launch {
            repository.deletePost(id)
        }
    }

    fun auditProfileBio(username: String, niche: String, bio: String) {
        viewModelScope.launch {
            _isAuditingBio.value = true
            val result = GeminiAnalyzer.auditBio(username, niche, bio)
            _bioAuditResult.value = result

            // Update profile with health score
            val current = repository.getProfile()
            if (current != null) {
                repository.saveProfile(
                    current.copy(
                        profileHealthScore = result.score,
                        bioAuditFeedback = result.critique
                    )
                )
            }
            _isAuditingBio.value = false
        }
    }

    fun updateProfile(
        username: String,
        displayName: String,
        niche: String,
        followers: Int,
        following: Int,
        totalPosts: Int,
        bio: String
    ) {
        viewModelScope.launch {
            val existing = repository.getProfile() ?: ProfileEntity()
            val updated = existing.copy(
                username = username,
                displayName = displayName,
                niche = niche,
                followers = followers,
                following = following,
                totalPosts = totalPosts,
                bio = bio
            )
            repository.saveProfile(updated)
        }
    }

    fun generateCaptions(topic: String, niche: String, tone: String, language: String) {
        viewModelScope.launch {
            _isGeneratingCaptions.value = true
            val captions = GeminiAnalyzer.generateCaptionsAndHooks(topic, niche, tone, language)
            _generatedCaptions.value = captions
            _isGeneratingCaptions.value = false
        }
    }

    fun clearLastAnalysis() {
        _lastAnalysisResult.value = null
    }
}

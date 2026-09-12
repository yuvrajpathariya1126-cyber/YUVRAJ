package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.InstaTopBar
import com.example.ui.screens.CaptionStudioScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.PageOptimizerScreen
import com.example.ui.screens.PostAnalyzerScreen
import com.example.ui.screens.ProfileEditDialog
import com.example.ui.theme.InstaPink
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppTab
import com.example.viewmodel.InstaViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: InstaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppScreen(viewModel: InstaViewModel) {
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val posts by viewModel.posts.collectAsStateWithLifecycle()
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val language by viewModel.userLanguage.collectAsStateWithLifecycle()

    val isAnalyzingPost by viewModel.isAnalyzingPost.collectAsStateWithLifecycle()
    val lastAnalysisResult by viewModel.lastAnalysisResult.collectAsStateWithLifecycle()
    val isAuditingBio by viewModel.isAuditingBio.collectAsStateWithLifecycle()
    val bioAuditResult by viewModel.bioAuditResult.collectAsStateWithLifecycle()
    val generatedCaptions by viewModel.generatedCaptions.collectAsStateWithLifecycle()
    val isGeneratingCaptions by viewModel.isGeneratingCaptions.collectAsStateWithLifecycle()

    var showProfileDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            InstaTopBar(
                language = language,
                onToggleLanguage = { viewModel.toggleLanguage() },
                onOpenProfile = { showProfileDialog = true }
            )
        },
        bottomBar = {
            NavigationBar(
                windowInsets = WindowInsets.navigationBars,
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                AppTab.values().forEach { tab ->
                    val isSelected = selectedTab == tab
                    val label = if (language == "Hindi") tab.titleHi else tab.titleEn
                    val icon = when (tab) {
                        AppTab.OVERVIEW -> Icons.Default.Dashboard
                        AppTab.POST_ANALYZER -> Icons.Default.Analytics
                        AppTab.PAGE_GROWTH -> Icons.Default.TrendingUp
                        AppTab.CAPTION_STUDIO -> Icons.Default.EditNote
                    }

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.setTab(tab) },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = label
                            )
                        },
                        label = {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = InstaPink,
                            selectedTextColor = InstaPink,
                            indicatorColor = InstaPink.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                AppTab.OVERVIEW -> {
                    DashboardScreen(
                        profile = profile,
                        posts = posts,
                        language = language,
                        onNavigateTab = { viewModel.setTab(it) },
                        onDeletePost = { viewModel.deletePost(it) },
                        onOpenProfileEdit = { showProfileDialog = true }
                    )
                }
                AppTab.POST_ANALYZER -> {
                    PostAnalyzerScreen(
                        language = language,
                        isAnalyzing = isAnalyzingPost,
                        lastResult = lastAnalysisResult,
                        onAnalyzeAndSave = { type, caption, hashtags, mediaUri, likes, comments, shares, saves, reach, impressions ->
                            viewModel.analyzeAndSavePost(
                                postType = type,
                                caption = caption,
                                hashtags = hashtags,
                                mediaUri = mediaUri,
                                likes = likes,
                                comments = comments,
                                shares = shares,
                                saves = saves,
                                reach = reach,
                                impressions = impressions
                            )
                        },
                        onClearResult = { viewModel.clearLastAnalysis() }
                    )
                }
                AppTab.PAGE_GROWTH -> {
                    PageOptimizerScreen(
                        profile = profile,
                        language = language,
                        bioAuditResult = bioAuditResult,
                        isAuditingBio = isAuditingBio,
                        onAuditBio = { u, n, b -> viewModel.auditProfileBio(u, n, b) },
                        onApplyNewBio = { newBio ->
                            profile?.let {
                                viewModel.updateProfile(
                                    username = it.username,
                                    displayName = it.displayName,
                                    niche = it.niche,
                                    followers = it.followers,
                                    following = it.following,
                                    totalPosts = it.totalPosts,
                                    bio = newBio
                                )
                            }
                        }
                    )
                }
                AppTab.CAPTION_STUDIO -> {
                    CaptionStudioScreen(
                        language = language,
                        generatedCaptions = generatedCaptions,
                        isGenerating = isGeneratingCaptions,
                        onGenerateCaptions = { topic, niche, tone, lang ->
                            viewModel.generateCaptions(topic, niche, tone, lang)
                        }
                    )
                }
            }
        }

        if (showProfileDialog) {
            ProfileEditDialog(
                profile = profile,
                language = language,
                onDismiss = { showProfileDialog = false },
                onSave = { username, displayName, niche, followers, following, totalPosts, bio ->
                    viewModel.updateProfile(
                        username = username,
                        displayName = displayName,
                        niche = niche,
                        followers = followers,
                        following = following,
                        totalPosts = totalPosts,
                        bio = bio
                    )
                    showProfileDialog = false
                }
            )
        }
    }
}

// Keep Greeting for screenshot test backward compatibility
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

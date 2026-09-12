package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.PostEntity
import com.example.data.model.ProfileEntity
import com.example.ui.components.MetricCard
import com.example.ui.theme.InstaCoral
import com.example.ui.theme.InstaGradient
import com.example.ui.theme.InstaPink
import com.example.ui.theme.InstaPurple
import com.example.ui.theme.MetricGreen
import com.example.ui.theme.MetricOrange
import com.example.viewmodel.AppTab

@Composable
fun DashboardScreen(
    profile: ProfileEntity?,
    posts: List<PostEntity>,
    language: String,
    onNavigateTab: (AppTab) -> Unit,
    onDeletePost: (Long) -> Unit,
    onOpenProfileEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedPostForDetail by remember { mutableStateOf<PostEntity?>(null) }

    // Calculate aggregated metrics
    val totalPostsCount = posts.size
    val avgER = if (posts.isNotEmpty()) posts.map { it.engagementRate }.average().toFloat() else 0f
    val avgSaveRatio = if (posts.isNotEmpty()) posts.map { it.saveToReachRatio }.average().toFloat() else 0f
    val totalReach = posts.sumOf { it.reach.toLong() }
    val bestPost = posts.maxByOrNull { it.engagementRate }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Hero Banner
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(135.dp)
                    .clip(RoundedCornerShape(20.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hero_banner),
                    contentDescription = "Instagram Growth Hero",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color(0xCC000000))
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp)
                ) {
                    Text(
                        text = if (language == "Hindi") "🚀 इंस्टाग्राम पेज ग्रोथ स्टूडियो" else "🚀 Instagram Growth Studio",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    )
                    Text(
                        text = if (language == "Hindi") "अपनी पोस्ट्स का AI विश्लेषण करें और रीच 3x बढ़ाएं" else "AI post audits, viral hook analyzer & algorithm insights",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFE0E0E0),
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }

        // Profile Overview & Health Score Card
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                        RoundedCornerShape(20.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(InstaGradient),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = profile?.displayName?.take(1)?.uppercase() ?: "C",
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = profile?.displayName ?: "Creator Hub",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Text(
                                    text = profile?.username ?: "@creator",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = InstaPink,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                                Text(
                                    text = "Niche: ${profile?.niche ?: "Tech"}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }

                        // Health Score Circle
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(56.dp)
                        ) {
                            val score = profile?.profileHealthScore ?: 80
                            CircularProgressIndicator(
                                progress = { score / 100f },
                                modifier = Modifier.fillMaxSize(),
                                color = if (score >= 80) MetricGreen else MetricOrange,
                                trackColor = MaterialTheme.colorScheme.surface,
                                strokeWidth = 5.dp
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$score",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Text(
                                    text = "SCORE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 7.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Follower & Post Counters
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ProfileStatItem(
                            label = if (language == "Hindi") "फॉलोअर्स" else "Followers",
                            value = formatCount(profile?.followers ?: 12400)
                        )
                        ProfileStatItem(
                            label = if (language == "Hindi") "फॉलोइंग" else "Following",
                            value = formatCount(profile?.following ?: 350)
                        )
                        ProfileStatItem(
                            label = if (language == "Hindi") "कुल पोस्ट" else "Total Posts",
                            value = "${profile?.totalPosts ?: 48}"
                        )
                        ProfileStatItem(
                            label = if (language == "Hindi") "एवरेज ER%" else "Avg ER%",
                            value = "${String.format("%.1f", avgER)}%"
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Bio Audit Summary Tip
                    if (!profile?.bioAuditFeedback.isNullOrBlank()) {
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(InstaPurple.copy(alpha = 0.08f))
                                .padding(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = InstaPink,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = profile?.bioAuditFeedback ?: "",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }
                }
            }
        }

        // 4 Key Analytics Metrics
        item {
            Text(
                text = if (language == "Hindi") "📊 मुख्य एनालिटिक्स" else "📊 Core Analytics",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = if (language == "Hindi") "एंगेजमेंट रेट" else "Engagement Rate",
                    value = "${String.format("%.1f", avgER)}%",
                    subtitle = if (avgER >= 5f) "🔥 Great (High)" else "📈 Average",
                    icon = Icons.Default.Insights,
                    iconColor = InstaPink,
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = if (language == "Hindi") "सेव रेशियो" else "Save-to-Reach",
                    value = "${String.format("%.2f", avgSaveRatio)}%",
                    subtitle = if (avgSaveRatio >= 2f) "⭐ Viral Ready" else "🎯 Target: >2%",
                    icon = Icons.Default.Bookmark,
                    iconColor = MetricGreen,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = if (language == "Hindi") "कुल रीच" else "Total Reach",
                    value = formatCount(totalReach.toInt()),
                    subtitle = if (language == "Hindi") "$totalPostsCount पोस्ट्स से" else "Across $totalPostsCount posts",
                    icon = Icons.Default.Visibility,
                    iconColor = InstaCoral,
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = if (language == "Hindi") "बेस्ट पोस्ट ER" else "Top Post ER",
                    value = if (bestPost != null) "${String.format("%.1f", bestPost.engagementRate)}%" else "0%",
                    subtitle = bestPost?.postType ?: "None yet",
                    icon = Icons.Default.RocketLaunch,
                    iconColor = InstaPurple,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Quick Action Buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onNavigateTab(AppTab.POST_ANALYZER) },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = InstaPink),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("action_analyze_post")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (language == "Hindi") "नई पोस्ट जोड़ें" else "Analyze Post",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = { onNavigateTab(AppTab.PAGE_GROWTH) },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("action_page_growth")
                ) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp), tint = InstaPurple)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (language == "Hindi") "पेज ग्रोथ गाइड" else "Growth Guide",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Recent Analyzed Posts Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (language == "Hindi") "📝 हाल ही में जांची गई पोस्ट्स" else "📝 Recently Analyzed Posts",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = "${posts.size} posts",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        // Post Items List
        if (posts.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Insights,
                            contentDescription = null,
                            tint = InstaPink,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (language == "Hindi") "अभी कोई पोस्ट एनालाइज नहीं की गई" else "No posts analyzed yet",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (language == "Hindi") "'नई पोस्ट जोड़ें' पर टैप करके अपनी पहली पोस्ट का AI ऑडिट देखें!" else "Tap 'Analyze Post' to see engagement scores and hook critiques!",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                            modifier = Modifier.padding(horizontal = 16.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(posts, key = { it.id }) { post ->
                PostItemCard(
                    post = post,
                    language = language,
                    onClick = { selectedPostForDetail = post },
                    onDelete = { onDeletePost(post.id) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Detail / Audit Dialog
    selectedPostForDetail?.let { post ->
        PostDetailDialog(
            post = post,
            language = language,
            onDismiss = { selectedPostForDetail = null }
        )
    }
}

@Composable
fun ProfileStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Composable
fun PostItemCard(
    post: PostEntity,
    language: String,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                RoundedCornerShape(16.dp)
            )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Post Type Badge
                val (typeBg, typeColor) = when (post.postType) {
                    "Reel" -> Pair(InstaPink.copy(alpha = 0.15f), InstaPink)
                    "Carousel" -> Pair(InstaPurple.copy(alpha = 0.15f), InstaPurple)
                    "Single Photo" -> Pair(InstaCoral.copy(alpha = 0.15f), InstaCoral)
                    else -> Pair(MetricGreen.copy(alpha = 0.15f), MetricGreen)
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = typeBg
                ) {
                    Text(
                        text = post.postType,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = typeColor,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // AI Rating or Score badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (post.aiAuditScore != null) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (post.aiAuditScore >= 80) MetricGreen.copy(alpha = 0.15f) else MetricOrange.copy(alpha = 0.15f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = if (post.aiAuditScore >= 80) MetricGreen else MetricOrange,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${post.aiAuditScore}/100",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (post.aiAuditScore >= 80) MetricGreen else MetricOrange
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Caption preview
            Text(
                text = post.caption,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Normal
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Stats Pill Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = InstaPink, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = formatCount(post.likes), style = MaterialTheme.typography.labelSmall)

                    Spacer(modifier = Modifier.width(10.dp))

                    Icon(imageVector = Icons.Default.ModeComment, contentDescription = null, tint = InstaPurple, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = formatCount(post.comments), style = MaterialTheme.typography.labelSmall)

                    Spacer(modifier = Modifier.width(10.dp))

                    Icon(imageVector = Icons.Default.Bookmark, contentDescription = null, tint = MetricGreen, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = formatCount(post.saves), style = MaterialTheme.typography.labelSmall)
                }

                Text(
                    text = "ER: ${String.format("%.1f", post.engagementRate)}%",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = InstaPink
                    )
                )
            }
        }
    }
}

@Composable
fun PostDetailDialog(
    post: PostEntity,
    language: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (language == "Hindi") "🔍 पोस्ट AI ऑडिट विश्लेषण" else "🔍 Post AI Audit Details",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                if (post.aiAuditScore != null) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = InstaPink.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "${post.aiAuditScore}/100",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = InstaPink,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Viral rating banner
                item {
                    if (!post.aiViralRating.isNullOrBlank()) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = InstaPurple.copy(alpha = 0.12f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Viral Potential: ${post.aiViralRating}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = InstaPurple
                                ),
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }

                // Metrics summary
                item {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Metrics Overview:",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "• Engagement Rate: ${String.format("%.1f", post.engagementRate)}%", style = MaterialTheme.typography.bodySmall)
                            Text(text = "• Save-to-Reach Ratio: ${String.format("%.2f", post.saveToReachRatio)}%", style = MaterialTheme.typography.bodySmall)
                            Text(text = "• Total Reach: ${formatCount(post.reach)}", style = MaterialTheme.typography.bodySmall)
                            Text(text = "• Likes: ${post.likes} | Comments: ${post.comments} | Shares: ${post.shares} | Saves: ${post.saves}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                // Hook critique
                item {
                    if (!post.aiHookFeedback.isNullOrBlank()) {
                        Column {
                            Text(
                                text = if (language == "Hindi") "🎯 हुक और फर्स्ट 3 सेकेंड्स समीक्षा:" else "🎯 Hook & First 3 Seconds Critique:",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = InstaPink)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = post.aiHookFeedback ?: "",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                // Strengths
                item {
                    if (!post.aiStrengths.isNullOrBlank()) {
                        Column {
                            Text(
                                text = if (language == "Hindi") "✅ क्या अच्छा रहा (Strengths):" else "✅ What Worked Well:",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = MetricGreen)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = post.aiStrengths ?: "",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                // Improvements
                item {
                    if (!post.aiImprovements.isNullOrBlank()) {
                        Column {
                            Text(
                                text = if (language == "Hindi") "💡 अगले पोस्ट के लिए सुधार (Action Items):" else "💡 Improvements for Next Post:",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = MetricOrange)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = post.aiImprovements ?: "",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                // Hashtags critique
                item {
                    if (!post.aiHashtagAdvice.isNullOrBlank()) {
                        Column {
                            Text(
                                text = if (language == "Hindi") "#️⃣ हैशटैग सलाह:" else "#️⃣ Hashtag Advice:",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = InstaPurple)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = post.aiHashtagAdvice ?: "",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = InstaPink)
            ) {
                Text(text = if (language == "Hindi") "समझ गया" else "Got It")
            }
        }
    )
}

fun formatCount(count: Int): String {
    return when {
        count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
        count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
        else -> "$count"
    }
}

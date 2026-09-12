package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.PostAnalysisResult
import com.example.ui.theme.InstaCoral
import com.example.ui.theme.InstaGradient
import com.example.ui.theme.InstaPink
import com.example.ui.theme.InstaPurple
import com.example.ui.theme.MetricGreen
import com.example.ui.theme.MetricOrange

@Composable
fun PostAnalyzerScreen(
    language: String,
    isAnalyzing: Boolean,
    lastResult: PostAnalysisResult?,
    onAnalyzeAndSave: (
        postType: String,
        caption: String,
        hashtags: String,
        mediaUri: String?,
        likes: Int,
        comments: Int,
        shares: Int,
        saves: Int,
        reach: Int,
        impressions: Int
    ) -> Unit,
    onClearResult: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var selectedType by remember { mutableStateOf("Reel") }
    var captionText by remember {
        mutableStateOf("🚨 3 mistakes jo aapki Instagram reels reach kill kar rahi hain!\n\n1. Starting with 'Hey guys'\n2. Low lighting\n3. No call to action\n\nSave this reel aur agle video me implement karo! 👇")
    }
    var hashtagText by remember {
        mutableStateOf("#reeltips #instagramgrowth #creatorhacks #viralreels #growthmindset")
    }

    var likesText by remember { mutableStateOf("2450") }
    var commentsText by remember { mutableStateOf("310") }
    var sharesText by remember { mutableStateOf("680") }
    var savesText by remember { mutableStateOf("1420") }
    var reachText by remember { mutableStateOf("28500") }
    var impressionsText by remember { mutableStateOf("34000") }

    val likes = likesText.toIntOrNull() ?: 0
    val comments = commentsText.toIntOrNull() ?: 0
    val shares = sharesText.toIntOrNull() ?: 0
    val saves = savesText.toIntOrNull() ?: 0
    val reach = (reachText.toIntOrNull() ?: 1).coerceAtLeast(1)
    val impressions = (impressionsText.toIntOrNull() ?: reach).coerceAtLeast(reach)

    val totalInteractions = likes + comments + shares + saves
    val liveER = (totalInteractions.toFloat() / reach.toFloat()) * 100f
    val liveSaveRatio = (saves.toFloat() / reach.toFloat()) * 100f
    val liveShareRatio = (shares.toFloat() / (if (likes == 0) 1 else likes).toFloat()) * 100f

    val postTypes = listOf("Reel", "Carousel", "Single Photo", "Story")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Section Header
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Column {
                Text(
                    text = if (language == "Hindi") "🔍 पोस्ट एनालाइजर & AI ऑडिट" else "🔍 Post Analyzer & AI Audit",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = if (language == "Hindi") "अपनी पोस्ट का डेटा दर्ज करें और एल्गोरिदम के अनुसार ऑडिट स्कोर पाएं" else "Input your post metrics to get instant algorithmic health and AI suggestions",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        // Post Type Selector
        item {
            Text(
                text = if (language == "Hindi") "पोस्ट का प्रकार (Format):" else "Post Format:",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                postTypes.forEach { type ->
                    val isSelected = selectedType == type
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) InstaPink else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedType = type }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(vertical = 10.dp)
                        ) {
                            Text(
                                text = type,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // Live Engagement Meter
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                        RoundedCornerShape(16.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (language == "Hindi") "⚡ लाइव एंगेजमेंट स्कोर" else "⚡ Live Engagement Score",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        val erStatus = when {
                            liveER >= 10f -> Pair("🔥 Viral Level", MetricGreen)
                            liveER >= 5f -> Pair("⭐ High (Strong)", MetricGreen)
                            liveER >= 2.5f -> Pair("📈 Good Average", InstaCoral)
                            else -> Pair("⚠️ Below Average", Color(0xFFFF5252))
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = erStatus.second.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = erStatus.first,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = erStatus.second,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${String.format("%.1f", liveER)}%",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = InstaPink
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            LinearProgressIndicator(
                                progress = { (liveER / 15f).coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(CircleShape),
                                color = InstaPink,
                                trackColor = MaterialTheme.colorScheme.surface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (language == "Hindi") "एल्गोरिदम बेंचमार्क: 3-6% अच्छा | >10% वायरल" else "Benchmark: 3-6% Good | >10% Viral",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Secondary rates (Save ratio + Share ratio)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Save/Reach: ${String.format("%.2f", liveSaveRatio)}%",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (liveSaveRatio >= 2f) MetricGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Text(
                            text = "Share/Like: ${String.format("%.1f", liveShareRatio)}%",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = InstaPurple,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }

        // Caption Input Field
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (language == "Hindi") "कैप्शन (Caption):" else "Caption:",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = "${captionText.length} chars",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = captionText,
                    onValueChange = { captionText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_caption"),
                    minLines = 3,
                    maxLines = 6,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = InstaPink,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                // First Line / Hook Preview tip
                val firstLine = captionText.lines().firstOrNull() ?: ""
                if (firstLine.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Hook: \"${firstLine.take(60)}${if (firstLine.length > 60) "..." else ""}\"",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = InstaPink,
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }

        // Hashtags Field
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (language == "Hindi") "हैशटैग्स (Hashtags):" else "Hashtags:",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    val tagCount = hashtagText.split(" ", ",").filter { it.startsWith("#") }.size
                    Text(
                        text = "$tagCount tags",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (tagCount in 3..8) MetricGreen else InstaCoral
                        )
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = hashtagText,
                    onValueChange = { hashtagText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_hashtags"),
                    singleLine = false,
                    maxLines = 3,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = InstaPink,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
            }
        }

        // Metrics Inputs (Likes, Comments, Shares, Saves, Reach, Impressions)
        item {
            Text(
                text = if (language == "Hindi") "📊 मेट्रिक्स (Post Metrics):" else "📊 Post Metrics:",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Row 1: Likes, Comments, Shares
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = likesText,
                    onValueChange = { likesText = it.filter { c -> c.isDigit() } },
                    label = { Text("Likes", fontSize = 11.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_likes"),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = commentsText,
                    onValueChange = { commentsText = it.filter { c -> c.isDigit() } },
                    label = { Text("Comments", fontSize = 11.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_comments"),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = sharesText,
                    onValueChange = { sharesText = it.filter { c -> c.isDigit() } },
                    label = { Text("Shares", fontSize = 11.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_shares"),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Row 2: Saves, Reach, Impressions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = savesText,
                    onValueChange = { savesText = it.filter { c -> c.isDigit() } },
                    label = { Text("Saves", fontSize = 11.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_saves"),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = reachText,
                    onValueChange = { reachText = it.filter { c -> c.isDigit() } },
                    label = { Text("Reach", fontSize = 11.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_reach"),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = impressionsText,
                    onValueChange = { impressionsText = it.filter { c -> c.isDigit() } },
                    label = { Text("Impressions", fontSize = 11.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_impressions"),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        // Action Buttons: Run AI Deep Audit
        item {
            Button(
                onClick = {
                    onAnalyzeAndSave(
                        selectedType,
                        captionText,
                        hashtagText,
                        null,
                        likes,
                        comments,
                        shares,
                        saves,
                        reach,
                        impressions
                    )
                },
                enabled = !isAnalyzing,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = InstaPink),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("button_run_audit")
            ) {
                if (isAnalyzing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (language == "Hindi") "एआई ऑडिट चल रहा है..." else "Running AI Deep Audit...",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == "Hindi") "🤖 AI डीप ऑडिट शुरू करें" else "🤖 Run AI Deep Audit",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // AI Audit Result Section
        if (lastResult != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            2.dp,
                            InstaPink.copy(alpha = 0.6f),
                            RoundedCornerShape(20.dp)
                        )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(InstaGradient),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (language == "Hindi") "AI ऑडिट रिपोर्ट" else "AI Audit Report",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = lastResult.viralRating,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = InstaPink,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }

                            // Score badge
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (lastResult.score >= 80) MetricGreen.copy(alpha = 0.2f) else MetricOrange.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "${lastResult.score}/100",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (lastResult.score >= 80) MetricGreen else MetricOrange
                                    ),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Hook Feedback
                        AuditCardSection(
                            title = if (language == "Hindi") "🎯 हुक और फर्स्ट लाइन (Hook Analysis):" else "🎯 Hook Analysis:",
                            content = lastResult.hookFeedback,
                            color = InstaPink
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Strengths
                        AuditCardSection(
                            title = if (language == "Hindi") "✅ क्या अच्छा रहा (Strengths):" else "✅ What Worked Well:",
                            content = lastResult.strengths,
                            color = MetricGreen
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Improvements
                        AuditCardSection(
                            title = if (language == "Hindi") "💡 अगले पोस्ट में क्या सुधारें (Actionable Fixes):" else "💡 Actionable Improvements:",
                            content = lastResult.improvements,
                            color = MetricOrange
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Hashtag Advice
                        AuditCardSection(
                            title = if (language == "Hindi") "#️⃣ हैशटैग समीक्षा:" else "#️⃣ Hashtag Critique:",
                            content = lastResult.hashtagAdvice,
                            color = InstaPurple
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            OutlinedButton(
                                onClick = onClearResult,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(text = if (language == "Hindi") "हटाएं" else "Dismiss", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AuditCardSection(title: String, content: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 18.sp
                )
            )
        }
    }
}

package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.BioAuditResult
import com.example.data.model.ProfileEntity
import com.example.ui.theme.InstaCoral
import com.example.ui.theme.InstaGradient
import com.example.ui.theme.InstaPink
import com.example.ui.theme.InstaPurple
import com.example.ui.theme.MetricGreen
import com.example.ui.theme.MetricOrange

@Composable
fun PageOptimizerScreen(
    profile: ProfileEntity?,
    language: String,
    bioAuditResult: BioAuditResult?,
    isAuditingBio: Boolean,
    onAuditBio: (username: String, niche: String, bio: String) -> Unit,
    onApplyNewBio: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Column {
                Text(
                    text = if (language == "Hindi") "🚀 इंस्टाग्राम पेज ग्रोथ गाइड" else "🚀 Instagram Page Growth Guide",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = if (language == "Hindi") "अपने पेज को प्रोफेशनल बनाएं, फॉलोअर कन्वर्जन बढ़ाएं और एल्गोरिदम हैक करें" else "Optimize your bio, profile conversion, and content strategy to accelerate reach",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        // Bio Audit & Optimizer Section
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
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (language == "Hindi") "प्रोफाइल बायो ऑप्टिमाइजर" else "Profile Bio Optimizer",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = profile?.username ?: "@my_page",
                                    style = MaterialTheme.typography.labelSmall.copy(color = InstaPink)
                                )
                            }
                        }

                        // Score
                        val score = bioAuditResult?.score ?: profile?.profileHealthScore ?: 80
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (score >= 80) MetricGreen.copy(alpha = 0.15f) else MetricOrange.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "Score: $score/100",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (score >= 80) MetricGreen else MetricOrange
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Current Bio Display
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (language == "Hindi") "वर्तमान बायो (Current Bio):" else "Current Bio:",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = profile?.bio ?: "No bio set yet.",
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Audit Button
                    Button(
                        onClick = {
                            profile?.let {
                                onAuditBio(it.username, it.niche, it.bio)
                            }
                        },
                        enabled = !isAuditingBio,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = InstaPurple),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("button_audit_bio")
                    ) {
                        if (isAuditingBio) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Auditing Bio...")
                        } else {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == "Hindi") "✨ बायो का AI ऑडिट और री-राइट करें" else "✨ Audit & Rewrite Bio with AI",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Audit Result
                    if (bioAuditResult != null) {
                        Spacer(modifier = Modifier.height(14.dp))

                        // Critique
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = InstaPurple.copy(alpha = 0.08f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = null,
                                    tint = InstaPink,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = bioAuditResult.critique,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (language == "Hindi") "💡 AI द्वारा सुझाए गए 3 हाई-कन्वर्टिंग बायो:" else "💡 3 High-Converting Suggested Bios:",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        bioAuditResult.suggestedBios.forEachIndexed { index, suggestedBio ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                        RoundedCornerShape(12.dp)
                                    )
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Option ${index + 1}:",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = InstaPink,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = suggestedBio,
                                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        OutlinedButton(
                                            onClick = {
                                                clipboardManager.setText(AnnotatedString(suggestedBio))
                                                Toast.makeText(context, "Bio copied to clipboard!", Toast.LENGTH_SHORT).show()
                                            },
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Copy", fontSize = 11.sp)
                                        }

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Button(
                                            onClick = {
                                                onApplyNewBio(suggestedBio)
                                                Toast.makeText(context, "Bio applied to profile!", Toast.LENGTH_SHORT).show()
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = InstaPink)
                                        ) {
                                            Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(if (language == "Hindi") "बायो में लगाएं" else "Apply", fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4 Content Pillars Strategy Card
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (language == "Hindi") "🎯 4 कंटेंट पिलर्स फॉर्मूला (4-Pillar Content Mix)" else "🎯 4 Content Pillars Growth Strategy",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = if (language == "Hindi") "सफल पेजों का पोस्टिंग रेशियो जो फॉलोअर्स और सेल्स दोनों देता है:" else "The proven content balance for steady follower growth & trust:",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PillarRow(
                        percent = "40%",
                        title = if (language == "Hindi") "ज्ञान व सीख (Educate / Carousels)" else "Educate & Problem Solve",
                        desc = if (language == "Hindi") "Step-by-step guides, tips, cheatsheets (उच्चतम सेव्स मिलते हैं)" else "Cheatsheets, actionable tips, carousels (Drives maximum saves)",
                        color = InstaPink
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PillarRow(
                        percent = "30%",
                        title = if (language == "Hindi") "मनोरंजन / रिलेटिबल (Entertain / Relatable Reels)" else "Entertain & Relatable Reels",
                        desc = if (language == "Hindi") "Trending audio, POV reels, creator memes (नए लोगों तक रीच दिलाता है)" else "Trending sounds, POV moments, humor (Explodes non-follower reach)",
                        color = InstaPurple
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PillarRow(
                        percent = "20%",
                        title = if (language == "Hindi") "विश्वास / अथॉरिटी (Inspire & Social Proof)" else "Inspire & Authority",
                        desc = if (language == "Hindi") "Case studies, transformations, BTS, journey (फॉलोअर को लॉयल बनाता है)" else "Transformations, BTS, personal stories (Builds deep trust)",
                        color = MetricGreen
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PillarRow(
                        percent = "10%",
                        title = if (language == "Hindi") "कम्युनिटी व प्रमोशन (Community & Offers)" else "Community & Direct CTA",
                        desc = if (language == "Hindi") "Stories Q&A, DMs, product/service links, collabs" else "Stories Q&A, broadcast channels, clear offer prompts",
                        color = InstaCoral
                    )
                }
            }
        }

        // Viral Reel Framework (0-3-15 Formula)
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.TrendingUp, contentDescription = null, tint = MetricGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == "Hindi") "🔥 2026 वायरल रील फॉर्मूला (0-3-15 Rule)" else "🔥 Viral Reel Blueprint (0-3-15 Rule)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    RuleStepItem(
                        step = "0-3s",
                        title = if (language == "Hindi") "विजुअल + टेक्स्ट हुक (Visual Hook)" else "Visual + Text Hook",
                        desc = if (language == "Hindi") "'Hey guys' कभी न कहें! स्क्रीन पर बड़ा बोल्ड टेक्स्ट, तेज मूव्मेंट या सवाल रखें." else "Never say 'Hey guys'. High contrast bold text, quick motion, or controversial question."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    RuleStepItem(
                        step = "3-15s",
                        title = if (language == "Hindi") "स्पीड डिलीवरी (Value Density)" else "Fast Value Delivery",
                        desc = if (language == "Hindi") "हर 2-3 सेकेंड में B-roll कट, जूम या टेक्स्ट चेंज ताकि वॉच टाइम 80%+ रहे." else "Cut pauses, use B-rolls/zooms every 2.5s to maintain 80%+ retention rate."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    RuleStepItem(
                        step = "15-20s",
                        title = if (language == "Hindi") "सीटीए + लूप (Call To Action & Loop)" else "Looping & Smart CTA",
                        desc = if (language == "Hindi") "लास्ट लाइन को फर्स्ट लाइन से जोड़ें (Seamless Loop) + 'Comment LINK' बोलें." else "End sentence loops back into opening line + prompt 'Comment [KEYWORD] for link'."
                    )
                }
            }
        }

        // Best Time to Post Table
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = InstaCoral)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == "Hindi") "⏰ पोस्ट करने का सबसे सही समय (Best Posting Times)" else "⏰ Best Times to Post (Peak Engagement)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    PostingTimeRow(
                        slot = "🌅 Morning (सुबह)",
                        time = "8:30 AM – 10:00 AM",
                        desc = if (language == "Hindi") "कम्यूट टाइम - स्टोरीज और इंस्पायरिंग कोट्स के लिए बेस्ट" else "Commute time — Great for Stories & quick reels"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PostingTimeRow(
                        slot = "☀️ Afternoon (दोपहर)",
                        time = "1:00 PM – 2:30 PM",
                        desc = if (language == "Hindi") "लंच ब्रेक - एजुकेशनल कैरोसेल और टिप्स के लिए परफेक्ट" else "Lunch break — Ideal for carousels and saving guides"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PostingTimeRow(
                        slot = "🌙 Evening (शाम - Peak)",
                        time = "6:30 PM – 9:30 PM",
                        desc = if (language == "Hindi") "उच्चतम सक्रियता (Highest Velocity) - रील्स के लिए सबसे असरदार" else "Highest daily active users — Prime window for Reels"
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun PillarRow(percent: String, title: String, desc: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = color.copy(alpha = 0.15f)
            ) {
                Text(
                    text = percent,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = color
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}

@Composable
fun RuleStepItem(step: String, title: String, desc: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = InstaPink.copy(alpha = 0.15f)
            ) {
                Text(
                    text = step,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = InstaPink
                    ),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                )
            }
        }
    }
}

@Composable
fun PostingTimeRow(slot: String, time: String, desc: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = slot,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = InstaCoral.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = time,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = InstaCoral
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            )
        }
    }
}

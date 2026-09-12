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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.InstaCoral
import com.example.ui.theme.InstaPink
import com.example.ui.theme.InstaPurple
import com.example.ui.theme.MetricGreen

@Composable
fun CaptionStudioScreen(
    language: String,
    generatedCaptions: List<String>,
    isGenerating: Boolean,
    onGenerateCaptions: (topic: String, niche: String, tone: String, lang: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    var topicText by remember { mutableStateOf("5 Best AI tools that save 10 hours a week") }
    var selectedNiche by remember { mutableStateOf("Tech & AI") }
    var selectedTone by remember { mutableStateOf("Viral Hook 🔥") }

    val tones = listOf("Viral Hook 🔥", "Educational 💡", "Storytelling 📖", "Punchy & Direct ⚡")
    val niches = listOf("Tech & AI", "Fitness & Health", "Fashion & Lifestyle", "Comedy & Reels", "Business & Finance", "Photography")

    val hashtagVault = remember {
        mapOf(
            "Tech & AI" to listOf("#aitools", "#techtips", "#reelsindia", "#techcreator", "#chatgpt", "#aiinnovation", "#contentcreation", "#viraltech"),
            "Fitness & Health" to listOf("#fitnessindia", "#workoutmotivation", "#fatlosstips", "#gymreels", "#healthyliving", "#fitnesstips", "#dailyworkout"),
            "Fashion & Lifestyle" to listOf("#fashioninspo", "#outfitideas", "#stylehacks", "#budgetfashion", "#ootdindia", "#streetstyle", "#lifestylecreator"),
            "Comedy & Reels" to listOf("#funnyreels", "#relatablecomedy", "#comedyindia", "#desimemes", "#trendingaudio", "#reelsindia", "#viralreels"),
            "Business & Finance" to listOf("#moneytips", "#personalfinance", "#entrepreneurship", "#investingindia", "#stockmarkettips", "#startupgrowth"),
            "Photography" to listOf("#mobilephotography", "#photoediting", "#lightroompresets", "#camerahacks", "#photographytips", "#cinematicreels")
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Column {
                Text(
                    text = if (language == "Hindi") "✍️ AI कैप्शन & हैशटैग स्टूडियो" else "✍️ AI Caption & Hashtag Studio",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = if (language == "Hindi") "वायरल हुक्स, सीटीए और नीचे-स्पेसिफिक हैशटैग्स के साथ रेडी-टू-पोस्ट कैप्शन बनाएं" else "Generate high-retention captions with scroll-stopping hooks & curated hashtags",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        // Generator Card
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
                    Text(
                        text = if (language == "Hindi") "पोस्ट का विषय / आइडिया:" else "Post Topic or Rough Idea:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = topicText,
                        onValueChange = { topicText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_topic"),
                        placeholder = { Text("e.g. 3 secret camera hacks for iPhone") },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InstaPink,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Niche Picker
                    Text(
                        text = if (language == "Hindi") "निश (Category):" else "Category / Niche:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(niches) { niche ->
                            val isSelected = selectedNiche == niche
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) InstaPink else MaterialTheme.colorScheme.surface,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { selectedNiche = niche }
                            ) {
                                Text(
                                    text = niche,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Tone Picker
                    Text(
                        text = if (language == "Hindi") "टोन (Tone):" else "Caption Tone:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        tones.forEach { tone ->
                            val isSelected = selectedTone == tone
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) InstaPurple else MaterialTheme.colorScheme.surface,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { selectedTone = tone }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                ) {
                                    Text(
                                        text = tone,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 10.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                        ),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Generate Button
                    Button(
                        onClick = {
                            onGenerateCaptions(topicText, selectedNiche, selectedTone, language)
                        },
                        enabled = !isGenerating && topicText.isNotBlank(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = InstaPink),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("button_generate_captions")
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (language == "Hindi") "कैप्शन लिख रहे हैं..." else "Generating Captions...")
                        } else {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == "Hindi") "✨ 3 वायरल कैप्शन जनरेट करें" else "✨ Generate 3 Viral Captions",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Generated Captions List
        if (generatedCaptions.isNotEmpty()) {
            item {
                Text(
                    text = if (language == "Hindi") "📋 जनरेट किए गए कैप्शन:" else "📋 Generated Caption Options:",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            items(generatedCaptions) { caption ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            RoundedCornerShape(16.dp)
                        )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = caption,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                lineHeight = 20.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(caption))
                                    Toast.makeText(context, "Caption copied to clipboard!", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = InstaPink)
                            ) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (language == "Hindi") "कॉपी करें" else "Copy Caption", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // Curated Hashtag Vault by Niche
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Tag, contentDescription = null, tint = InstaPurple)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == "Hindi") "🏷️ निश हैशटैग वॉल्ट" else "🏷️ Niche Hashtag Vault",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        val tags = hashtagVault[selectedNiche] ?: emptyList()
                        OutlinedButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(tags.joinToString(" ")))
                                Toast.makeText(context, "Hashtags copied!", Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Copy All", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Tags for: $selectedNiche",
                        style = MaterialTheme.typography.labelSmall.copy(color = InstaPink, fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val tags = hashtagVault[selectedNiche] ?: emptyList()
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = tags.joinToString("  "),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.primary,
                                lineHeight = 20.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

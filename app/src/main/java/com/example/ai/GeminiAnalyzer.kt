package com.example.ai

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class PostAnalysisResult(
    val score: Int,
    val hookFeedback: String,
    val strengths: String,
    val improvements: String,
    val hashtagAdvice: String,
    val viralRating: String
)

data class BioAuditResult(
    val score: Int,
    val critique: String,
    val suggestedBios: List<String>,
    val tips: List<String>
)

object GeminiAnalyzer {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private const val MODEL = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL:generateContent"

    suspend fun analyzePost(
        postType: String,
        caption: String,
        hashtags: String,
        likes: Int,
        comments: Int,
        shares: Int,
        saves: Int,
        reach: Int,
        niche: String = "Creator"
    ): PostAnalysisResult = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (_: Exception) {
            ""
        }

        // Calculate actual engagement metrics
        val effectiveReach = if (reach <= 0) 1 else reach
        val totalInteractions = likes + comments + shares + saves
        val er = (totalInteractions.toFloat() / effectiveReach.toFloat()) * 100f
        val saveRatio = (saves.toFloat() / effectiveReach.toFloat()) * 100f
        val shareRatio = (shares.toFloat() / effectiveReach.toFloat()) * 100f

        // Try AI generation if API key is provided and valid
        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val prompt = """
                    You are an elite Instagram growth expert and algorithm strategist.
                    Analyze this Instagram post for a creator in the '$niche' niche:
                    
                    - Format: $postType
                    - Caption: "$caption"
                    - Hashtags: "$hashtags"
                    - Metrics: Likes: $likes, Comments: $comments, Shares: $shares, Saves: $saves, Reach: $reach
                    - Calculated Engagement Rate: ${String.format("%.1f", er)}%
                    - Save-to-Reach Ratio: ${String.format("%.2f", saveRatio)}%
                    
                    Provide an honest, expert audit in a helpful mix of English and Hindi (Hinglish) so it's super actionable.
                    Output STRICT valid JSON with these keys:
                    {
                      "score": (integer 0-100),
                      "hookFeedback": (string critique of the first line / hook),
                      "strengths": (string highlighting best performing elements),
                      "improvements": (string with 2-3 specific actionable fixes to boost viral reach),
                      "hashtagAdvice": (string on tag relevance, volume mix and improvements),
                      "viralRating": (string: "Needs Optimization ⚠️" or "Moderate 📈" or "High Performer ⭐" or "Viral Performer 🔥")
                    }
                """.trimIndent()

                val responseText = callGeminiApi(prompt, apiKey)
                val parsed = parseJsonAnalysis(responseText)
                if (parsed != null) {
                    return@withContext parsed
                }
            } catch (e: Exception) {
                // Fall back to rule-based engine
            }
        }

        // Rule-based heuristic analysis
        generateHeuristicPostAnalysis(postType, caption, hashtags, likes, comments, shares, saves, er, saveRatio, shareRatio)
    }

    suspend fun auditBio(
        username: String,
        niche: String,
        currentBio: String
    ): BioAuditResult = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (_: Exception) {
            ""
        }

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val prompt = """
                    You are an Instagram profile optimization coach.
                    Audit this Instagram profile bio:
                    - Handle: $username
                    - Niche: $niche
                    - Current Bio: "$currentBio"
                    
                    Analyze:
                    1. Value Proposition (Is it clear who this page helps?)
                    2. Social Proof / Credibility
                    3. Call to Action (CTA)
                    4. Formatting & Readability
                    
                    Return strict JSON:
                    {
                      "score": (integer 50-100),
                      "critique": (string summary in clear English/Hinglish),
                      "suggestedBios": [3 string variations with emojis & linebreaks],
                      "tips": [3 short bullet tips]
                    }
                """.trimIndent()

                val responseText = callGeminiApi(prompt, apiKey)
                val jsonStr = extractJsonString(responseText)
                if (jsonStr.isNotBlank()) {
                    val root = JSONObject(jsonStr)
                    val score = root.optInt("score", 78)
                    val critique = root.optString("critique", "Clear start, but needs stronger CTA.")
                    val suggestedBios = mutableListOf<String>()
                    val biosArr = root.optJSONArray("suggestedBios")
                    if (biosArr != null) {
                        for (i in 0 until biosArr.length()) {
                            suggestedBios.add(biosArr.getString(i))
                        }
                    }
                    val tips = mutableListOf<String>()
                    val tipsArr = root.optJSONArray("tips")
                    if (tipsArr != null) {
                        for (i in 0 until tipsArr.length()) {
                            tips.add(tipsArr.getString(i))
                        }
                    }
                    if (suggestedBios.isNotEmpty()) {
                        return@withContext BioAuditResult(score, critique, suggestedBios, tips)
                    }
                }
            } catch (_: Exception) {
                // fallback
            }
        }

        // High quality heuristic bio analysis
        generateHeuristicBioAudit(username, niche, currentBio)
    }

    suspend fun generateCaptionsAndHooks(
        topic: String,
        niche: String,
        tone: String,
        language: String
    ): List<String> = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (_: Exception) {
            ""
        }

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val prompt = """
                    You are a viral Instagram copywriter. Write 3 high-engagement Instagram caption variations for:
                    - Topic: $topic
                    - Niche: $niche
                    - Tone: $tone
                    - Language: $language (Hindi / Hinglish or English)
                    
                    Each caption MUST include:
                    1. Magnetic stop-the-scroll Hook in line 1
                    2. Short snackable body with linebreaks
                    3. Engagement prompt / CTA (e.g. 'Comment YES', 'Save for later', 'Share with a friend')
                    4. 5 high-converting relevant hashtags
                    
                    Return strict JSON:
                    {
                      "captions": ["caption 1 text", "caption 2 text", "caption 3 text"]
                    }
                """.trimIndent()

                val responseText = callGeminiApi(prompt, apiKey)
                val jsonStr = extractJsonString(responseText)
                if (jsonStr.isNotBlank()) {
                    val root = JSONObject(jsonStr)
                    val arr = root.optJSONArray("captions")
                    if (arr != null && arr.length() > 0) {
                        val list = mutableListOf<String>()
                        for (i in 0 until arr.length()) {
                            list.add(arr.getString(i))
                        }
                        return@withContext list
                    }
                }
            } catch (_: Exception) {
                // fallback
            }
        }

        // Built-in smart templates
        generateTemplateCaptions(topic, niche, tone, language)
    }

    private fun callGeminiApi(prompt: String, apiKey: String): String {
        val url = "$BASE_URL?key=$apiKey"
        val requestJson = JSONObject().apply {
            val contents = JSONArray().apply {
                put(JSONObject().apply {
                    val parts = JSONArray().apply {
                        put(JSONObject().put("text", prompt))
                    }
                    put("parts", parts)
                })
            }
            put("contents", contents)
            put("generationConfig", JSONObject().apply {
                put("temperature", 0.7)
                put("topP", 0.95)
            })
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = requestJson.toString().toRequestBody(mediaType)
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw Exception("HTTP error code: ${response.code}")
            }
            val bodyString = response.body?.string() ?: ""
            val json = JSONObject(bodyString)
            val candidates = json.optJSONArray("candidates")
            val candidate = candidates?.optJSONObject(0)
            val content = candidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            return parts?.optJSONObject(0)?.optString("text") ?: ""
        }
    }

    private fun extractJsonString(raw: String): String {
        var clean = raw.trim()
        if (clean.startsWith("```json")) {
            clean = clean.removePrefix("```json")
        } else if (clean.startsWith("```")) {
            clean = clean.removePrefix("```")
        }
        if (clean.endsWith("```")) {
            clean = clean.removeSuffix("```")
        }
        clean = clean.trim()
        val firstBrace = clean.indexOf('{')
        val lastBrace = clean.lastIndexOf('}')
        if (firstBrace != -1 && lastBrace != -1 && lastBrace > firstBrace) {
            return clean.substring(firstBrace, lastBrace + 1)
        }
        return clean
    }

    private fun parseJsonAnalysis(responseText: String): PostAnalysisResult? {
        return try {
            val jsonStr = extractJsonString(responseText)
            val obj = JSONObject(jsonStr)
            PostAnalysisResult(
                score = obj.optInt("score", 75),
                hookFeedback = obj.optString("hookFeedback", "Effective opening line."),
                strengths = obj.optString("strengths", "Strong engagement signals and saves."),
                improvements = obj.optString("improvements", "Test shorter captions and clearer CTA."),
                hashtagAdvice = obj.optString("hashtagAdvice", "Keep 5-8 niche hashtags for discovery."),
                viralRating = obj.optString("viralRating", "High Performer ⭐")
            )
        } catch (_: Exception) {
            null
        }
    }

    private fun generateHeuristicPostAnalysis(
        postType: String,
        caption: String,
        hashtags: String,
        likes: Int,
        comments: Int,
        shares: Int,
        saves: Int,
        er: Float,
        saveRatio: Float,
        shareRatio: Float
    ): PostAnalysisResult {
        var calculatedScore = 60

        // Benchmark metrics scoring
        if (er >= 12f) calculatedScore += 18
        else if (er >= 7f) calculatedScore += 12
        else if (er >= 3f) calculatedScore += 6

        if (saveRatio >= 3f) calculatedScore += 12
        else if (saveRatio >= 1.5f) calculatedScore += 6

        if (shareRatio >= 2f) calculatedScore += 8

        // Caption length and hook check
        val lines = caption.lines().filter { it.isNotBlank() }
        val firstLine = lines.firstOrNull() ?: ""
        val hasQuestion = firstLine.contains("?") || firstLine.contains("kyu") || firstLine.contains("how") || firstLine.contains("kaise")
        val hasNumbers = firstLine.any { it.isDigit() }
        val hasEmoji = firstLine.any { Character.isSurrogate(it) }

        val hookFeedback = when {
            firstLine.length < 10 -> "⚠️ Hook bahut chhota hai. First line me scroller ko curiosity ya immediate value offer karo."
            hasNumbers && (hasQuestion || hasEmoji) -> "🔥 Zabardast hook! Numbers + curiosity formula scrollers ko instant stop karta hai."
            hasQuestion -> "⭐ Good curiosity hook. Question puchne se audience caption padhne ke liye rukti hai."
            firstLine.length > 90 -> "⚠️ First line thodi lambi hai. Instagram sirf pehle 2 lines preview karta hai, isliye punchy rakhein."
            else -> "👍 Hook accha hai, but isme thoda intrigue ya numbers (e.g. 'Top 3...', 'Secret mistake...') add karke aur behtar bana sakte hain."
        }

        val tagCount = hashtags.split(" ", ",").filter { it.startsWith("#") }.size
        val hashtagAdvice = when {
            tagCount == 0 -> "⚠️ Koi hashtags nahi hain! 5 se 8 targeted niche hashtags daalne se Explore page reach 20-30% badh sakti hai."
            tagCount in 3..8 -> "✅ Perfect hashtag range (3-8 tags). Niche-specific tags algorithm ko correct target audience tak pahunchate hain."
            tagCount > 20 -> "⚠️ Bahut zyada generic hashtags hain. Too many broad tags make it look spammy; stick to 5-8 high relevance tags."
            else -> "💡 Hashtags theek hain. Niche keywords aur trending topics ka balance maintain karein."
        }

        val strengths = when {
            postType == "Reel" && saveRatio >= 2.5f -> "Reel ka save ratio bohot high hai (${String.format("%.1f", saveRatio)}%). Algorithm high saves wali reels ko loop & viral promote karta hai."
            postType == "Carousel" -> "Carousel format swipe time badhata hai. Audience dwell time zyada hone se feed priority milti hai."
            comments > (likes * 0.1) -> "Comment ratio bohot accha hai! High comments signal active community engagement."
            else -> "Clean format aur genuine engagement. Clear message audience tak accurately deliver ho raha hai."
        }

        val improvements = when {
            !caption.lowercase().contains("comment") && !caption.lowercase().contains("save") && !caption.lowercase().contains("share") ->
                "1. End me ek clear Call To Action (CTA) add karein jaise 'Save this reel' ya 'Comment your opinion'.\n2. First 3 seconds me visual change ya text overlay rakhein."
            saveRatio < 1.0f ->
                "1. Value-dense content banayein jise log dobara dekhne ke liye SAVE karein (cheatsheets, step-by-step guides).\n2. Caption me bullet points aur clean spacing use karein."
            else ->
                "1. Peak audience active hours (shaam 6:30 PM - 9:00 PM) par post karke first-hour velocity badhayein.\n2. Story me post share karke interactive poll lagayein."
        }

        calculatedScore = calculatedScore.coerceIn(45, 98)

        val viralRating = when {
            calculatedScore >= 85 -> "Viral Performer 🔥"
            calculatedScore >= 72 -> "High Performer ⭐"
            calculatedScore >= 60 -> "Moderate 📈"
            else -> "Needs Optimization ⚠️"
        }

        return PostAnalysisResult(
            score = calculatedScore,
            hookFeedback = hookFeedback,
            strengths = strengths,
            improvements = improvements,
            hashtagAdvice = hashtagAdvice,
            viralRating = viralRating
        )
    }

    private fun generateHeuristicBioAudit(username: String, niche: String, currentBio: String): BioAuditResult {
        val hasCta = currentBio.contains("👇") || currentBio.contains("link") || currentBio.contains("click") || currentBio.contains("dm")
        val lines = currentBio.lines().filter { it.isNotBlank() }
        val score = if (hasCta && lines.size in 3..5) 85 else if (lines.size >= 2) 72 else 58

        val critique = "Aapka bio ${if (hasCta) "me clear Call-To-Action hai" else "me strong Call-To-Action missing hai"}. Instagram bio 150 characters me audience ko batana chahiye: Aap kaun hain, unhe kya milega, aur unhe kyu follow karna chahiye."

        val suggested1 = """
            ⚡ Helping you grow in $niche
            📈 Proven strategies, tips & daily reels
            👇 Click the link to grab your free guide
        """.trimIndent()

        val suggested2 = """
            🔥 Master $niche with zero fluff
            💡 Daily hacks & insights for creators
            🚀 Join 15K+ smart creators below 👇
        """.trimIndent()

        val suggested3 = """
            ✨ Your daily dose of $niche inspiration
            🎯 Actionable tutorials & creative breakdowns
            📩 DM for collaborations | Link below 👇
        """.trimIndent()

        val tips = listOf(
            "Name field me niche keyword add karein (e.g. '$username | $niche Tips') taaki search me page top par aaye.",
            "Bio ko 3 se 4 bullet lines me rakhein with relevant emojis for easy scanning.",
            "Hamesha ek specific Call To Action (CTA) ke sath link pointing rakhein (👇 Link below)."
        )

        return BioAuditResult(score, critique, listOf(suggested1, suggested2, suggested3), tips)
    }

    private fun generateTemplateCaptions(topic: String, niche: String, tone: String, language: String): List<String> {
        val isHindi = language.contains("Hindi", ignoreCase = true) || language.contains("Hinglish", ignoreCase = true)

        if (isHindi) {
            return listOf(
                """
                    🚨 Yeh secret trick $niche me 99% log miss kar dete hain! 🤫
                    
                    Agar aap $topic ko next level par le jana chahte hain, toh yeh 3 steps follow karein:
                    
                    1️⃣ Consistency over perfection
                    2️⃣ Audience ke main pain point ko solve karein
                    3️⃣ Har post me ek clear action plan dein
                    
                    👉 Save this post for later aur apne creator doston ke sath share karein!
                    
                    #$niche #$topic #instagramgrowth #creatortips #viralreels
                """.trimIndent(),
                """
                    Kya aap bhi $topic ko lekar confuse rehte hain? 🤔
                    
                    Tension mat lo! Aaj hum isko bilkul simple shabdon me break down karenge:
                    
                    ✨ Step 1: Foundation strong karo
                    ✨ Step 2: Sahi tools aur workflow choose karo
                    ✨ Step 3: Roz 1% improve karo
                    
                    Aapka sabse bada challenge kya hai? Niche comment me batayein! 👇
                    
                    #${niche.replace(" ", "")} #trendinghindi #socialmediatips #reelsindia #dailygrowth
                """.trimIndent(),
                """
                    Stop scrolling! 🛑 Yeh advice aapka pura game change kar sakti hai.
                    
                    $topic par grow karne ka sabse fast formula:
                    
                    1. First 3 seconds me strong hook dein
                    2. Clear aur crisp information deliver karein
                    3. Community ke sath regularly interact karein
                    
                    📌 Tag someone who needs to see this!
                    
                    #${niche.replace(" ", "")}growth #creatorhacks #explorepage #viraltips #instatips
                """.trimIndent()
            )
        } else {
            return listOf(
                """
                    🚨 The #1 mistake people make with $topic (and how to fix it):
                    
                    Most people overcomplicate it. Here is the streamlined 3-step formula:
                    
                    1. Focus on high-retention hooks
                    2. Deliver immediate actionable value
                    3. Always end with a strong CTA
                    
                    Save this so you don't lose it! 📌
                    
                    #$niche #$topic #instagramtips #contentcreation #growthhacks
                """.trimIndent(),
                """
                    If I had to start over in $niche today, this is exactly what I would do:
                    
                    💡 Step 1: Master the fundamentals of $topic
                    💡 Step 2: Post consistently 4-5x a week
                    💡 Step 3: Analyze top-performing metrics weekly
                    
                    Which step are you currently working on? Drop a comment below! 👇
                    
                    #socialmediastrategy #pagegrowth #creatorlifestyle #growthmindset
                """.trimIndent(),
                """
                    Stop doing this in 2026! 🛑
                    
                    Here is the exact framework to level up your $topic game:
                    
                    • High contrast visual hooks
                    • Clear problem-solving captions
                    • Smart bookmarkable carousels
                    
                    Share this with a fellow creator who needs this reminder! 🚀
                    
                    #${niche.replace(" ", "")} #reeltips #algorithminsights #viralcontent
                """.trimIndent()
            )
        }
    }
}

package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.PostEntity
import com.example.data.model.ProfileEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [PostEntity::class, ProfileEntity::class], version = 1, exportSchema = false)
abstract class InstaDatabase : RoomDatabase() {
    abstract fun instaDao(): InstaDao

    companion object {
        @Volatile
        private var INSTANCE: InstaDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): InstaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InstaDatabase::class.java,
                    "insta_grow_db"
                ).addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.instaDao())
                    }
                }
            }

            suspend fun populateInitialData(dao: InstaDao) {
                // Initial profile
                dao.saveProfile(
                    ProfileEntity(
                        id = 1,
                        username = "@creative_tech_hindi",
                        displayName = "Tech & Reels Guru",
                        niche = "Technology & AI",
                        followers = 15800,
                        following = 412,
                        totalPosts = 56,
                        bio = "⚡ Daily AI tools, viral editing tricks & gadget reviews\n🔥 100K+ community across platforms\n🎁 Free Creator Kit in link 👇",
                        profileHealthScore = 84,
                        bioAuditFeedback = "Strong value proposition and clear CTA. Recommendation: Add a targeted keyword in your name field (e.g. 'Tech & Reels Guru | AI Hacks') to boost discovery in Instagram search."
                    )
                )

                val now = System.currentTimeMillis()
                val oneDay = 86400000L

                // Sample Post 1: High performing viral reel
                dao.insertPost(
                    PostEntity(
                        postType = "Reel",
                        caption = "🚨 5 AI tools jo har creator ko pata hone chahiye! Save this before it gets deleted 🤫\n\n1. ElevenLabs for AI voice\n2. OpusClip for viral shorts\n3. CapCut Desktop\n4. Midjourney v6\n5. Notion AI for scripts\n\nComment 'TOOL' aur main direct link bhej dunga! 👇",
                        hashtags = "#aitools #reelsindia #creatortips #instagramgrowth #trendingreels #techhindi",
                        likes = 3450,
                        comments = 480,
                        shares = 920,
                        saves = 1840,
                        reach = 38000,
                        impressions = 45000,
                        dateEpoch = now - (oneDay * 2),
                        engagementRate = 17.6f,
                        saveToReachRatio = 4.8f,
                        aiAuditScore = 92,
                        aiHookFeedback = "Powerful curiosity hook with urgency ('before it gets deleted'). Excellent viral retention design.",
                        aiStrengths = "High save-to-reach ratio (4.8%), clear CTA encouraging DM automation with 'Comment TOOL', crisp bullet points.",
                        aiImprovements = "Add a quick 2-second on-screen preview of tool #1 within the first 3 seconds to increase watch time above 85%.",
                        aiHashtagAdvice = "Good mix of niche and broad tags. Consider adding 2 micro-niche tags like #contentcreatortools.",
                        aiViralRating = "Viral Performer 🔥"
                    )
                )

                // Sample Post 2: Educational Carousel
                dao.insertPost(
                    PostEntity(
                        postType = "Carousel",
                        caption = "Instagram algorithm 2026 update: Views down kyu ho rahe hain? 📉\n\nSwipe left to see exactly how the new ranking signals work & how to fix your reach in 7 days.\n\nSwipe till the end for the posting checklist! ➡️",
                        hashtags = "#algorithmhack #instagramtips #pagegrowth #socialmediastrategy #digitalmarketinghindi",
                        likes = 1280,
                        comments = 145,
                        shares = 310,
                        saves = 790,
                        reach = 14500,
                        impressions = 19200,
                        dateEpoch = now - (oneDay * 5),
                        engagementRate = 17.4f,
                        saveToReachRatio = 5.4f,
                        aiAuditScore = 88,
                        aiHookFeedback = "Direct problem-solving hook addressing creator pain point ('Views down kyu ho rahe hain?').",
                        aiStrengths = "Carousels with swipe prompts boost dwell time. High saves indicate evergreen bookmark value.",
                        aiImprovements = "Slide 1 cover could use a higher contrast text banner and bold question mark.",
                        aiHashtagAdvice = "Hashtags are well targeted to growth seekers. 5 tags is an optimal clean number.",
                        aiViralRating = "High Performer ⭐"
                    )
                )

                // Sample Post 3: Photo / Single image needing improvement
                dao.insertPost(
                    PostEntity(
                        postType = "Single Photo",
                        caption = "New workspace setup ready. Ready to record new videos. Let me know what you think!",
                        hashtags = "#workspace #desksetup #creator #work #photo",
                        likes = 420,
                        comments = 28,
                        shares = 12,
                        saves = 18,
                        reach = 8200,
                        impressions = 9800,
                        dateEpoch = now - (oneDay * 9),
                        engagementRate = 5.8f,
                        saveToReachRatio = 0.22f,
                        aiAuditScore = 58,
                        aiHookFeedback = "Weak passive hook. Doesn't trigger curiosity or immediate value for the scroller.",
                        aiStrengths = "Aesthetic personal touch creates connection with loyal followers.",
                        aiImprovements = "Turn this into a Reel or carousel listing the exact gear specs and pricing. Ask a specific question like 'Which monitor arm do you use?' rather than generic 'What you think'.",
                        aiHashtagAdvice = "#photo and #work are too generic (over 100M posts). Replace with #creatorworkspace and #desktour.",
                        aiViralRating = "Needs Optimization ⚠️"
                    )
                )
            }
        }
    }
}

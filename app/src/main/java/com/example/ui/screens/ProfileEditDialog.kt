package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProfileEntity
import com.example.ui.theme.InstaPink

@Composable
fun ProfileEditDialog(
    profile: ProfileEntity?,
    language: String,
    onDismiss: () -> Unit,
    onSave: (
        username: String,
        displayName: String,
        niche: String,
        followers: Int,
        following: Int,
        totalPosts: Int,
        bio: String
    ) -> Unit
) {
    var username by remember { mutableStateOf(profile?.username ?: "@my_insta_page") }
    var displayName by remember { mutableStateOf(profile?.displayName ?: "Creator Hub") }
    var niche by remember { mutableStateOf(profile?.niche ?: "Technology & AI") }
    var followersText by remember { mutableStateOf("${profile?.followers ?: 12400}") }
    var followingText by remember { mutableStateOf("${profile?.following ?: 350}") }
    var totalPostsText by remember { mutableStateOf("${profile?.totalPosts ?: 48}") }
    var bio by remember { mutableStateOf(profile?.bio ?: "Helping you grow on Instagram.") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (language == "Hindi") "✏️ प्रोफाइल विवरण अपडेट करें" else "✏️ Edit Profile Details",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username / Handle") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("edit_username")
                )

                OutlinedTextField(
                    value = displayName,
                    onValueChange = { displayName = it },
                    label = { Text("Display Name") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("edit_display_name")
                )

                OutlinedTextField(
                    value = niche,
                    onValueChange = { niche = it },
                    label = { Text("Page Niche / Category") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("edit_niche")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = followersText,
                        onValueChange = { followersText = it.filter { c -> c.isDigit() } },
                        label = { Text("Followers") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).testTag("edit_followers")
                    )

                    OutlinedTextField(
                        value = followingText,
                        onValueChange = { followingText = it.filter { c -> c.isDigit() } },
                        label = { Text("Following") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).testTag("edit_following")
                    )
                }

                OutlinedTextField(
                    value = totalPostsText,
                    onValueChange = { totalPostsText = it.filter { c -> c.isDigit() } },
                    label = { Text("Total Posts Count") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("edit_total_posts")
                )

                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Profile Bio") },
                    minLines = 3,
                    maxLines = 5,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("edit_bio")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val followers = followersText.toIntOrNull() ?: (profile?.followers ?: 1000)
                    val following = followingText.toIntOrNull() ?: (profile?.following ?: 100)
                    val totalPosts = totalPostsText.toIntOrNull() ?: (profile?.totalPosts ?: 10)
                    onSave(username, displayName, niche, followers, following, totalPosts, bio)
                },
                colors = ButtonDefaults.buttonColors(containerColor = InstaPink),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("button_save_profile")
            ) {
                Text(if (language == "Hindi") "सेव करें" else "Save")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(if (language == "Hindi") "रद्द करें" else "Cancel")
            }
        }
    )
}

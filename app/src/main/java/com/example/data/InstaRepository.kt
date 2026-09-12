package com.example.data

import com.example.data.model.PostEntity
import com.example.data.model.ProfileEntity
import kotlinx.coroutines.flow.Flow

class InstaRepository(private val dao: InstaDao) {
    val allPosts: Flow<List<PostEntity>> = dao.getAllPosts()
    val profileFlow: Flow<ProfileEntity?> = dao.getProfileFlow()

    suspend fun getPostById(id: Long): PostEntity? = dao.getPostById(id)

    suspend fun insertPost(post: PostEntity): Long = dao.insertPost(post)

    suspend fun updatePost(post: PostEntity) = dao.updatePost(post)

    suspend fun deletePost(id: Long) = dao.deletePost(id)

    suspend fun getProfile(): ProfileEntity? = dao.getProfile()

    suspend fun saveProfile(profile: ProfileEntity) = dao.saveProfile(profile)
}

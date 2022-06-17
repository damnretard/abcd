package com.example.gradpro.models

import com.google.firebase.firestore.PropertyName


data class student_posts(
    var description: String = "",
    @get:PropertyName("image_url") @set:PropertyName("image_url")var imageUrl: String = "",
    @get:PropertyName("creation_time_ms") @set:PropertyName("creation_time_ms")var creationTimeMs: Long = 0,
    var student: student? = null
)
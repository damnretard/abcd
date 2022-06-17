package com.example.gradpro.models

import com.google.firebase.firestore.PropertyName

data class student(
    var username: String = "",
    @get:PropertyName("roll_no") @set:PropertyName("roll_no")var rollNo: String = ""
)


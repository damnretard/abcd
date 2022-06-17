package com.example.gradpro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.gradpro.models.student_posts
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

private const val TAG = "PostsActivity"
class PostsActivity : AppCompatActivity() {

    private lateinit var firestoreDb: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        firestoreDb = FirebaseFirestore.getInstance()
        val postsReference = firestoreDb
            .collection("student_posts")
            .limit(30)
            .orderBy("creation_time_ms",Query.Direction.DESCENDING )
        postsReference.addSnapshotListener { snapshot, exception ->
            if(exception != null || snapshot == null){
                Log.e(TAG, "Exception when querying posts", exception)
                return@addSnapshotListener
            }
            val postList = snapshot.toObjects(student_posts::class.java)
            for (student_post in postList){
                Log.i(TAG, "Student_post ${student_post}")
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_posts, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_profile) {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}
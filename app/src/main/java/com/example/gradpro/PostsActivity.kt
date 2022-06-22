package com.example.gradpro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gradpro.models.student
import com.example.gradpro.models.student_posts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_posts.*

private const val TAG = "PostsActivity"
public const val EXTRA_USERNAME = "EXTRA_USERNAME"
open class PostsActivity : AppCompatActivity() {

    private var signedInUser: student? = null
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var posts: MutableList<student_posts>
    private lateinit var adapter: PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        posts = mutableListOf()

        adapter = PostsAdapter(this, posts)

        rvPosts.adapter = adapter
        rvPosts.layoutManager = LinearLayoutManager(this)

        firestoreDb = FirebaseFirestore.getInstance()

        firestoreDb.collection("students")
            .document(FirebaseAuth.getInstance().currentUser?.uid as String)
            .get()
            .addOnSuccessListener { studentsSnapshot ->
                signedInUser = studentsSnapshot.toObject(student:: class.java)
                Log.i(TAG, "signed in student: $signedInUser")
            }
            .addOnFailureListener { exception ->
                Log.i(TAG, "Failure fetching signed in student", exception)
            }


        var postsReference = firestoreDb
            .collection("student_posts")
            .limit(30)
            .orderBy("creation_time_ms",Query.Direction.DESCENDING )
        val username = intent.getStringExtra(EXTRA_USERNAME)
        if (username != null){
            supportActionBar?.title = username
            postsReference = postsReference.whereEqualTo("student.username", username)

        }
        postsReference.addSnapshotListener { snapshot, exception ->
            if(exception != null || snapshot == null){
                Log.e(TAG, "Exception when querying posts", exception)
                return@addSnapshotListener
            }
            val postList = snapshot.toObjects(student_posts::class.java)
            posts.clear()
            posts.addAll(postList)
            adapter.notifyDataSetChanged()
            for (student_post in postList){
                Log.i(TAG, "Student_post $student_post")
            }
        }
        fabCreate.setOnClickListener {
            val intent = Intent(this,CreateActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_posts, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_profile) {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra(EXTRA_USERNAME, signedInUser?.username)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}
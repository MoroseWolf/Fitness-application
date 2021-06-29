package com.example.fitnessapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.fitnessapplication.ui.main.exercise.ExerciseActivity
import com.example.fitnessapplication.ui.main.exercise.ExerciseAdapter
import com.firebase.ui.auth.AuthUI
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.addExercise) {
            val intent = Intent(this, ExerciseActivity::class.java)
            startActivity(intent)
        }
        if(item.itemId == R.id.login_button) {
            AuthUI.getInstance().signOut(applicationContext)
        }

        return super.onOptionsItemSelected(item)
    }
}
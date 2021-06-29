package com.example.fitnessapplication.ui.main.exercise

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnessapplication.R
import com.example.fitnessapplication.model.Exercise
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class ExerciseActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ExerciseActivity"
    }

    private var db: FirebaseFirestore? = null
    private var key: String = ""
    private var date: String = ""

    val currentUser: String = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.email.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercise_add_activity)

        db = FirebaseFirestore.getInstance()

        val activityParts: ActivityParts = ActivityParts()

        val bundle = intent.extras
        if (bundle != null) {
            key = bundle.getString("UpdateExerciseId").toString()

            activityParts.editName.setText(bundle.getString("UpdateExerciseName"))
            activityParts.edtDescription.setText(bundle.getString("UpdateExerciseDescription"))
            activityParts.edtCountOfRepeat.setText(bundle.getInt("UpdateExerciseCountOfRepeat").toString())
            activityParts.edtCountOfApproaches.setText(bundle.getInt("UpdateExerciseCountOfApproaches").toString())
            activityParts.edtBodyPart.setText(bundle.getString("UpdateExerciseBodyPart"))


            date = bundle.getString("UpdateExerciseDate").toString()
        }

        activityParts.buttonAdd.setOnClickListener {
            val name = activityParts.editName.text.toString()
            val desc = activityParts.edtDescription.text.toString()
            val repCount = activityParts.edtCountOfRepeat.text.toString().toInt()
            val apprCount = activityParts.edtCountOfApproaches.text.toString().toInt()
            val body = activityParts.edtBodyPart.text.toString()

            if(key.isNotEmpty()){
                updateExercise(key, name, desc, repCount, apprCount, body, date)
            } else {
                addExercise(name, desc, repCount, apprCount, body)
            }

            finish()
        }
    }

    inner class ActivityParts {
        var editName = findViewById<EditText>(R.id.edtName)
        var edtDescription = findViewById<EditText>(R.id.edtDescription)
        var edtCountOfRepeat = findViewById<EditText>(R.id.edtCountOfRepeat)
        var edtCountOfApproaches = findViewById<EditText>(R.id.edtCountOfApproaches)
        var edtBodyPart = findViewById<EditText>(R.id.edtBodyPart)

        var buttonAdd = findViewById<Button>(R.id.btAdd)
    }

    private fun updateExercise(
        key: String,
        name: String,
        description: String,
        countOfRepeat: Int,
        countOfApproaches: Int,
        bodyPart: String,
        day: String
    ) {
        val exercise = Exercise(key, name, description, countOfRepeat, countOfApproaches,  bodyPart, day).toMap()

        db?.collection(currentUser)
            ?.document(key)
            ?.set(exercise)
            ?.addOnSuccessListener {
                Log.e(TAG, "Exercise update successful!")
                Toast.makeText(applicationContext, "Exercise updated!", Toast.LENGTH_SHORT).show()
            }
            ?.addOnFailureListener { e ->
                Log.e(TAG, "Error adding Exercise", e)
                Toast.makeText(applicationContext, "Exercise could not be updated!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addExercise(
        name: String,
        description: String,
        countOfRepeat: Int,
        countOfApproaches: Int,
        bodyPart: String
    ) {

        val format = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        val nowDate = format.format(Date())

        val exercise = Exercise(name, description, countOfRepeat, countOfApproaches,  bodyPart, nowDate).toMap()

        db?.collection(currentUser)
            ?.add(exercise)
            ?.addOnSuccessListener { documentReference ->
                Log.e(TAG, "DocumentSnapshot written with ID: " + documentReference.id)
                Toast.makeText(applicationContext, "Exercise has been added!", Toast.LENGTH_SHORT).show()
            }
            ?.addOnFailureListener { e ->
                Log.e(TAG, "Error adding Note document", e)
                Toast.makeText(applicationContext, "Exercise could not be added!", Toast.LENGTH_SHORT).show()
            }

    }
}
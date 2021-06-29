package com.example.fitnessapplication.ui.main.exercise

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapplication.R
import com.example.fitnessapplication.model.Exercise
import com.google.firebase.firestore.FirebaseFirestore


class ExerciseAdapter(
        private val exerciseList: MutableList<Exercise>,
        private val context: Context,
        private val db: FirebaseFirestore
)
    : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    val currentUser: String = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.email.toString()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)

        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exerciseList[position]

        holder.name.text = exercise.name
        holder.descript.text = exercise.description
        holder.countOfRepeat.text = exercise.countOfRepeat.toString()
        holder.countOfApproaches.text = exercise.countOfApproaches.toString()
        holder.bodyPart.text = exercise.bodyPart
        holder.date.text = exercise.day

        val popupMenu = PopupMenu(context, holder.moreBtn)
        popupMenu.inflate(R.menu.menu_popup)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit_btn -> {
                    updateExercise(exercise)
                    true
                }
                R.id.delete_btn -> {
                    exercise.key?.let { it1 -> deleteExercise(it1, position) }!!
                    true
                }
                else -> false
            }
        }

        holder.moreBtn.setOnClickListener{ popupMenu.show()}
    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }

    inner class ExerciseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name = view.findViewById<TextView>(R.id.name)
        var descript = view.findViewById<TextView>(R.id.description)
        var countOfRepeat = view.findViewById<TextView>(R.id.count_of_repeat)
        var countOfApproaches = view.findViewById<TextView>(R.id.count_of_approaches)
        var bodyPart = view.findViewById<TextView>(R.id.bodyPart)
        var date = view.findViewById<TextView>(R.id.exercise_date)

        var moreBtn = view.findViewById<ImageView>(R.id.ivMore)
    }

    private fun updateExercise(exercise: Exercise) {
        val intent = Intent(context, ExerciseActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("UpdateExerciseId", exercise.key)
        intent.putExtra("UpdateExerciseDescription", exercise.description)
        intent.putExtra("UpdateExerciseName", exercise.name)
        intent.putExtra("UpdateExerciseCountOfRepeat", exercise.countOfRepeat)
        intent.putExtra("UpdateExerciseCountOfApproaches", exercise.countOfApproaches)
        intent.putExtra("UpdateExerciseBodyPart", exercise.bodyPart)
        intent.putExtra("UpdateExerciseDate", exercise.day)
        context.startActivity(intent)

    }

    private fun deleteExercise(id: String, position: Int) {
        db.collection(currentUser)
            .document(id)
            .delete()
            .addOnCompleteListener {
                exerciseList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, exerciseList.size)
                Toast.makeText(context, "Exercise has been deleted!", Toast.LENGTH_SHORT).show()
            }
    }

}
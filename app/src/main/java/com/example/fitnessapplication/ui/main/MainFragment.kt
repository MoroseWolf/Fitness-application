package com.example.fitnessapplication.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessapplication.R
import com.example.fitnessapplication.databinding.MainFragmentBinding
import com.example.fitnessapplication.model.Exercise
import com.example.fitnessapplication.ui.main.exercise.ExerciseAdapter
import com.example.fitnessapplication.ui.main.login.LoginViewModel
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class MainFragment : Fragment() {

    private var adapter: ExerciseAdapter? = null

    private var db: FirebaseFirestore? = null
    private var listenerDB: ListenerRegistration? = null
    val currentUser: String = FirebaseAuth.getInstance().currentUser?.email.toString()

    companion object {
        const val TAG = "MainFragment"
        const val SIGN_IN_RESULT_CODE = 1001
    }

    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAuthenticationState()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESULT_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                Log.i( TAG,"Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!")
            } else {
                Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }

    private fun observeAuthenticationState() {

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    db = FirebaseFirestore.getInstance()
                    loadExercises()

                    listenerDB = db?.collection(currentUser)
                        ?.addSnapshotListener(EventListener {documentSnapshots, e ->
                            if (e != null) {
                                Log.e(TAG, "Listen failed!", e)
                                return@EventListener
                            }

                            val exList = mutableListOf<Exercise>()

                            if (documentSnapshots != null) {
                                for (temp in documentSnapshots) {

                                    val exTemp = temp.toObject(Exercise::class.java)
                                    exTemp.key = temp.id
                                    exList.add(exTemp)
                                }
                            }

                            adapter = activity?.let { ExerciseAdapter(exList, it, db!!) }
                            binding.exerciseList.adapter = adapter
                        })

                }
                else -> {
                    findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
                }
            }
        })
    }


    private fun loadExercises() {
        db?.collection(currentUser)
            ?.get()
            ?.addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    val exList = mutableListOf<Exercise>()

                    for (temp in task.result!!) {
                        val tempEx = temp.toObject<Exercise>(Exercise::class.java)
                        tempEx.key = temp.id
                        exList.add(tempEx)
                    }

                    adapter = activity?.let { ExerciseAdapter(exList, it, db!!) }
                    binding.exerciseList.layoutManager = LinearLayoutManager(activity)
                    binding.exerciseList.adapter = adapter
                }
                else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            }

    }

}
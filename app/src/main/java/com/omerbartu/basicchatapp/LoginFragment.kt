package com.omerbartu.basicchatapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.omerbartu.basicchatapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private lateinit var auth:FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        auth= Firebase.auth

        val currentUser=auth.currentUser
        if (currentUser!= null){

            val action= LoginFragmentDirections.actionLoginFragmentToChatFragment()
            findNavController().navigate(action)

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visibility=View.GONE

        binding.signButton.setOnClickListener {
            binding.progressBar.visibility=View.VISIBLE
            auth.createUserWithEmailAndPassword(binding.userText.text.toString(),binding.passwordText.text.toString()).addOnSuccessListener {

                Toast.makeText(requireContext(),"User is created",Toast.LENGTH_LONG).show()

            }.addOnFailureListener {

                Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
            }

        }
        binding.loginButton.setOnClickListener {
            binding.progressBar.visibility=View.VISIBLE
            auth.signInWithEmailAndPassword(binding.userText.text.toString(),binding.passwordText.text.toString()).addOnSuccessListener {

                val action= LoginFragmentDirections.actionLoginFragmentToChatFragment()
                Navigation.findNavController(view).navigate(action)

            }.addOnFailureListener {

                Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
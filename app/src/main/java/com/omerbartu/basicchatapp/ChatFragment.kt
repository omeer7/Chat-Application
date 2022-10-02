package com.omerbartu.basicchatapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.omerbartu.basicchatapp.databinding.FragmentChatBinding

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null

    private val binding get() = _binding!!

    private lateinit var auth:FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: ChatAdapter
    private var chats = arrayListOf<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firestore=Firebase.firestore
        auth=Firebase.auth

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visibility=View.GONE

        adapter=ChatAdapter()
        binding.recyclerView.adapter=adapter
        binding.recyclerView.layoutManager=LinearLayoutManager(requireContext())

        binding.logOut.setOnClickListener {

            auth.signOut()
            val action= ChatFragmentDirections.actionChatFragmentToLoginFragment()
            findNavController().navigate(action)

        }

        binding.sendButton.setOnClickListener {

            binding.progressBar.visibility=View.VISIBLE


            auth.currentUser?.let{
                val chatText = binding.chatText.text.toString()
                val user= it.email
                val date= FieldValue.serverTimestamp()

                val dataMap= HashMap<String,Any>()
                dataMap.put("user",user!!)
                dataMap.put("chat",chatText)
                dataMap.put("date",date)

                firestore.collection("Chats").add(dataMap).addOnSuccessListener {

                    binding.chatText.setText("")
                    binding.progressBar.visibility=View.GONE

                }.addOnFailureListener {

                    Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
                    binding.chatText.setText("")
                }
            }

        }
        firestore.collection("Chats").orderBy("date",Query.Direction.ASCENDING).addSnapshotListener { value, error ->

            if (error!=null){

                Toast.makeText(requireContext(),error.localizedMessage,Toast.LENGTH_LONG).show()

            }
            else{

                if (value!=null){

                    if (value.isEmpty){

                        Toast.makeText(requireContext(),"No Message",Toast.LENGTH_LONG).show()
                    }
                    else{

                        val documents = value.documents

                        chats.clear()

                        for (document in documents){
                            val text = document.get("chat") as String
                            val user = document.get("user") as String
                            val chat= Chat(user,text)
                            chats.add(chat)
                            adapter.chats=chats


                        }

                    }

                    adapter.notifyDataSetChanged()

                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
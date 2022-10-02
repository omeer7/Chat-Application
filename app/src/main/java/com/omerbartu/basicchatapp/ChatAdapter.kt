package com.omerbartu.basicchatapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.omerbartu.basicchatapp.databinding.RecylerRowBinding
import java.lang.reflect.Array.set

class ChatAdapter: RecyclerView.Adapter<ChatAdapter.ChatHolder>() {



    class ChatHolder(val binding: RecylerRowBinding) : RecyclerView.ViewHolder(binding.root)


    private val diffUtil = object :DiffUtil.ItemCallback<Chat>(){
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {

            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {

            return oldItem == newItem
        }

    }

    private val recyclerDiffer=AsyncListDiffer(this,diffUtil)
    var chats:List<Chat>
    get() =recyclerDiffer.currentList
        set(value) = recyclerDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val binding= RecylerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ChatHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {


        holder.binding.chatRecylerViewRow.text="${chats.get(position).text}"

    }

    override fun getItemCount(): Int {

        return chats.size
    }


}
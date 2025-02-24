package com.assignment.myapplication.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.assignment.myapplication.R
import com.assignment.myapplication.domain.Question
import com.assignment.myapplication.presentation.viewholder.SearchViewHolder

class SearchResultAdapter(private var questions: List<Question>) : RecyclerView.Adapter<SearchViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_questions,parent,false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question)
    }

    override fun getItemCount(): Int {
      return questions.size
    }

    fun updateData(newQuestions: List<Question>) {
        questions = newQuestions
        notifyDataSetChanged()
    }

    fun clearData() {
        questions = ArrayList()
        notifyDataSetChanged()
    }


}
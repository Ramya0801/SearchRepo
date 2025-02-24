package com.assignment.myapplication.presentation.viewholder

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.assignment.myapplication.R
import com.assignment.myapplication.domain.Question
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SearchViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {

    private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
    private val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
    private val creationDateTextView: TextView = itemView.findViewById(R.id.creationDateTextView)
    private val link: TextView = itemView.findViewById(R.id.linkTextView)

    private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    fun bind(question: Question) {
        with(itemView) {
            titleTextView.text = question.title
            authorTextView.text = question.owner.displayName
            link.text = question.link
            creationDateTextView.text = dateFormat.format(Date(question.creationDate))

            setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(question.link))
                itemView.context.startActivity(intent)
            }
        }
    }

}
package com.ashvia.quizee.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ashvia.quizee.R
import com.ashvia.quizee.model.Question


class QuestionAdapter(var dataSet: ArrayList<Question> = ArrayList()) :
    RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView
        val category: TextView
        val question: TextView
        val reward: TextView

        init {
            // Define click listener for the ViewHolder's View.
            title = view.findViewById(R.id.questionTitle)
            category = view.findViewById(R.id.category)
            question = view.findViewById(R.id.question)
            reward = view.findViewById(R.id.reward)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_question, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.title.text = dataSet[position].title
        viewHolder.category.text = dataSet[position].category
        dataSet[position].items?.size.let {
            viewHolder.question.text = String.format("%s Pertanyaan", it)
        }
        var reward = 0
        for (row in dataSet[position].items!!) {
            reward += row.reward!!
        }
        viewHolder.reward.text = String.format("%s Poin", reward)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}

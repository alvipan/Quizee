package com.ashvia.quizee.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ashvia.quizee.R
import com.ashvia.quizee.data.Question


class QuestionAdapter(var context: Context, var dataSet: ArrayList<Question> = ArrayList()) :
    RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {

    var onItemClick: ((Int) -> Unit)? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val card: CardView
        val title: TextView
        val category: TextView
        val question: TextView
        val reward: TextView

        init {
            // Define click listener for the ViewHolder's View.
            card = view.findViewById(R.id.cardview)
            title = view.findViewById(R.id.title)
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
    override fun onBindViewHolder(view: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        view.title.text = dataSet[position].title
        view.category.text = dataSet[position].category
        dataSet[position].items?.size.let {
            view.question.text = String.format("%s Pertanyaan", it)
        }
        var reward = 0
        for (row in dataSet[position].items!!) {
            reward += row.reward
        }
        view.reward.text = String.format("%s Poin", reward)
        view.card.setOnClickListener {
            onItemClick?.invoke(position)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}

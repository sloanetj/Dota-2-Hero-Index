package com.example.firebasetest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class IconListAdapter (private val icons : List<HeroIcon>, private val context : Context, private val savedInstanceState: Bundle?): RecyclerView.Adapter<IconListAdapter.IconsViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconsViewHolder {
        // Open & parse our XML file
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_item, parent, false)

        // Create a new ViewHolder
        return IconsViewHolder(view)
    }

    // Returns the number of rows to render
    override fun getItemCount(): Int = icons.size

    // onBindViewHolder is called when the RecyclerView is ready to display a new row at [position]
    // and needs you to fill that row with the necessary data.
    //
    // It passes you a ViewHolder, either from what you returned from onCreateViewHolder *or*
    // it's passing you an existing ViewHolder as a part of the "recycling" mechanism.
    override fun onBindViewHolder(holder: IconsViewHolder, position: Int) {
        val currentIcon = icons[position]

//        // hero id stored in content description. used in on-click api call
//        holder.icon.contentDescription = currentIcon.id

        // get the image from the url
        Picasso
            .get()
            .load(currentIcon.iconUrl)
            .error(R.drawable.abaddon_icon)
            .into(holder.icon)

//        holder.icon.setImageResource(currentIcon)
        holder.icon.setScaleType(ImageView.ScaleType.FIT_XY)

        holder.icon.setOnClickListener {
            val intent = Intent(context, HeroInfoActivity::class.java)
            intent.putExtra("HERO ID", currentIcon.heroId)
            startActivity(context, intent, savedInstanceState)

        }



    }

    // A ViewHolder is a class which *holds* references to *views* that we care about in each
    // individual row. The findViewById function is somewhat inefficient, so the idea is to the lookup
    // for each view once and then reuse the object.
    class IconsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var icon: ImageButton = view.findViewById(R.id.imageButton)

    }
}
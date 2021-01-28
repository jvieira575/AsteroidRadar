package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidItemBinding
import com.udacity.asteroidradar.domain.Asteroid

/**
 * Asteroid [RecyclerView.Adapter] for use to display [Asteroid] items.
 */
class AsteroidAdapter(private val asteroidClickListener: AsteroidClickListener) :
    RecyclerView.Adapter<AsteroidAdapter.AsteroidViewHolder>() {

    // Holds the asteroids to display
    var asteroids: List<Asteroid> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = asteroids.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val asteroidItemBinding: AsteroidItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            AsteroidViewHolder.LAYOUT,
            parent,
            false
        )
        return AsteroidViewHolder(asteroidItemBinding)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        holder.asteroidItemBinding.also {
            it.asteroid = asteroids[position]
            it.asteroidClickListener = asteroidClickListener
        }
    }

    /**
     * Custom [RecyclerView.ViewHolder] implementation.
     */
    class AsteroidViewHolder(val asteroidItemBinding: AsteroidItemBinding) :
        RecyclerView.ViewHolder(asteroidItemBinding.root) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.asteroid_item
        }
    }

    /**
     * Click listener for use in [MainFragment] layout [RecyclerView].
     */
    class AsteroidClickListener(val block: (Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = block(asteroid)
    }
}
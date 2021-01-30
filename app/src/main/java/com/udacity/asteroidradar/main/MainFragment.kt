package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.domain.Asteroid

/**
 * [Fragment] used to display the NASA Picture of the Day and a [List] of Near Earth Objects. Serves
 * as the first view a user sees when launching the app.
 */
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModel.Factory(requireActivity().application)).get(
            MainViewModel::class.java
        )
    }

    private var asteroidAdapter: AsteroidAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Observe Nasa APOD and display it
        viewModel.pictureOfDay.observe(viewLifecycleOwner, Observer {
            Picasso.with(requireContext())
                .load(it.url)
                .placeholder(R.drawable.placeholder_picture_of_day)
                .error(R.drawable.placeholder_picture_of_day)
                .into(binding.activityMainImageOfTheDay)

            // Set the content description dynamically by using the title of the response
            binding.activityMainImageOfTheDay.contentDescription = it.title
        })

        // Declare asteroid adapter
        asteroidAdapter = AsteroidAdapter(AsteroidAdapter.AsteroidClickListener {
            viewModel.navigateToSelectedAsteroid(it)
        })

        binding.root.findViewById<RecyclerView>(R.id.asteroid_recycler).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = asteroidAdapter
        }

        // Observe whether the user has navigated to the details screen
        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.navigateToSelectedAsteroidCompleted()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe asteroids and update list
        viewModel.asteroids.observe(viewLifecycleOwner, Observer<List<Asteroid>> { asteroids ->
            asteroids?.apply {
                asteroidAdapter?.asteroids = asteroids
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        viewModel.updateFilter(
            when (item.itemId) {
                R.id.show_today_menu -> AsteroidFilter.SHOW_TODAY
                R.id.show_week_menu -> AsteroidFilter.SHOW_WEEK
                else -> AsteroidFilter.SHOW_ALL
            }
        )

        return true
    }
}

package ba.grbo.doitintime.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ba.grbo.doitintime.R
import ba.grbo.doitintime.databinding.FragmentToDosBinding
import ba.grbo.doitintime.ui.viewmodels.ToDosViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ToDosFragment : Fragment() {
    private val viewModel: ToDosViewModel by viewModels()
    lateinit var binding: FragmentToDosBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToDosBinding.inflate(inflater, container, false)

        viewModel.addObservers()

        binding.addToDoFloatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_toDosFragment_to_addToDoFragment)
        }

        binding.toDosCoordinatorLayout.setOnClickListener {
            Log.i("ToDosFragmenet", "coordinator clicked")
            findNavController().navigate(R.id.action_toDosFragment_to_updateToDoFragment)
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.to_dos_menu, menu)

        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setOnSearchClickListener {
        }

        searchView.setOnCloseListener {
            false
        }
    }

    private object onQueryTextListener : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            TODO("Not yet implemented")
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            TODO("Not yet implemented")
        }
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        if (nextAnim == 0 || !enter) return super.onCreateAnimation(transit, enter, nextAnim)
        else {
            return AnimationUtils.loadAnimation(requireContext(), nextAnim).apply {
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                        setEnabled(false)
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        setEnabled(true)
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }
                })
            }
        }
    }

    private fun ToDosViewModel.addObservers() {
//        observeToDosCount()
    }

    private fun ToDosViewModel.observeToDosCount() {
//        toDosCount.observe(viewLifecycleOwner) {
//            Snackbar.make(
//                binding.toDosCoordinatorLayout,
//                "Todo successfully added",
//                Snackbar.LENGTH_LONG
//            ).show()
//        }
    }

    private fun setEnabled(state: Boolean) {
        binding.addToDoFloatingActionButton.isClickable = state
        binding.toDosCoordinatorLayout.isEnabled = state
    }
}
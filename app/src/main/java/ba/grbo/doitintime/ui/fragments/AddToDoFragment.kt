package ba.grbo.doitintime.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ba.grbo.doitintime.R
import ba.grbo.doitintime.databinding.FragmentAddOrUpdateToDoBinding
import ba.grbo.doitintime.ui.adapters.ToDoAdapter
import ba.grbo.doitintime.ui.viewmodels.AddToDoViewModel
import ba.grbo.doitintime.utilities.EventObserver
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddToDoFragment : Fragment() {
    private val addToDoViewModel: AddToDoViewModel by viewModels()
    private lateinit var binding: FragmentAddOrUpdateToDoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddOrUpdateToDoBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            adapter = ToDoAdapter(
                addToDoViewModel.viewsEnabled,
                addToDoViewModel.titleWarningMessage,
                viewLifecycleOwner,
                requireContext(),
                ::observeTitleLength,
                ::showKeyboard,
                ::hideKeyboard
            )
            toDo = addToDoViewModel.toDo
        }

        addToDoViewModel.addObservers()
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_to_do_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.confirm_add) addToDoViewModel.onConfirmAddClicked()
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (nextAnim == 0 || !enter) super.onCreateAnimation(transit, enter, nextAnim)
        else AnimationUtils.loadAnimation(requireContext(), nextAnim).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    addToDoViewModel.onAnimationEnd()
                    setupEverything()
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.adapter?.cancelJob()
    }

    private fun setupEverything() {
        binding.run {
//            lifecycleOwner = viewLifecycleOwner
//            adapter = ToDoAdapter(
//                addToDoViewModel.viewsEnabled,
//                addToDoViewModel.titleWarningMessage,
//                viewLifecycleOwner,
//                requireContext(),
//                ::observeTitleLength,
//                ::showKeyboard,
//                ::hideKeyboard
//            )
//            toDo = addToDoViewModel.toDo
        }

//        addToDoViewModel.addObservers()
    }

    private fun AddToDoViewModel.addObservers() {
        observeTitleStillTooLongEvent()
        observeProceedToToDosFragmentEvent()
        observeDatabaseErrorEvent()
        observeInvalidFieldsErrorEvent()
        observeUnknownErrorEvent()
    }

    private fun AddToDoViewModel.observeTitleStillTooLongEvent() {
        titleStillTooLongEvent.observe(viewLifecycleOwner, EventObserver({
            hideKeyboard()
            showSnackbar(it)
        }))
    }

    private fun AddToDoViewModel.observeProceedToToDosFragmentEvent() {
        proceedToToDosFragmentEvent.observe(viewLifecycleOwner, EventObserver({
            moveBackToToDosFragment()
        }))
    }

    private fun AddToDoViewModel.observeDatabaseErrorEvent() {
        databaseErrorEvent.observe(viewLifecycleOwner, EventObserver({
            showDialog(it.first, it.second, it.third)
        }))
    }

    private fun AddToDoViewModel.observeInvalidFieldsErrorEvent() {
        invalidFieldsErrorEvent.observe(viewLifecycleOwner, EventObserver({
            showDialog(it.first, it.second, it.third)
        }))
    }

    private fun AddToDoViewModel.observeUnknownErrorEvent() {
        unknownErrorEvent.observe(viewLifecycleOwner, EventObserver({
            showDialog(it.first, it.second, it.third)
        }))
    }

    private fun showSnackbar(@StringRes message: Int) {
        Snackbar.make(binding.addOrUpdateLinearLayout, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showDialog(
        @StringRes title: Int,
        @StringRes message: Int,
        @StringRes buttonText: Int
    ) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(buttonText) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun hideKeyboard() {
        (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(binding.addOrUpdateLinearLayout.windowToken, 0)
    }

    private fun showKeyboard(view: View) {
        (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(view, 0)
    }

    private fun moveBackToToDosFragment() {
        requireActivity().onBackPressed()
    }

    private fun observeTitleLength(length: Int) {
        addToDoViewModel.onTitleLengthChanged(length)
    }
}
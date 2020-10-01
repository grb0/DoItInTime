package ba.grbo.doitintime.ui.fragments

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import androidx.annotation.StringRes
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ba.grbo.doitintime.R
import ba.grbo.doitintime.databinding.FragmentAddOrUpdateToDoBinding
import ba.grbo.doitintime.ui.viewmodels.AddToDoViewModel
import ba.grbo.doitintime.utilities.EventObserver
import ba.grbo.doitintime.utilities.MaterialSpinnerAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddToDoFragment : Fragment() {
    private val addToDoViewModel: AddToDoViewModel by viewModels()
    private lateinit var binding: FragmentAddOrUpdateToDoBinding
    private var wasEnabled = false

    companion object {
        private const val WAS_ENABLED = "WAS_ENABLED"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddOrUpdateToDoBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = addToDoViewModel
        }

        reinstantiateSavedState(savedInstanceState)
        setupViews()
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
                    setEnabled()
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(WAS_ENABLED, wasEnabled)
    }

    private fun reinstantiateSavedState(savedInstanceState: Bundle?) {
        savedInstanceState?.run {
            wasEnabled = getBoolean(WAS_ENABLED).also { if (it) setEnabled() }
        }
    }

    private fun setupViews() {
        setupTitleEditText()
        setupPriorityDropdownMenu()
        setStatusDropdownMenu()
    }

    private fun setupTitleEditText() {
//        setupEditText(
//            binding.titleEditText,
//            ::observeTitleLength
//        )
    }

    private fun setupPriorityDropdownMenu() {
//        setupDropdownMenu(
//            listOf(Priority.High.name, Priority.Medium.name, Priority.Low.name),
//            listOf(
//                R.drawable.ic_priority_high,
//                R.drawable.ic_priority_medium,
//                R.drawable.ic_priority_low
//            ),
//            "priority",
//            binding.priorityDropdownMenu,
//            binding.priorityLayout
//        )
    }

    private fun setStatusDropdownMenu() {
//        setupDropdownMenu(
//            listOf(Status.Active.identifier, Status.Completed.identifier, Status.OnHold.identifier),
//            listOf(
//                R.drawable.ic_status_active,
//                R.drawable.ic_status_completed,
//                R.drawable.ic_status_on_hold
//            ),
//            "status",
//            binding.statusDropdownMenu,
//            binding.statusLayout
//        )
    }

    private fun setupEditText(
        primaryEditText: TextInputEditText,
        observePrimaryEditTextLength: (Int) -> Unit
    ) {
        primaryEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) showKeyboard(v)
            else hideKeyboard()
        }

        primaryEditText.doOnTextChanged { text, _, _, _ ->
            text?.run { observePrimaryEditTextLength(length) }
        }

    }

    private fun setupDropdownMenu(
        contents: List<String>,
        drawables: List<Int>,
        type: String,
        dropdownMenu: AutoCompleteTextView,
        layout: TextInputLayout
    ) {
        val contentAdapter = MaterialSpinnerAdapter(
            requireContext(),
            R.layout.dropdown_list_item,
            contents
        )

        dropdownMenu.setAdapter(contentAdapter)
        dropdownMenu.doOnTextChanged { content, _, _, _ ->
            // If device is rotated before any selection is made
            if (content.toString() == "") return@doOnTextChanged

            layout.setStartIconTintMode(PorterDuff.Mode.DST)
            layout.setStartIconDrawable(
                when (content.toString()) {
                    contents[0] -> drawables[0]
                    contents[1] -> drawables[1]
                    contents[2] -> drawables[2]
                    else -> throw IllegalArgumentException("Unknown $type: $content")
                }
            )
        }
    }

    private fun AddToDoViewModel.addObservers() {
        observeTitleEvent()
        observeTitleStillTooLongEvent()
        observeProceedToToDosFragmentEvent()
        observeDatabaseErrorEvent()
        observeInvalidFieldsErrorEvent()
        observeUnknownErrorEvent()
    }

    private fun AddToDoViewModel.observeTitleEvent() {
        titleEvent.observe(
            viewLifecycleOwner,
            EventObserver(
                { showTitleWarning(it) },
                { hideTitleWarning() })
        )
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
//        Snackbar.make(binding.addToDoConstraintLayout, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showDialog(
        @StringRes title: Int,
        @StringRes message: Int,
        @StringRes buttonText: Int
    ) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(buttonText) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun hideKeyboard() {
        (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
//            .hideSoftInputFromWindow(binding.addToDoConstraintLayout.windowToken, 0)
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

    private fun showTitleWarning(@StringRes message: Int) {
//        binding.titleLayout.error = getString(message)
    }

    private fun hideTitleWarning() {
//        binding.titleLayout.error = null
    }

    private fun setEnabled() {
        binding.run {
//            titleEditText.isEnabled = true
//            priorityDropdownMenu.isEnabled = true
//            statusDropdownMenu.isEnabled = true
            wasEnabled = true
        }
    }
}
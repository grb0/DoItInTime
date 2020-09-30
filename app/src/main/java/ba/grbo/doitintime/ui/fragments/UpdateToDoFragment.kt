package ba.grbo.doitintime.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import ba.grbo.doitintime.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateToDoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_add_or_update_to_do, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_to_do_menu, menu)
    }
}
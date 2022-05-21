package cat.oscarromero.randomuser.ui.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import cat.oscarromero.randomuser.R
import cat.oscarromero.randomuser.databinding.FragmentUsersBinding
import cat.oscarromero.randomuser.ui.viewmodel.UsersViewModel

class UsersFragment : Fragment(R.layout.fragment_users) {

    private val usersViewModel: UsersViewModel by activityViewModels()

    private var fragmentUsersBinding: FragmentUsersBinding? = null

    private val usersAdapter: UsersRecyclerViewAdapter by lazy {
        UsersRecyclerViewAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentUsersBinding.bind(view)
        fragmentUsersBinding = binding

        binding.apply { userRecyclerView.adapter = usersAdapter }

        usersViewModel.apply {
            users.observe(viewLifecycleOwner) {
                usersAdapter.apply {
                    items.addAll(it)
                    notifyDataSetChanged()
                }
            }

            isLoading.observe(viewLifecycleOwner) { show ->
                binding.loading.visibility = if (show) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }

            failure.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it.failureMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        fragmentUsersBinding = null
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance() = UsersFragment()
    }
}

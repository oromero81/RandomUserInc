package cat.oscarromero.randomuser.ui.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.oscarromero.randomuser.R
import cat.oscarromero.randomuser.databinding.FragmentUsersBinding
import cat.oscarromero.randomuser.ui.viewmodel.UsersViewModel

class UsersFragment : Fragment(R.layout.fragment_users) {

    private val usersViewModel: UsersViewModel by activityViewModels()

    private var fragmentUsersBinding: FragmentUsersBinding? = null

    private val usersAdapter: UsersRecyclerViewAdapter by lazy {
        UsersRecyclerViewAdapter(
            userDelete = { usersViewModel.deleteUser(it) },
            userSelected = { usersViewModel.userSelected(it) })
    }

    private var userScroll = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentUsersBinding.bind(view)
        fragmentUsersBinding = binding

        binding.apply {
            usersSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    usersAdapter.filter.filter(query)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    usersAdapter.filter.filter(newText)
                    return false
                }
            })

            userRecyclerView.layoutManager as LinearLayoutManager
            userRecyclerView.adapter = usersAdapter

            userRecyclerView.layoutManager?.let {
                if (it is LinearLayoutManager) {
                    userRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)

                            if (userScroll) {
                                val totalItemCount = it.itemCount;
                                val lastVisibleItem = it.findLastVisibleItemPosition();

                                if (totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
                                    usersViewModel.loadMoreUsers()
                                }
                                userScroll = false
                            }
                        }

                        override fun onScrollStateChanged(
                            recyclerView: RecyclerView,
                            newState: Int
                        ) {
                            super.onScrollStateChanged(recyclerView, newState)
                            userScroll = true
                        }
                    })
                }
            }
        }

        usersViewModel.apply {
            users.observe(viewLifecycleOwner) {
                usersAdapter.addData(it)
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

        private const val VISIBLE_THRESHOLD = 7
    }
}

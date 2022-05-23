package cat.oscarromero.randomuser.ui.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import cat.oscarromero.randomuser.R
import cat.oscarromero.randomuser.databinding.FragmentUserDetailsBinding
import cat.oscarromero.randomuser.extension.loadImageFromUrl
import cat.oscarromero.randomuser.ui.viewmodel.UsersViewModel

class UserDetailsFragment : Fragment(R.layout.fragment_user_details) {

    private val usersViewModel: UsersViewModel by activityViewModels()

    private var fragmentUserDetailsBinding: FragmentUserDetailsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentUserDetailsBinding.bind(view)
        fragmentUserDetailsBinding = binding

        usersViewModel.user.observe(viewLifecycleOwner) { userModel ->
            binding.apply {
                with(userModel) {
                    pictureImageView.loadImageFromUrl(
                        picture,
                        R.drawable.ic_user_flat,
                        R.drawable.ic_user_flat
                    )
                    nameTextView.text = name
                    emailTextView.text = email
                    phoneTextView.text = phone
                    locationTextView.text = location
                    genderTextView.text = gender
                    registerDateTextView.text = registeredDate
                }
            }
        }
        arguments?.let {
            val id = it.getString(USER_ID_ARG_PARAM, "")
            usersViewModel.loadUser(id)
        }
    }

    override fun onDestroyView() {
        fragmentUserDetailsBinding = null
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance(id: String) =
            UserDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_ID_ARG_PARAM, id)
                }
            }

        private const val USER_ID_ARG_PARAM = "user_position_arg_param"
    }
}
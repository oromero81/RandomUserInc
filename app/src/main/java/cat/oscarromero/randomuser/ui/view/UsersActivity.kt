package cat.oscarromero.randomuser.ui.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import cat.oscarromero.randomuser.R
import cat.oscarromero.randomuser.databinding.ActivityUsersBinding
import cat.oscarromero.randomuser.ui.viewmodel.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersActivity : AppCompatActivity() {

    private val usersViewModel: UsersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usersViewModel.loadUsers()
        supportFragmentManager.commit { add(R.id.container, UsersFragment.newInstance(), null) }

        usersViewModel.userSelected.observe(this) {
            supportFragmentManager.commit {
                replace(R.id.container, UserDetailsFragment.newInstance(it), null)
                    .addToBackStack(UserDetailsFragment::class.simpleName)
            }
        }
    }
}

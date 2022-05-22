package cat.oscarromero.randomuser.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.oscarromero.randomuser.R
import cat.oscarromero.randomuser.databinding.UserCellBinding
import cat.oscarromero.randomuser.extension.loadImageFromUrl
import cat.oscarromero.randomuser.ui.model.UserModel
import kotlin.properties.Delegates

class UsersRecyclerViewAdapter(private val userDelete: (String) -> Unit) :
    RecyclerView.Adapter<UsersRecyclerViewAdapter.UserViewHolder>() {

    val items: MutableList<UserModel> by Delegates.observable(mutableListOf()) { _, _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(
            UserCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ) { id ->
            val userToDelete = items.find { it.id == id }
            val index = items.indexOf(userToDelete)
            items.removeAt(index)
            notifyItemRemoved(index)

            userDelete(id)
        }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.render(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class UserViewHolder(binding: UserCellBinding, private val delete: (String) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        private val nameTextview: TextView = binding.nameTextView
        private val emailTextView: TextView = binding.emailTextView
        private val phoneTextView: TextView = binding.phoneTextView
        private val pictureImageView: ImageView = binding.pictureImageView
        private val deleteButton: ImageButton = binding.deleteButton

        fun render(user: UserModel) {
            with(user) {
                nameTextview.text = name
                emailTextView.text = email
                phoneTextView.text = phone
                pictureImageView.loadImageFromUrl(
                    picture,
                    R.drawable.ic_user_flat,
                    R.drawable.ic_user_flat
                )
                deleteButton.setOnClickListener { delete(user.id) }
            }
        }
    }
}

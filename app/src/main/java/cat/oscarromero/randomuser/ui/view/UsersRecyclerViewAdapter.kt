package cat.oscarromero.randomuser.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.oscarromero.randomuser.R
import cat.oscarromero.randomuser.databinding.UserCellBinding
import cat.oscarromero.randomuser.extension.loadImageFromUrl
import cat.oscarromero.randomuser.ui.model.UserModel
import kotlin.properties.Delegates

class UsersRecyclerViewAdapter : RecyclerView.Adapter<UsersRecyclerViewAdapter.UserViewHolder>() {

    val items: MutableList<UserModel> by Delegates.observable(mutableListOf()) { _, _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(
            UserCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.render(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class UserViewHolder(binding: UserCellBinding) : RecyclerView.ViewHolder(binding.root) {
        private val nameTextview: TextView = binding.nameTextView
        private val emailTextView: TextView = binding.emailTextView
        private val phoneTextView: TextView = binding.phoneTextView
        private val pictureImageView: ImageView = binding.pictureImageView

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
            }
        }
    }
}

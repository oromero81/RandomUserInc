package cat.oscarromero.randomuser.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import cat.oscarromero.randomuser.R
import cat.oscarromero.randomuser.databinding.UserCellBinding
import cat.oscarromero.randomuser.extension.loadImageFromUrl
import cat.oscarromero.randomuser.ui.model.UserModel

class UsersRecyclerViewAdapter(
    private val userDelete: (String) -> Unit,
    private val userSelected: (String) -> Unit
) :
    RecyclerView.Adapter<UsersRecyclerViewAdapter.UserViewHolder>(), Filterable {

    private val items: MutableList<UserModel> = mutableListOf()
    private val itemsFiltered: MutableList<UserModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(
            UserCellBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            { id ->
                val userToDelete = itemsFiltered.find { it.id == id }
                val index = itemsFiltered.indexOf(userToDelete)
                itemsFiltered.removeAt(index)
                notifyItemRemoved(index)

                userDelete(id)
            },
            { userSelected(it) }
        )

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.render(itemsFiltered[position])
    }

    override fun getItemCount(): Int = itemsFiltered.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                val listFiltered: MutableList<UserModel> = mutableListOf()

                if (charString.isEmpty()) {
                    listFiltered.addAll(items)
                } else {
                    items
                        .filter {
                            (it.name.contains(constraint!!)) or (it.email.contains(constraint))

                        }
                        .forEach { listFiltered.add(it) }

                }
                return FilterResults().apply { values = listFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                itemsFiltered.clear()
                itemsFiltered.addAll(results?.values as Collection<UserModel>)
                notifyDataSetChanged()
            }
        }
    }

    fun addData(data: List<UserModel>) {
        items.clear()
        items.addAll(data)

        itemsFiltered.clear()
        itemsFiltered.addAll(data)

        notifyDataSetChanged()
    }

    inner class UserViewHolder(
        binding: UserCellBinding,
        private val delete: (String) -> Unit,
        private val userSelected: (String) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private val nameTextview: TextView = binding.nameTextView
        private val emailTextView: TextView = binding.emailTextView
        private val phoneTextView: TextView = binding.phoneTextView
        private val pictureImageView: ImageView = binding.pictureImageView
        private val deleteButton: ImageButton = binding.deleteButton
        private val cell: View = binding.root

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
                cell.setOnClickListener { userSelected(user.id) }
            }
        }
    }
}

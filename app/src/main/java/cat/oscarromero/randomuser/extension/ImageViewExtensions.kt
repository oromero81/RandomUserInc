package cat.oscarromero.randomuser.extension

import android.webkit.URLUtil
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.squareup.picasso.Picasso

fun ImageView.loadImageFromUrl(
    url: String,
    @DrawableRes errorRes: Int? = null,
    @DrawableRes placeHolderRes: Int? = null
) {
    if (URLUtil.isValidUrl(url)) {
        Picasso
            .get()
            .load(url)
            .apply { errorRes?.let { this.error(it) } }
            .apply { placeHolderRes?.let { this.placeholder(it) } }
            .into(this)
    } else {
        if (placeHolderRes != null) {
            setImageResource(placeHolderRes)
        } else if (errorRes != null) {
            setImageResource(errorRes)
        }
    }
}

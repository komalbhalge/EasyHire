package android.kodroid.com.easyhiring.adapters;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.kodroid.com.easyhiring.R;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class GlideBindingAdapter {
    @BindingAdapter("imageUrl")
    public static void setImageResource(ImageView imageView, String res){
        Context context = imageView.getContext();

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.default_person)
                .error(R.drawable.default_person);

        Glide.with(context).setDefaultRequestOptions(options).load(res).into(imageView);

    }
}

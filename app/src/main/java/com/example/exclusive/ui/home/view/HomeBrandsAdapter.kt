import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.R

import com.example.exclusive.databinding.RowBrandBinding
import com.example.exclusive.model.Brand
import com.example.exclusive.ui.home.view.OnItemClickListener
import com.squareup.picasso.Picasso

class HomeBrandsAdapter(
    private var brands: List<Brand>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<HomeBrandsAdapter.BrandViewHolder>() {

    inner class BrandViewHolder(private val binding: RowBrandBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(brands[position])
                }
            }
        }

        fun bind(brand: Brand) {
            binding.tvBrandName.text = brand.name
            if (!brand.imageUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(brand.imageUrl)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(binding.ivBrandImage)
                Log.i("TAG", "bind: ${brand.imageUrl}")

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val binding = RowBrandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BrandViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        holder.bind(brands[position])
    }

    override fun getItemCount(): Int = brands.size

    fun updateBrands(newBrands: List<Brand>) {
        brands = newBrands
        notifyDataSetChanged()
    }
}

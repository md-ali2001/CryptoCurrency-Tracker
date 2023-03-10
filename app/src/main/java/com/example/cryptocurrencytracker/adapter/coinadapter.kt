package com.example.cryptocurrencytracker.adapter

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocurrencytracker.R
import com.example.cryptocurrencytracker.common.common
import com.example.cryptocurrencytracker.`interface`.Iloadmore
import com.example.cryptocurrencytracker.model.coinmodel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.coinlayout.view.*


class CoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var coinIcon = itemView.coinIcon
    var coinSymbol = itemView.coinSymbol
    var coinName = itemView.coinName
    var coinPrice = itemView.priceUsd
    var oneHourChange = itemView.oneHour
    var twentyFourChange = itemView.twentyFourHour
    var sevenDayChange = itemView.sevenDay
}

class CoinAdapter(recyclerView: RecyclerView, internal var activity: Activity, var items: List<coinmodel>) : RecyclerView.Adapter<CoinViewHolder>() {

    internal var loadMore: Iloadmore? = null
    var isLoading: Boolean = false
    var visibleThreshold = 5
    var lastVisibleItem: Int = 0
    var totalItemCount: Int = 0

    init {
        val linearLayout = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = linearLayout.itemCount
                lastVisibleItem = linearLayout.findLastVisibleItemPosition()
                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    if (loadMore != null)
                        loadMore!!.onloadmore()
                    isLoading = true
                }
            }
        })
    }

    fun setLoadMore(loadMore: Iloadmore) {
        this.loadMore = loadMore
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val view = LayoutInflater.from(activity)
            .inflate(R.layout.coinlayout, parent, false)
        return CoinViewHolder(view)
    }



    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val coinModel = items.get(position)

        val item = holder as CoinViewHolder

        item.coinName.text = coinModel.name
        item.coinSymbol.text = coinModel.symbol
        item.coinPrice.text = coinModel.price_usd
        item.oneHourChange.text = coinModel.percent_change_1h + "%"
        item.twentyFourChange.text = coinModel.percent_change_24h + "%"
        item.sevenDayChange.text = coinModel.percent_change_7d + "%"

        Picasso.with(activity.baseContext)
            .load(StringBuilder(common.imageUrl)
                .append(coinModel.symbol!!.toLowerCase())
                .append(".png")
                .toString())
            .into(item.coinIcon)

        //Set color
        item.oneHourChange.setTextColor(if (coinModel.percent_change_1h!!.contains("-"))
            Color.parseColor("#FF0000")
        else
            Color.parseColor("#32CD32")
        )

        item.twentyFourChange.setTextColor(if (coinModel.percent_change_24h!!.contains("-"))
            Color.parseColor("#FF0000")
        else
            Color.parseColor("#32CD32")
        )

        item.sevenDayChange.setTextColor(if (coinModel.percent_change_7d!!.contains("-"))
            Color.parseColor("#FF0000")
        else
            Color.parseColor("#32CD32")
        )

    }

    fun  setLoaded(){
        isLoading = false
    }

    fun updateData(coinModels: List<coinmodel>)
    {
        this.items = coinModels
        notifyDataSetChanged()
    }

}
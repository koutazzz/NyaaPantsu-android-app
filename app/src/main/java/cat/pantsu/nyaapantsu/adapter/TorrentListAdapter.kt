package cat.pantsu.nyaapantsu.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.mvp.model.TorrentListResponse
import cat.pantsu.nyaapantsu.mvp.model.TorrentModel
import cat.pantsu.nyaapantsu.ui.activity.TorrentActivity
import cat.pantsu.nyaapantsu.util.download
import cat.pantsu.nyaapantsu.util.formatDate
import kotlinx.android.synthetic.main.torrent_item.view.*


class TorrentListAdapter(var context: Context, private var torrentList: TorrentListResponse<TorrentModel>) : RecyclerView.Adapter<TorrentListAdapter.TorrentListViewHolder>() {

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TorrentListViewHolder?, position: Int) {
        if (holder == null) return
        val item = torrentList.torrents[position]

        holder.itemView.name.text = item.name
        holder.itemView.uploader.text = item.uploaderName
        holder.itemView.stats.text = "S: " + item.seeders + " L: " + item.leechers
        holder.itemView.date.text = item.date.formatDate()

        holder.itemView.download.setOnClickListener {
            when {
                !TextUtils.isEmpty(item.torrent) -> download(context as Activity, holder.itemView, item.torrent, item.name)
                else -> Toast.makeText(context, R.string.torrent_not_available, Toast.LENGTH_SHORT).show()
            }
        }

        holder.itemView.copy.setOnClickListener {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(item.name, item.magnet)
            clipboard.primaryClip = clipData
            Toast.makeText(context, R.string.magnet_copied, Toast.LENGTH_SHORT).show()
        }

        holder.itemView.cardview.setOnClickListener {
            context.startActivity(TorrentActivity.createIntent(context, item.id, item.name))
        }

        when (item.status) {
            2 -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.itemView.statusLine.setBackgroundColor(context.resources.getColor(R.color.colorRemake, null))
                } else {
                    holder.itemView.statusLine.setBackgroundColor(context.resources.getColor(R.color.colorRemake))
                }

            }
            3 -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.itemView.statusLine.setBackgroundColor(context.resources.getColor(R.color.colorTrusted, null))
                } else {
                    holder.itemView.statusLine.setBackgroundColor(context.resources.getColor(R.color.colorTrusted))
                }

            }
            4 -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.itemView.statusLine.setBackgroundColor(context.resources.getColor(R.color.colorAPlus, null))
                } else {
                    holder.itemView.statusLine.setBackgroundColor(context.resources.getColor(R.color.colorAPlus))
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TorrentListViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        return TorrentListViewHolder(layoutInflater.inflate(R.layout.torrent_item, parent, false))

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return torrentList.torrents.size
    }

    class TorrentListViewHolder(view: View) : RecyclerView.ViewHolder(view)

}

package cat.pantsu.nyaapantsu.ui.fragment


import android.app.Fragment
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.adapter.TorrentListAdapter
import cat.pantsu.nyaapantsu.helper.getRecentPlaylistAsArray
import cat.pantsu.nyaapantsu.model.RecentlyPlayed
import cat.pantsu.nyaapantsu.model.Torrent
import cat.pantsu.nyaapantsu.ui.activity.TorrentActivity
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.fragment_recent.*
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import org.json.JSONArray
import java.util.*


class RecentFragment : Fragment() {
    var torrents: JSONArray = JSONArray()

    companion object {
        fun newInstance(): RecentFragment {
            val fragment = RecentFragment()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val closeButton = activity.toolbar.find<ImageButton>(R.id.buttonClose)
        closeButton.visibility = View.GONE
        activity.fab.visibility = View.GONE
        activity.title = getString(R.string.recent)
        torrents = getRecentPlaylistAsArray()
        return inflater!!.inflate(R.layout.fragment_recent, container, false)
    }
    fun parseTorrents() {
        val length = (torrents.length()-1)
        val torrentList = (0..length).mapTo(LinkedList<Torrent>()) { Torrent(torrents.getJSONObject(it)) }
        torrentlist.adapter = TorrentListAdapter(activity, torrentList = torrentList)
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (torrents.length() > 0) {
            parseTorrents()
        }
        torrentlist.setOnItemClickListener { _, _, i, _ ->
            startActivity<TorrentActivity>("position" to i, "type" to "recent")
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recent_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear_recent -> {
                RecentlyPlayed.torrents = ""
                val recentFragment = RecentFragment.newInstance() // refresh placeholder if we switch to recycler view I will use better solution
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, recentFragment as Fragment)
                        .commit()
            }
        }
        return false
    }
}

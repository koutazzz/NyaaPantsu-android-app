package cat.pantsu.nyaapantsu.ui.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.adapter.TorrentListAdapter
import cat.pantsu.nyaapantsu.base.BaseActivity
import cat.pantsu.nyaapantsu.mvp.model.TorrentListResponse
import cat.pantsu.nyaapantsu.mvp.model.TorrentModel
import cat.pantsu.nyaapantsu.mvp.presenter.SearchTorrentListPresenter
import cat.pantsu.nyaapantsu.mvp.view.SearchTorrentListView
import kotlinx.android.synthetic.main.fragment_search_torrent_list.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.contentView
import javax.inject.Inject


class SearchActivity : BaseActivity(), SearchTorrentListView {

    @Inject
    lateinit var presenter: SearchTorrentListPresenter

    lateinit var adapter: TorrentListAdapter

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchView.clearFocus()
                    presenter.subscribe(this@SearchActivity)
                    presenter.loadData(null, query, null, null, null, null, null, null, null)
                }
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                return false
            }
        })

        return true
    }

    override fun onItemsLoaded(items: TorrentListResponse<TorrentModel>) {
        recyclerView = storrentlist
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TorrentListAdapter(this, items)
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter

    }

    override fun onError(e: Throwable?) {
        Log.e("searchtorrentlist", e.toString())
        Snackbar.make(contentView!!, e.toString(), Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }
}
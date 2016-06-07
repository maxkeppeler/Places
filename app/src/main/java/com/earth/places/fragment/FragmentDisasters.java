package com.earth.places.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.earth.places.R;
import com.earth.places.adapters.DisastersAdapter;
import com.earth.places.models.Disasters;
import com.earth.places.threads.DisastersJSON;
import com.earth.places.utilities.Utils;

public class FragmentDisasters extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "FragmentDisasters";
    public static SwipeRefreshLayout refreshLayout;
    public static RecyclerView recyclerView;
    private static DisastersAdapter disastersAdapter;
    private static Activity context;

    public static void updateLayout() {

        if (Disasters.getDisastersList() != null && Disasters.getDisastersList().size() > 0) {

            context.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    disastersAdapter = new DisastersAdapter(context, new DisastersAdapter.ClickListener() {

                        @Override
                        public void onClick(DisastersAdapter.SinsViewHolder v, final int position) {

                            Utils.openChromeTab(context, Disasters.getDisastersList().get(position).getUrl(), 0);

                        }
                    });

                    disastersAdapter.setData(Disasters.getDisastersList());
                    recyclerView.setAdapter(disastersAdapter);

                }
            });
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_nature_disasters, null);

        context = getActivity();

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.disastersRefresh);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.disastersRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(disastersAdapter);

        // If list is null, load disasters, show recyclerView and update layout
        if (Disasters.getDisastersList() == null)
            loadDisastersList(context);
        else {
            recyclerView.setVisibility(View.VISIBLE);
            updateLayout();
        }

        return view;
    }

    @Override
    public void onRefresh() {
        disastersAdapter.notifyDataSetChanged();
        recyclerView.setVisibility(View.INVISIBLE);
        loadDisastersList(context);
    }

    public static void loadDisastersList(Activity context) {

        new DisastersJSON(context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.actions_places, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}

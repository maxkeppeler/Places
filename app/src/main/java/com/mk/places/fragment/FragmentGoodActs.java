package com.mk.places.fragment;

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
import android.view.View;
import android.view.ViewGroup;

import com.mk.places.R;
import com.mk.places.adapters.GoodActsAdapter;
import com.mk.places.models.Disasters;
import com.mk.places.models.GoodActs;
import com.mk.places.threads.GoodActsJSON;
import com.mk.places.utilities.Utils;

public class FragmentGoodActs extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "FragmentDisasters";
    public static SwipeRefreshLayout refreshLayout;
    public static RecyclerView recyclerView;
    private static GoodActsAdapter sinsAdapter;
    private static Activity context;

    public static void updateLayout() {

        if (GoodActs.getGoodActsList() != null && GoodActs.getGoodActsList().size() > 0) {

            context.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    sinsAdapter = new GoodActsAdapter(context, new GoodActsAdapter.ClickListener() {

                        @Override
                        public void onClick(GoodActsAdapter.SinsViewHolder v, final int position) {

                            Utils.openChromeTab(context, Disasters.getDisastersList().get(position).getUrl(), 0);

                        }
                    });

                    sinsAdapter.setData(GoodActs.getGoodActsList());
                    recyclerView.setAdapter(sinsAdapter);

                }
            });
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_nature_good_acts, null);

        context = getActivity();

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.disastersRefresh);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.disastersRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(sinsAdapter);

        FragmentGoodActs.loadSinsList(context);

        return view;
    }

    @Override
    public void onRefresh() {
        sinsAdapter.notifyDataSetChanged();
        recyclerView.setVisibility(View.INVISIBLE);
        loadSinsList(context);
    }

    public static void loadSinsList(Activity context) {

        new GoodActsJSON(context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

}

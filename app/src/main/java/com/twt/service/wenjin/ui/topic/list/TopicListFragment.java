package com.twt.service.wenjin.ui.topic.list;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.twt.service.wenjin.R;
import com.twt.service.wenjin.bean.Topic;
import com.twt.service.wenjin.support.LogHelper;
import com.twt.service.wenjin.support.ResourceHelper;
import com.twt.service.wenjin.ui.BaseFragment;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TopicListFragment extends BaseFragment implements TopicListView {

    private static final String LOG_TAG = TopicListFragment.class.getSimpleName();

    public static final String PARAM_POSITION = "position";

    @Inject
    TopicListPresenter mPresenter;

    @InjectView(R.id.swipe_refresh_layout_topic)
    SwipeRefreshLayout mRefreshLayout;
    @InjectView(R.id.recycler_view_topic)
    RecyclerView mRecyclerView;

    private TopicListAdapter mAdapter;
    private int position;

    public TopicListFragment() {
    }

    public static TopicListFragment getInstance(int position) {
        TopicListFragment fragment = new TopicListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(PARAM_POSITION);
        LogHelper.v(LOG_TAG, "onCreate, position: " + position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_topic_list, container, false);
        ButterKnife.inject(this, rootView);

        mRefreshLayout.setColorSchemeColors(ResourceHelper.getColor(R.color.color_primary));
        mAdapter = new TopicListAdapter(getActivity());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    protected List<Object> getModules() {
        return Arrays.<Object>asList(new TopicListModule(this));
    }

    @Override
    public void onStart() {
        super.onStart();
        LogHelper.v(LOG_TAG, "onStart, position: " + position);
        mPresenter.loadTopics(position);
    }

    @Override
    public void updateTopics(Topic[] topics) {
        LogHelper.d(LOG_TAG, "topics length: " + topics.length);
        mAdapter.updateTopics(topics);
    }

    @Override
    public void startRefresh() {
        if (!mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void stopRefresh() {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void toastMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
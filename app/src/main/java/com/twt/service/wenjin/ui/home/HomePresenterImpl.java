package com.twt.service.wenjin.ui.home;

import android.view.View;

import com.twt.service.wenjin.R;
import com.twt.service.wenjin.bean.HomeItem;
import com.twt.service.wenjin.bean.HomeResponse;
import com.twt.service.wenjin.interactor.HomeInteractor;
import com.twt.service.wenjin.interactor.HomeInteractorImpl;
import com.twt.service.wenjin.support.LogHelper;
import com.twt.service.wenjin.support.ResourceHelper;

import java.util.ArrayList;

/**
 * Created by M on 2015/3/22.
 */
public class HomePresenterImpl implements HomePresenter, OnGetItemsCallback, OnPublishCallback {

    private static final String LOG_TAG = HomeInteractorImpl.class.getSimpleName();

    private HomeView mHomeView;
    private HomeInteractor mHomeInteractor;

    private int mItemsPerPage = 20;
    private int mPage = 0;
    private boolean isLoadingMore = false;

    public HomePresenterImpl(HomeView homeView, HomeInteractor homeInteractor) {
        this.mHomeView = homeView;
        this.mHomeInteractor = homeInteractor;
    }

    @Override
    public void refreshHomeItems() {
        mPage = 0;
        mHomeView.showRefresh();
        mHomeView.useLoadMoreFooter();
        mHomeInteractor.getHomeItems(mItemsPerPage, mPage, this);
    }

    @Override
    public void loadMoreHomeItems() {
        mPage += 1;
        isLoadingMore = true;
        mHomeInteractor.getHomeItems(mItemsPerPage, mPage, this);
    }

    @Override
    public void publishQuestion(String title, String content, String attachKey, String topics) {
        mHomeInteractor.publishQuestion(title, content, attachKey, topics, this);
    }

    @Override
    public void onItemClicked(View v, int position) {
        switch (v.getId()) {
            case R.id.iv_home_item_avatar:
                mHomeView.toastMessage("position " + position + " avatar clicked!");
                break;
            case R.id.tv_home_item_username:
                mHomeView.toastMessage("position " + position + " username clicked!");
                break;
            case R.id.tv_home_item_title:
                mHomeView.startQuestionActivity(position);
                break;
            case R.id.tv_home_item_content:
                mHomeView.startAnswerActivity(position);
                break;
            case R.id.iv_home_item_agree:
                mHomeView.toastMessage("position " + position + " agreed");
                break;
        }
    }

    @Override
    public void onSuccess(HomeResponse homeResponse) {
        mHomeView.hideRefresh();

        if (homeResponse.total_rows == 0) {
            mHomeView.toastMessage(ResourceHelper.getString(R.string.no_more_infomation));
            mHomeView.hideLoadMoreFooter();
            return;
        }
        if (isLoadingMore) {
            mHomeView.loadMoreItems((ArrayList<HomeItem>) homeResponse.rows);
            isLoadingMore = false;
        } else {
            mHomeView.refreshItems((ArrayList<HomeItem>) homeResponse.rows);
        }
    }

    @Override
    public void onFailure(String errorString) {
        mHomeView.hideRefresh();
        mHomeView.toastMessage(errorString);
    }

    @Override
    public void publishSuccess(int questionId) {
    }

    @Override
    public void publishFailure(String errorMsg) {
        mHomeView.toastMessage(errorMsg);
    }
}

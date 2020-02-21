package com.example.zhang.thinmusic.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhang.thinmusic.R;
import com.example.zhang.thinmusic.model.NeteaseListInfo;
import com.example.zhang.thinmusic.utils.Bind;
import com.example.zhang.thinmusic.utils.ViewBinder;
import com.example.zhang.thinmusic.widget.CanScrollHorizonView;

import java.util.List;

/*网易云歌单适配器*/
public class NeteaseListAdapter extends BaseAdapter {
    private Context mContext;
    //private List<NeteaseListInfo> mdata;
    private List<List<NeteaseListInfo>> mdata;

    private CanScrollHorizonViewAdapter canScrollHorizonViewAdapter;
    private static final int TYPE_PROFILE = 0;
    private static final int TYPE_MUSICLIST = 1;

    public NeteaseListAdapter(List<List<NeteaseListInfo>> data) {
        mdata = data;
    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public Object getItem(int position) {
        return mdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public boolean isEnable(int postion) {
        return getItemViewType(postion) == TYPE_MUSICLIST;
    }

/*    @Override
    public int getItemViewType(int position){
        if(mdata.get(position).getType().equals("#")){
            return TYPE_PROFILE;
        }else{
            return TYPE_MUSICLIST;
        }
    }

    @Override
    public int getViewTypeCount(){return 2;}*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mContext = parent.getContext();
        List<NeteaseListInfo> neteaseListInfoList = mdata.get(position);
        CanScrollHorizonViewAdapter adapter= new CanScrollHorizonViewAdapter(mContext,neteaseListInfoList);
        //NeteaseListInfo neteaseListInfo = mdata.get(position);

        ViewHolderMusicList holderMusicList;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.neteaselistinfo_horizonview_holder, parent, false);

            holderMusicList = new ViewHolderMusicList(convertView);//把每个item的数据放到viewholder中
            convertView.setTag(holderMusicList);
        } else {
            holderMusicList = (ViewHolderMusicList) convertView.getTag();

        }
        Log.d("response", "getView:listview "+position+" "+neteaseListInfoList.get(position).getTitle());

/*        为scrollHorizonView增加滑动回调*/
        holderMusicList.scrollHorizonView.setmCurrentCardChangeListener(new CanScrollHorizonView.CurrentCardChangeListener() {
            @Override
            public void onCurrentCardChangeListener(int position, View viewIndicater) {

            }
        });
        holderMusicList.scrollHorizonView.setOnItemClickListener(new CanScrollHorizonView.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
        });
        holderMusicList.netease_songlist_divider.setVisibility(isShowDivider(position) ? View.VISIBLE : View.INVISIBLE);
        holderMusicList.scrollHorizonView.initDatas(adapter,neteaseListInfoList.size());
        return convertView;
    }

    private boolean isShowDivider(int position) {
        return position != mdata.size() - 1;
    }

   /* private void getMusicListInfo(final List<NeteaseListInfo> neteaseListInfolist, final ViewHolderMusicList holderMusicList) {

        for ( int i = 0; i < neteaseListInfolist.size(); i++) {
            if (neteaseListInfolist.get(i).getCoverUrl()== null) {
                holderMusicList.cardviews.get(i).Cover.setImageResource(R.drawable.default_cover);
                holderMusicList.cardviews.get(i).title.setText("loading.......please wait");
                int position =i;
                HttpClient2Netease.getNeteaseMusicListInfo(neteaseListInfolist.get(i).getId(), position, new HttpCallback<NeteaseMusicList>() {
                    @Override
                    public void onSuccess(NeteaseMusicList response) {
                        if (response == null || response.getPlaylist() == null) {
                            return;
                        }
                        if (!neteaseListInfolist.get(position).getTitle().equals(holderMusicList.cardviews.get(position).title.getTag())) {
                            return;
                        }
                        parse(response, neteaseListInfolist.get(position));
                        setData(neteaseListInfolist.get(position),position, holderMusicList);
                    }

                    @Override
                    public void onFail(Exception e) {

                    }
                });
            } else {
                holderMusicList.cardviews.get(i).title.setTag(null);
                setData(neteaseListInfolist.get(i),i, holderMusicList);
            }
        }
    }
    private void parse(NeteaseMusicList response, NeteaseListInfo neteaseListInfo) {
        List<NeteaseMusic> neteaseMusics = response.getTracks();
        String title = response.getPlaylist().getName();
        neteaseListInfo.setCoverUrl(response.getPlaylist().getCoverImgUrl());
        if (title != null) {
            neteaseListInfo.setTitle(title);
        }
    }

    private void setData(NeteaseListInfo neteaseListInfo, int position,ViewHolderMusicList holderMusicList) {
        holderMusicList.scrollHorizonView.get(position).title.setText(neteaseListInfo.getTitle());
        Glide.with(mContext)
                .load(neteaseListInfo.getCoverUrl())
                .placeholder(R.drawable.default_cover)
                .error(R.drawable.default_cover)
                .into(holderMusicList.cardviews.get(position).Cover);
    }*/


    private static class ViewHolderProfile {
        @Bind(R.id.profile)
        private TextView profile;

        public ViewHolderProfile(View view) {
            ViewBinder.bind(this, view);
        }

    }

    private static class ViewHolderMusicList {


        @Bind(R.id.netease_songlist_divider)
        private View netease_songlist_divider;

        @Bind(R.id.scrollcardview)
        private CanScrollHorizonView scrollHorizonView;
        //private List<cardview> cardviews;


        public ViewHolderMusicList(View view) {
            ViewBinder.bind(this, view);
        }
    }

    private class cardview {
        @Bind(R.id.netease_songlist_cover)
        private ImageView Cover;
        @Bind(R.id.netease_songlist_title)
        private TextView title;

    }
}

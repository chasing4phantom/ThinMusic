package com.example.zhang.thinmusic.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhang.thinmusic.R;
import com.example.zhang.thinmusic.http.HttpCallback;
import com.example.zhang.thinmusic.http.HttpClient2Netease;
import com.example.zhang.thinmusic.model.NeteaseListInfo;
import com.example.zhang.thinmusic.model.NeteaseMusicList;
import com.example.zhang.thinmusic.utils.Bind;
import com.example.zhang.thinmusic.utils.ViewBinder;

import java.util.List;


public class CanScrollHorizonViewAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<NeteaseListInfo> mdata;
    private final int OFFSET=0;

    public CanScrollHorizonViewAdapter(Context context, List<NeteaseListInfo> data){
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        mdata = data;

    }

    public int getCount(){return mdata.size();}

    public Object  getItem(int position){return  mdata.get(position);}

    public View getView(int position, View convertView, ViewGroup parent){


        CardViewHolder cardViewHolder;
        if(convertView==null){

            convertView = inflater.inflate(R.layout.neteaselistinfo_horizonview_item,parent,false);
            cardViewHolder = new CardViewHolder(convertView);
            convertView.setTag(cardViewHolder);
        }else{
            cardViewHolder = (CardViewHolder) convertView.getTag();
        }

        NeteaseListInfo neteaseListInfo = mdata.get(position);
            Log.d("response", "getView: "+position+" "+mdata.get(position).getTitle()+" "+mdata.get(position).getId()+"childcount"+parent.getChildCount());
        getMusicListInfo(neteaseListInfo,cardViewHolder);

        return convertView;
    }

    private void getMusicListInfo(final NeteaseListInfo neteaseListInfo,final CardViewHolder holderMusicList){
        if(neteaseListInfo.getCoverUrl() == null){
            holderMusicList.Cover.setImageResource(R.drawable.default_cover);
            holderMusicList.title.setText(neteaseListInfo.getTitle());
             HttpClient2Netease.getNeteaseMusicListInfo(neteaseListInfo.getId(), new HttpCallback<NeteaseMusicList>() {
                @Override
                public void onSuccess(NeteaseMusicList response) {
                    if(response == null|| response.getPlaylist() == null){
                        Log.d("response", "onSuccess:response == null|| response.getPlaylist() == null ");
                        return;
                    }
/*                    if(!neteaseListInfo.getTitle().equals(holderMusicList.title.getTag())){
                        Log.d("response", "onSuccesse:equals "+neteaseListInfo.getTitle()+" "+holderMusicList.title.getTag());
                        return;
                    }*/
                    Log.d("response:onsuccess", "onSuccess: ready2parse");
                    parse(response,neteaseListInfo);
                    setData(neteaseListInfo,holderMusicList);
                }

                @Override
                public void onFail(Exception e) {
                    Log.d("response:onfail", "onFail: ");
                    e.printStackTrace();
                }
            });
        }else{
            holderMusicList.title.setTag(null);
            setData(neteaseListInfo,holderMusicList);
        }
    }
    private void parse(NeteaseMusicList response,NeteaseListInfo neteaseListInfo){
        //List<NeteaseMusic> neteaseMusics = response.getTracks();
        String title = response.getPlaylist().getName();
        neteaseListInfo.setCoverUrl(response.getPlaylist().getCoverImgUrl());
        Log.d("neteaselistinfo", "parse:title+coverurl "+title+" "+neteaseListInfo.getCoverUrl());
        if(title!=null){
            neteaseListInfo.setTitle(title);
        }
    }

    private void setData(NeteaseListInfo neteaseListInfo, CardViewHolder holderMusicList){
        holderMusicList.title.setText(neteaseListInfo.getTitle());
        Log.d("neteaselistinfo", "setData: "+holderMusicList.title.getText());
        Glide.with(mContext)
                .load(neteaseListInfo.getCoverUrl())
                .placeholder(R.drawable.default_cover)
                .error(R.drawable.default_cover)
                .into(holderMusicList.Cover);
    }
    private class CardViewHolder{

        @Bind(R.id.netease_songlist_cover)
        private ImageView Cover;
        @Bind(R.id.netease_songlist_title)
        private TextView title;

        public CardViewHolder(View view){
            ViewBinder.bind(this,view);
        }
    }
}

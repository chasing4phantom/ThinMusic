package com.example.zhang.thinmusic.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.example.zhang.thinmusic.BuildConfig;
import com.example.zhang.thinmusic.R;

/**
 * Created by 26292 on 2018/4/27.
 */

public class AboutActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_about);

        getFragmentManager().beginTransaction().replace(R.id.fragment_container,new AboutFragment()).commit();

    }

    public static class AboutFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener{
        private Preference Version;
        private Preference github;

        @Override
        public void onCreate(Bundle saveInstanceState){
            super.onCreate(saveInstanceState);
            addPreferencesFromResource(R.xml.preference_about);

            Version = findPreference("version");
            github = findPreference("github");

            Version.setSummary("v" + BuildConfig.VERSION_NAME);
            setListener();
        }

        private void setListener(){
            github.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference){
            if(preference == github){
                openUrl(preference.getSummary().toString());
                return true;
            }
            else
                return false;
        }

        private void openUrl(String url){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
    }

}

package eu.faircode.email;

/*
    This file is part of FairEmail.

    FairEmail is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    NetGuard is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with NetGuard.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2018 by Marcel Bokhorst (M66B)
*/

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

abstract class ActivityBase extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(Helper.TAG, "Create " + this.getClass().getName() + " version=" + BuildConfig.VERSION_NAME);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean pro = prefs.getBoolean("pro", false);
        String theme = (pro ? prefs.getString("theme", "light") : "light");
        setTheme("light".equals(theme) ? R.style.AppThemeLight : R.style.AppThemeDark);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        Log.i(Helper.TAG, "Resume " + this.getClass().getName());
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(Helper.TAG, "Pause " + this.getClass().getName());
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i(Helper.TAG, "Config " + this.getClass().getName());
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        Log.i(Helper.TAG, "Destroy " + this.getClass().getName());
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        Log.i(Helper.TAG, "Preference " + key + "=" + prefs.getAll().get(key));
        if ("theme".equals(key) || "debug".equals(key)) {
            finish();
            startActivity(getIntent());
        }
    }

    private List<IBackPressedListener> backPressedListeners = new ArrayList<>();

    public void addBackPressedListener(IBackPressedListener listener) {
        backPressedListeners.add(listener);
    }

    @Override
    public void onBackPressed() {
        for (IBackPressedListener listener : backPressedListeners)
            if (listener.onBackPressed())
                return;
        super.onBackPressed();
    }

    public interface IBackPressedListener {
        boolean onBackPressed();
    }
}

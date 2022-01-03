/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.homepage;

import static com.android.settings.search.actionbar.SearchMenuController.NEED_SEARCH_ICON_IN_ACTION_BAR;
import static com.android.settingslib.search.SearchIndexable.MOBILE;

import android.app.settings.SettingsEnums;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import android.util.Log;

import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.support.SupportPreferenceController;
import com.android.settingslib.core.instrumentation.Instrumentable;
import com.android.settingslib.search.SearchIndexable;

@SearchIndexable(forTarget = MOBILE)
public class TopLevelSettings extends DashboardFragment implements
        PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private static final String TAG = "TopLevelSettings";

    public static final String[] CHANGE_LAYOUT_AND_ORDER_KEYS = {
        "dashboard_tile_pref_com.oneplus.extras.DeviceSettings",
        "dashboard_tile_pref_org.lineageos.settings.device",
        "dashboard_tile_pref_org.lineageos.settings",
        "dashboard_tile_pref_org.omnirom.devices",
        "dashboard_tile_pref_org.omnirom.device",
        "dashboard_tile_pref_com.poco.parts",
        "dashboard_tile_pref_com.xiaomi.parts",
        "dashboard_tile_pref_com.asus.zenparts"
    };

    public static final String[] CHANGE_LAYOUT_KEYS = {
        "top_level_google",
        "dashboard_tile_pref_com.google.android.gms.app.settings.GoogleSettingsIALink",
        "dashboard_tile_pref_com.google.android.apps.wellbeing.settings.TopLevelSettingsActivity",
        "top_level_wellbeing",
    };

    public static final int[] LAYOUTS_N = {
        R.layout.rdnt_card_google,
        R.layout.rdnt_card_google,
        R.layout.rdnt_card_wellbeing,
        R.layout.rdnt_card_wellbeing
    };

    public TopLevelSettings() {
        final Bundle args = new Bundle();
        // Disable the search icon because this page uses a full search view in actionbar.
        args.putBoolean(NEED_SEARCH_ICON_IN_ACTION_BAR, false);
        setArguments(args);
    }

    @Override
    protected int getPreferenceScreenResId() {
        return R.xml.top_level_settings;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getMetricsCategory() {
        return SettingsEnums.DASHBOARD_SUMMARY;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        use(SupportPreferenceController.class).setActivity(getActivity());
    }

    @Override
    public int getHelpResource() {
        // Disable the help icon because this page uses a full search view in actionbar.
        return 0;
    }

    @Override
    public Fragment getCallbackFragment() {
        return this;
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        new SubSettingLauncher(getActivity())
                .setDestination(pref.getFragment())
                .setArguments(pref.getExtras())
                .setSourceMetricsCategory(caller instanceof Instrumentable
                        ? ((Instrumentable) caller).getMetricsCategory()
                        : Instrumentable.METRICS_CATEGORY_UNKNOWN)
                .setTitleRes(-1)
                .launch();
        return true;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        super.onCreatePreferences(savedInstanceState, rootKey);
        for (int i = 0; i < CHANGE_LAYOUT_KEYS.length; i++) {
            Preference preference = findPreference(CHANGE_LAYOUT_KEYS[i]);
            if (preference != null){
                preference.setLayoutResource(LAYOUTS_N[i]);
            }
        }
        for (int i = 0; i < CHANGE_LAYOUT_AND_ORDER_KEYS.length; i++) {
            Preference preference = findPreference(CHANGE_LAYOUT_AND_ORDER_KEYS[i]);
            if (preference != null){
                preference.setLayoutResource(R.layout.rdnt_card_device);
                preference.setOrder(12);
            }
        }
    }

    @Override
    protected boolean shouldForceRoundedIcon() {
        return getContext().getResources()
                .getBoolean(R.bool.config_force_rounded_icon_TopLevelSettings);
    }

    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider(R.xml.top_level_settings) {

                @Override
                protected boolean isPageSearchEnabled(Context context) {
                    // Never searchable, all entries in this page are already indexed elsewhere.
                    return false;
                }
            };
}

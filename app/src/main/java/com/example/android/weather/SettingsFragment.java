package com.example.android.weather;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.fragment.app.Fragment;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        final MultiSelectListPreference lp = (MultiSelectListPreference) findPreference("sub_details");
        lp.setDefaultValue(R.array.sub_details_default);

        lp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Set<String> x = (Set<String>) newValue;
                if(x.size()<3){
                    Toast.makeText(getActivity(),"Select 3 main details!",Toast.LENGTH_SHORT).show();
                    return false;
                }
                else{
                    return true;
                }
            }
        });

        Preference about = findPreference(getString(R.string.about));
        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), AboutActivity.class));
                return false;
            }
        });

        Preference privacyPolicy = findPreference(getString(R.string.privacyPolicy));
        privacyPolicy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), PrivacyPolicyActivity.class));
                return false;
            }
        });
        Preference terms = findPreference(getString(R.string.termsAndConditions));
        terms.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), TermsActivity.class));
                return false;
            }
        });
    }
}

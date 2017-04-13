package com.ultratweaker.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

import com.ultratweaker.R;
import com.ultratweaker.utils.du.CMDProcessor;

public class UltraTweaker extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    private SwitchPreference mSelinux;
    private ListPreference mGover;

    private static final String SELINUX = "selinux";
    private static final String GOVER = "gover";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    @SuppressLint("ValidFragment")
    public class MyPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        SharedPreferences mPrefs;

        @Override
        public void onStart() {
            super.onStart();
            mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        }

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.main_page);

            //SELinux
            mSelinux = (SwitchPreference) findPreference(SELINUX);
            mSelinux.setOnPreferenceChangeListener(this);

            if (CMDProcessor.runShellCommand("getenforce").getStdout().contains("Enforcing")) {
                mSelinux.setChecked(true);
                mSelinux.setSummary(R.string.selinux_enforcing_summary);
            } else {
                mSelinux.setChecked(false);
                mSelinux.setSummary(R.string.selinux_permissive_summary);
            }

            //Governors
            mGover = (ListPreference) findPreference(GOVER);
            mGover.setOnPreferenceChangeListener(this);
            mGover.setSummary(CMDProcessor.runShellCommand("cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor").getStdout());
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference == mSelinux) {
                if (newValue.toString().equals("true")) {
                    CMDProcessor.runSuCommand("setenforce 1");
                    mSelinux.setSummary(R.string.selinux_enforcing_summary);
                } else if (newValue.toString().equals("false")) {
                    CMDProcessor.runSuCommand("setenforce 0");
                    mSelinux.setSummary(R.string.selinux_permissive_summary);
                }
                return true;
            } else if (preference == mGover) {
                CMDProcessor.runSuCommand("echo " + mGover.getValue().toString() + " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor");
                mGover.setSummary(CMDProcessor.runShellCommand("cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor").getStdout());
                return true;
            }
            return false;
        }
    }
}

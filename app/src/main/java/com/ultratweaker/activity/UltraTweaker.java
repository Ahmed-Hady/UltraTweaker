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

import java.io.File;

public class UltraTweaker extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    /* Setting Objects*/
    private SwitchPreference mSelinux;
    private ListPreference mGover;
    private SwitchPreference mSysLight;
    private SwitchPreference mArchPower;

    /*Setting preference keys*/
    private static final String SELINUX = "selinux";
    private static final String GOVER = "gover";
    private static final String SYSLIGHT = "sysLight";
    private static final String ARCHPOWER = "arch_P";

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

            //System Light
            mSysLight = (SwitchPreference) findPreference(SYSLIGHT);
            mSysLight.setOnPreferenceChangeListener(this);

            if (CMDProcessor.runSuCommand("cat /sys/class/leds/charging/max_brightness").getStdout().contains("255")) {
                mSysLight.setChecked(true);
            } else {
                mSysLight.setChecked(false);
            }

            //ARCH Power
            mArchPower = (SwitchPreference) findPreference(ARCHPOWER);
            mArchPower.setOnPreferenceChangeListener(this);
            if(new File("/sys/kernel/sched/arch_power").exists()) {
                if (CMDProcessor.runSuCommand("cat /sys/kernel/sched/arch_power").getStdout().contains("1")) {
                    mArchPower.setChecked(true);
                } else {
                    mArchPower.setChecked(false);
                }
            }else{
                mArchPower.setEnabled(false);
            }
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
            } else if (preference == mSysLight) {
                if (newValue.toString().equals("true")) {
                    CMDProcessor.runSuCommand("echo 255 > /sys/class/leds/charging/max_brightness");
                } else if (newValue.toString().equals("false")) {
                    CMDProcessor.runSuCommand("echo 0 > /sys/class/leds/charging/max_brightness");
                }
                return true;
            }else if (preference == mArchPower) {
                if (newValue.toString().equals("true")) {
                    CMDProcessor.runSuCommand("echo 1 > /sys/kernel/sched/arch_power");
                } else if (newValue.toString().equals("false")) {
                    CMDProcessor.runSuCommand("echo 0 > /sys/kernel/sched/arch_power");
                }
                return true;
            }
            return false;
        }
    }
}

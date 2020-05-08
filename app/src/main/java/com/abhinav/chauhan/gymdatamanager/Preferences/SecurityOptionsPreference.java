package com.abhinav.chauhan.gymdatamanager.Preferences;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.biometric.BiometricManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceViewHolder;

import com.abhinav.chauhan.gymdatamanager.R;

public class SecurityOptionsPreference extends Preference {
    private SharedPreferences userPreferences;

    SecurityOptionsPreference(Context context) {
        super(context);
        userPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());//EditPreferences.getInstance().getUserPreference(getContext());
        setLayoutResource(R.layout.security_options_layout);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        setFingerRadio(holder);
        setPatternRadio(holder);
    }

    private void setPatternRadio(PreferenceViewHolder holder) {
        TextView textView = holder.itemView.findViewById(R.id.pattern_summary);
        RadioButton patternRadio = holder.itemView.findViewById(R.id.pattern_radio);
        KeyguardManager keyguardManager = (KeyguardManager) getContext().getSystemService(Context.KEYGUARD_SERVICE);
        assert keyguardManager != null;
        if (keyguardManager.isKeyguardSecure()) {
            textView.setText(R.string.select_pattern);
            if (userPreferences.getString("auth", "").equals("pattern")) {
                patternRadio.setChecked(true);
            }
            setAuthPreference(patternRadio, "pattern");
        } else {
            textView.setText(R.string.pattern_lock_disabled);
            patternRadio.setEnabled(false);
        }
    }

    private void setFingerRadio(PreferenceViewHolder holder) {
        TextView textView = holder.itemView.findViewById(R.id.fingerprint_summary);
        int specs = BiometricManager.from(getContext()).canAuthenticate();
        RadioButton fingerRadio = holder.itemView.findViewById(R.id.finger_radio);
        boolean canUseFingerPrint = true;
        switch (specs) {
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                textView.setText(R.string.no_fingerprint_sensor);
                canUseFingerPrint = false;
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                textView.setText(R.string.no_fingerprint_enrolled);
                canUseFingerPrint = false;
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                textView.setText(R.string.fingerprint_sensor_not_available);
                canUseFingerPrint = false;
                break;
        }
        if (canUseFingerPrint) {
            textView.setText(R.string.select_fingerprint);
            if (userPreferences.getString("auth", "").equals("finger")) {
                fingerRadio.setChecked(true);
            }
            setAuthPreference(fingerRadio, "finger");
        } else {
            fingerRadio.setEnabled(false);
        }
    }

    private void setAuthPreference(RadioButton radio, String value) {
        radio.setOnClickListener(v -> PreferenceManager.getDefaultSharedPreferences(getContext())
                .edit()
                .putString("auth", value)
                .apply());
    }

}

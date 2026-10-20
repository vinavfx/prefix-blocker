package app.prefixblocker;

import android.net.Uri;
import android.telecom.Call;
import android.telecom.CallScreeningService;
import android.telephony.TelephonyManager;
import java.util.Locale;

public final class PrefixScreeningService extends CallScreeningService {
    @Override
    public void onScreenCall(Call.Details details) {
        if (details.getCallDirection() != Call.Details.DIRECTION_INCOMING) {
            return;
        }
        Uri handle = details.getHandle();
        boolean blocked = handle != null && "tel".equals(handle.getScheme())
                && PrefixRules.blocks(handle.getSchemeSpecificPart(), getRegion(),
                        new PrefixStore(this).getPrefixes());
        respondToCall(details, new CallResponse.Builder()
                .setDisallowCall(blocked)
                .setRejectCall(blocked)
                .setSkipNotification(blocked)
                .setSkipCallLog(blocked)
                .build());
    }

    private String getRegion() {
        TelephonyManager telephony = getSystemService(TelephonyManager.class);
        String country = telephony == null ? "" : telephony.getNetworkCountryIso();
        if (country.isEmpty() && telephony != null) {
            country = telephony.getSimCountryIso();
        }
        return (country.isEmpty() ? Locale.getDefault().getCountry() : country)
                .toUpperCase(Locale.ROOT);
    }
}

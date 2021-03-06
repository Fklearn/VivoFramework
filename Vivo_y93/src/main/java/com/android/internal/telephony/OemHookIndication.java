package com.android.internal.telephony;

import android.annotation.VivoHook;
import android.annotation.VivoHook.VivoHookType;
import android.hardware.radio.deprecated.V1_0.IOemHookIndication.Stub;
import android.os.AsyncResult;
import com.android.internal.telephony.uicc.IccUtils;
import java.util.ArrayList;

public class OemHookIndication extends Stub {
    RIL mRil;

    public OemHookIndication(RIL ril) {
        this.mRil = ril;
    }

    @VivoHook(hookType = VivoHookType.CHANGE_CODE)
    public void oemHookRaw(int indicationType, ArrayList<Byte> data) {
        this.mRil.processIndication(indicationType);
        byte[] response = RIL.arrayListToPrimitiveArray(data);
        this.mRil.processUnsolOemHookRaw(response);
        this.mRil.unsljLogvRet(1028, IccUtils.bytesToHexString(response));
        if (this.mRil.mUnsolOemHookRawRegistrant != null) {
            this.mRil.mUnsolOemHookRawRegistrant.notifyRegistrant(new AsyncResult(null, response, null));
        }
    }
}

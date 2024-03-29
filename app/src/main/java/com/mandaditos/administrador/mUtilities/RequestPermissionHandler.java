package com.mandaditos.administrador.mUtilities;

import android.app.*;
import android.content.pm.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import java.util.*;

public class RequestPermissionHandler
{
    private Activity mActivity;
    private RequestPermissionListener mRequestPermissionListener;
    private int mRequestCode;

	public void requestPermission(Activity activity, @NonNull String[] permissions, int requestCode,
								  RequestPermissionListener listener) {
		mActivity = activity;
		mRequestCode = requestCode;
		mRequestPermissionListener = listener;

		if (!needRequestRuntimePermissions()) {
			mRequestPermissionListener.onSuccess();
			return;
		}
		requestUnGrantedPermissions(permissions, requestCode);
	}

	private boolean needRequestRuntimePermissions() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
	}

	private void requestUnGrantedPermissions(String[] permissions, int requestCode) {
		String[] unGrantedPermissions = findUnGrantedPermissions(permissions);
		if (unGrantedPermissions.length == 0) {
			mRequestPermissionListener.onSuccess();
			return;
		}
		ActivityCompat.requestPermissions(mActivity, unGrantedPermissions, requestCode);
	}

	private boolean isPermissionGranted(String permission) {
		return ActivityCompat.checkSelfPermission(mActivity, permission)
            == PackageManager.PERMISSION_GRANTED;
	}

	private String[] findUnGrantedPermissions(String[] permissions) {
		List<String> unGrantedPermissionList = new ArrayList<>();
		for (String permission : permissions) {
			if (!isPermissionGranted(permission)) {
				unGrantedPermissionList.add(permission);
			}
		}
		return unGrantedPermissionList.toArray(new String[0]);
	}

	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		if (requestCode == mRequestCode) {
			if (grantResults.length > 0) {
				for (int grantResult : grantResults) {
					if (grantResult != PackageManager.PERMISSION_GRANTED) {
						mRequestPermissionListener.onFailed();
						return;
					}
				}
				mRequestPermissionListener.onSuccess();
			} else {
				mRequestPermissionListener.onFailed();
			}
		}
	}

	public interface RequestPermissionListener {
		void onSuccess();

		void onFailed();
	}
}

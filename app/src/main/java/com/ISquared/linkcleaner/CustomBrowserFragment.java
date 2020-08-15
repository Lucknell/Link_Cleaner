package com.ISquared.linkcleaner;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class CustomBrowserFragment extends AppCompatDialogFragment implements AppAdapter.OnAppSelectedListener {


    private static final String ARGUMENT_KEY_APPS = "apps";
    private static final String ARGUMENT_URL = "url";
    private static final String ARGUMENT_STORE = "store";

    static CustomBrowserFragment newInstance(ActivityInfo[] apps, String url, ActivityInfo store) {
        final Bundle arguments = new Bundle();
        arguments.putParcelableArray(ARGUMENT_KEY_APPS, apps);
        arguments.putString(ARGUMENT_URL, url);
        arguments.putParcelable(ARGUMENT_STORE, store);

        final CustomBrowserFragment fragment = new CustomBrowserFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ContextThemeWrapper wrapper = new ContextThemeWrapper(getContext(),
                android.R.style.Theme_Material_Light);
        final View view = LayoutInflater.from(wrapper).inflate(R.layout.custom_browser, null);
        final Dialog dialog = new CustomWidthBottomSheetDialog(wrapper);
        dialog.setContentView(view);
        RecyclerView appList = view.findViewById(R.id.apps);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        appList.setLayoutManager(layoutManager);
        AppAdapter adapter = new AppAdapter(wrapper,
                (ActivityInfo[]) getArguments().getParcelableArray(ARGUMENT_KEY_APPS),
                (ActivityInfo) getArguments().getParcelable(ARGUMENT_STORE));
        adapter.setOnAppSelectedListener(this);
        appList.setAdapter(adapter);

        return dialog;
    }

    static class CustomWidthBottomSheetDialog extends BottomSheetDialog {

        CustomWidthBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public void setContentView(View contentView) {
            super.setContentView(contentView);

        }

        @Override
        public void setContentView(@LayoutRes int layoutResId) {
            throw new IllegalStateException("CustomWidthBottomSheetDialog only supports setContentView(View)");
        }

        @Override
        public void setContentView(View view, ViewGroup.LayoutParams params) {
            throw new IllegalStateException("CustomWidthBottomSheetDialog only supports setContentView(View)");
        }
    }

    @Override
    public void onAppSelected(AppAdapter.App app) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getArguments().getString(ARGUMENT_URL)));
        intent.setPackage(app.getPackageName());
        startActivity(intent);
        getActivity().finish();
        dismiss();
    }
}
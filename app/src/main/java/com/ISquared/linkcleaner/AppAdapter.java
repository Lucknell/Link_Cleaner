package com.ISquared.linkcleaner;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AppAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /* package */ static class App {
        private final Context context;
        private final ActivityInfo info;
        private final String label;

        App(Context context, ActivityInfo info) {
            this.context = context;
            this.info = info;
            this.label = info.loadLabel(context.getPackageManager()).toString();
        }

        String getLabel() {
            return label;
        }

        Drawable loadIcon() {
            return info.loadIcon(context.getPackageManager());
        }

        String getPackageName() {
            return info.packageName;
        }
    }

    /* package */ interface OnAppSelectedListener {
        void onAppSelected(App app);
    }

    private final List<App> apps;
    private final App store;
    private OnAppSelectedListener listener;

    AppAdapter(Context context, ActivityInfo[] infoArray, ActivityInfo store) {
        final List<App> apps = new ArrayList<>(infoArray.length);

        for (ActivityInfo info : infoArray) {
            if(info.packageName.equals(context.getPackageName()))
                continue;
            apps.add(new App(context, info));
        }

        Collections.sort(apps, new Comparator<App>() {
            @Override
            public int compare(App app1, App app2) {
                return app1.getLabel().compareTo(app2.getLabel());
            }
        });

        this.apps = apps;
        this.store = store != null ? new App(context, store) : null;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == AppViewHolder.LAYOUT_ID) {
            return new AppViewHolder(
                    inflater.inflate(AppViewHolder.LAYOUT_ID, parent, false));
        }
        throw new IllegalStateException("Unknown view type: " + viewType);
    }

    /* package */ void setOnAppSelectedListener(OnAppSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
            return AppViewHolder.LAYOUT_ID;
        }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
         bindApp(holder, position);

    }

    private void bindApp(RecyclerView.ViewHolder holder, int position) {
        final AppViewHolder appViewHolder = (AppViewHolder) holder;
        final App app = apps.get(position);

        appViewHolder.bind(app, listener);
    }


    @Override
    public int getItemCount() {
        return apps.size() + (store != null ? 1 : 0);
    }
}
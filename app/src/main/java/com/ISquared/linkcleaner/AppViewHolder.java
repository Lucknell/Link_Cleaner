package com.ISquared.linkcleaner;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

class AppViewHolder extends RecyclerView.ViewHolder {
    static final int LAYOUT_ID = R.layout.item_app;

    private final TextView titleView;
    private final ImageView iconView;

    /* package */ AppViewHolder(View itemView) {
        super(itemView);

        titleView = itemView.findViewById(R.id.title);
        iconView = itemView.findViewById(R.id.icon);
    }

    void bind(final AppAdapter.App app, final AppAdapter.OnAppSelectedListener listener) {
        titleView.setText(app.getLabel());

        iconView.setImageDrawable(app.loadIcon());

        itemView.setOnClickListener(createListenerWrapper(app, listener));
    }

    private View.OnClickListener createListenerWrapper(final AppAdapter.App app, final AppAdapter.OnAppSelectedListener listener) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onAppSelected(app);
                }
            }
        };
    }
}
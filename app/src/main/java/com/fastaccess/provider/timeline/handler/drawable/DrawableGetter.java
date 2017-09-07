package com.fastaccess.provider.timeline.handler.drawable;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Html;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.fastaccess.R;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Kosh on 22 Apr 2017, 7:44 PM
 */

public class DrawableGetter implements Html.ImageGetter {
    private WeakReference<TextView> container;
    private final Set<GlideDrawableTarget> cachedTargets;

    public DrawableGetter(TextView tv) {
        tv.setTag(R.id.drawable_callback, this);
        this.container = new WeakReference<>(tv);
        this.cachedTargets = new HashSet<>();
    }

    @Override public Drawable getDrawable(@NonNull String url) {
        final UrlDrawable urlDrawable = new UrlDrawable();
        if (container != null && container.get() != null) {
            Context context = container.get().getContext();
            final GenericRequestBuilder load = Glide.with(context)
                    .load(url)
                    .dontAnimate();
            final GlideDrawableTarget target = new GlideDrawableTarget(urlDrawable, container);
            load.into(target);
            cachedTargets.add(target);
        }
        return urlDrawable;
    }

    public void clear(@NonNull DrawableGetter drawableGetter) {
        if (drawableGetter.cachedTargets != null) {
            for (GlideDrawableTarget target : drawableGetter.cachedTargets) {
                Glide.clear(target);
            }
        }
    }
}

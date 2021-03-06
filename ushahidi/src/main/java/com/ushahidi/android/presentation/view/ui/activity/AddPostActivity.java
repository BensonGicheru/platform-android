/*
 * Copyright (c) 2015 Ushahidi.
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program in the file LICENSE-AGPL. If not, see
 * https://www.gnu.org/licenses/agpl-3.0.html
 */

package com.ushahidi.android.presentation.view.ui.activity;

import com.addhen.android.raiburari.presentation.di.HasComponent;
import com.ushahidi.android.R;
import com.ushahidi.android.presentation.di.components.post.AddPostComponent;
import com.ushahidi.android.presentation.di.components.post.DaggerAddPostComponent;
import com.ushahidi.android.presentation.model.FormModel;
import com.ushahidi.android.presentation.view.ui.fragment.AddPostFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Renders {@link AddPostFragment}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class AddPostActivity extends BaseAppActivity
        implements HasComponent<AddPostComponent> {

    private static final String INTENT_EXTRA_PARAM_FORM_MODEL
            = "com.ushahidi.android.INTENT_PARAM_POST_MODEL";

    private static final String BUNDLE_STATE_PARAM_FORM_MODEL
            = "com.ushahidi.android.STATE_PARAM_POST_MODEL";

    private static final String FRAG_TAG = "add_post";

    private AddPostComponent mAddPostComponent;

    private AddPostFragment mAddPostFragment;

    private FormModel mFormModel;

    /**
     * Default constructor
     */
    public AddPostActivity() {
        super(R.layout.activity_add_post, 0);
    }

    /**
     * Provides {@link Intent} for launching this activity
     *
     * @param context   The calling context
     * @param formModel The form model to pass to this activity from the calling activity
     * @return The intent to launch this activity
     */
    public static Intent getIntent(final Context context, FormModel formModel) {
        Intent intent = new Intent(context, AddPostActivity.class);
        intent.putExtra(INTENT_EXTRA_PARAM_FORM_MODEL, formModel);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injector();
        initialize(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(BUNDLE_STATE_PARAM_FORM_MODEL, mFormModel);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void injector() {
        mAddPostComponent = DaggerAddPostComponent.builder()
                .appComponent(getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }

    private void initialize(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mFormModel = getIntent().getParcelableExtra(INTENT_EXTRA_PARAM_FORM_MODEL);
            mAddPostFragment = (AddPostFragment) getSupportFragmentManager()
                    .findFragmentByTag(FRAG_TAG);
            if (mAddPostFragment == null) {
                mAddPostFragment = AddPostFragment.newInstance(mFormModel);
                replaceFragment(R.id.add_post_fragment_container, mAddPostFragment, FRAG_TAG);
            }

        } else {
            mFormModel = savedInstanceState.getParcelable(BUNDLE_STATE_PARAM_FORM_MODEL);
        }
        getSupportActionBar().setTitle(mFormModel.getName());
    }

    @Override
    public AddPostComponent getComponent() {
        return mAddPostComponent;
    }
}

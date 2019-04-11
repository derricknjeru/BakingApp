package com.derrick.bakingapp.UI.steps;


import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.derrick.bakingapp.R;
import com.derrick.bakingapp.data.local.Step;
import com.derrick.bakingapp.databinding.FragmentStepsBinding;
import com.derrick.bakingapp.utils.BakingPreference;
import com.derrick.bakingapp.utils.InjectorUtils;
import com.derrick.bakingapp.utils.LogUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment implements View.OnClickListener {

    private FragmentStepsBinding binding;
    private StepsFragmentViewModel mViewModel;

    private static final String LOG_TAG = StepsFragment.class.getSimpleName();

    private SimpleExoPlayer player;
    private PlayerView playerView;

    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;
    private MediaSource mediaSource;
    private boolean isLand;


    public StepsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_steps, container, false);

        playerView = binding.videoView;

        StepsFragmentViewModelFactory factory = InjectorUtils.provideStepsFragmentViewModelFactory(getActivity(), BakingPreference.getStepRecipeIdQuery(getActivity()));
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(StepsFragmentViewModel.class);

        binding.count.setText((BakingPreference.getStepPosQuery(getActivity()) + 1) + getString(R.string.of) + BakingPreference.getTotalStepPosQuery(getActivity()));

        binding.arrowRight.setOnClickListener(this);
        binding.arrowLeft.setOnClickListener(this);


        hideArrows();

        return binding.getRoot();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideViews(View.GONE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            hideViews(View.VISIBLE);
        }
    }

    private void hideViews(int visibility) {
        binding.linearLayout.setVisibility(visibility);
        binding.stepDesc.setVisibility(visibility);
    }


    public void setList(int step_id) {
        mViewModel.getStepsListLiveData().observe(this, recipeList -> {
            if (recipeList != null && recipeList.size() > 0) {
                //will always be one
                Step step = recipeList.get(0).getSteps().get(step_id);

                if (step != null) {
                    populateUI(step);
                }
            }

        });
    }

    private void populateUI(Step step) {
        if (!TextUtils.isEmpty(step.getDescription())) {
            binding.stepDesc.setVisibility(View.VISIBLE);
            binding.stepDesc.setText(step.getDescription());
        } else {
            binding.stepDesc.setVisibility(View.GONE);
        }

        LogUtils.showLog(LOG_TAG, "@Steps player::" + (player != null));

        LogUtils.showLog(LOG_TAG, "@Steps fragment step.getVideoURL()::" + step.getVideoURL());

        if (!TextUtils.isEmpty(step.getVideoURL())) {
            mediaSource = buildMediaSource(Uri.parse(step.getVideoURL()));
            if (player != null) {
                player.prepare(mediaSource);
            }
        } else {
            mediaSource = null;
            if (player != null) {
                playerView.setDefaultArtwork(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.question_mark));
                player.prepare(mediaSource);
            }
        }

        if (!TextUtils.isEmpty(step.getThumbnailURL())) {
            hideThumbnailImage(binding.thumbnail, View.VISIBLE);
            Picasso.get().load(step.getThumbnailURL()).into(binding.thumbnail);
        } else {
            hideThumbnailImage(binding.thumbnail, View.GONE);
        }
    }

    private void hideThumbnailImage(ImageView thumbnail, int visibility) {
        thumbnail.setVisibility(visibility);
    }


    @Override
    public void onClick(View v) {

        int x = BakingPreference.getStepPosQuery(getActivity());

        controlArrows(v, x);
    }

    private void arrowsAreVisible(int visible, int visible2) {
        binding.arrowLeft.setVisibility(visible);
        binding.arrowRight.setVisibility(visible2);
    }

    private void controlArrows(View v, int x) {
        if (v == binding.arrowLeft) {
            if (x != 0) {
                LogUtils.showLog(LOG_TAG, "@Steps fragment yes -1::" + (x - 1));
                BakingPreference.setStepQuery(getActivity(), (x - 1));
            }
        }
        if (v == binding.arrowRight) {
            if (x < (BakingPreference.getTotalStepPosQuery(getActivity()) - 1)) {
                LogUtils.showLog(LOG_TAG, "@Steps fragment yes +1::" + (x + 1));
                BakingPreference.setStepQuery(getActivity(), (x + 1));
            }
        }
        if (x < (BakingPreference.getTotalStepPosQuery(getActivity()) - 1)) {
            setList(BakingPreference.getStepPosQuery(getActivity()));
        }
        hideArrows();

        binding.count.setText((BakingPreference.getStepPosQuery(getActivity()) + 1) + getString(R.string.of) + BakingPreference.getTotalStepPosQuery(getActivity()));
    }

    private void hideArrows() {
        if (BakingPreference.getStepPosQuery(getActivity()) == 0) {
            arrowsAreVisible(View.INVISIBLE, View.VISIBLE);
        } else if (BakingPreference.getStepPosQuery(getActivity()) == BakingPreference.getTotalStepPosQuery(getActivity()) - 1) {
            arrowsAreVisible(View.VISIBLE, View.INVISIBLE);
        } else {
            arrowsAreVisible(View.VISIBLE, View.VISIBLE);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }

        setList(BakingPreference.getStepPosQuery(getActivity()));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void initializePlayer() {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getActivity()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            playerView.setPlayer(player);
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);
        }

    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory("baking-app"))
                .createMediaSource(uri);
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


}

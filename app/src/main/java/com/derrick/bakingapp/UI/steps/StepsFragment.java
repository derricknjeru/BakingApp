package com.derrick.bakingapp.UI.steps;


import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import static com.derrick.bakingapp.UI.steps.StepsActivity.EXTRA_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment implements View.OnClickListener {

    private FragmentStepsBinding binding;
    private StepsFragmentViewModel mViewModel;

    private long recipe_id;

    private static final String LOG_TAG = StepsFragment.class.getSimpleName();

    private SimpleExoPlayer player;
    private PlayerView playerView;

    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;


    public StepsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_steps, container, false);

        playerView = binding.videoView;


        if (getActivity() != null) {
            recipe_id = getActivity().getIntent().getLongExtra(EXTRA_ID, 0);
            StepsFragmentViewModelFactory factory = InjectorUtils.provideStepsFragmentViewModelFactory(getActivity(), (int) recipe_id);
            mViewModel = ViewModelProviders.of(getActivity(), factory).get(StepsFragmentViewModel.class);
        }


        binding.count.setText(BakingPreference.getStepPosQuery(getActivity()) + getString(R.string.of) + (BakingPreference.getTotalStepPosQuery(getActivity()) - 1));

        binding.arrowRight.setOnClickListener(this);
        binding.arrowLeft.setOnClickListener(this);


        hideArrows();


        return binding.getRoot();

    }

    public void setList(int step_id) {
        mViewModel.getStepsListLiveData().observe(this, recipeList -> {
            if (recipeList != null && recipeList.size() > 0) {
                //will always be one
                Step step = recipeList.get(0).getSteps().get(step_id);
                if (step != null) {
                    binding.stepDesc.setText(step.getDescription());
                    MediaSource mediaSource;
                    LogUtils.showLog(LOG_TAG, "@Steps fragment step.getVideoURL()::" + step.getVideoURL());

                    if (!TextUtils.isEmpty(step.getVideoURL())) {
                        mediaSource = buildMediaSource(Uri.parse(step.getVideoURL()));
                        player.prepare(mediaSource, true, false);
                    } else {
                        LogUtils.showLog(LOG_TAG, "@Steps fragment step.getVideoURL() is empty::");
                        videoLinkIsEmpty();

                    }

                }

            }

        });
    }

    private void videoLinkIsEmpty() {
        MediaSource mediaSource;
        mediaSource = null;
        player.prepare(mediaSource);
        playerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), R.drawable.question_mark));
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
            initializePlayer();
        }
        hideArrows();

        binding.count.setText(BakingPreference.getStepPosQuery(getActivity()) + getString(R.string.of) + (BakingPreference.getTotalStepPosQuery(getActivity()) - 1));
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

        setList(BakingPreference.getStepPosQuery(getActivity()));

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
        return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory("exoplayer-baking"))
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

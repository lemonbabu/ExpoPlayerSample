package com.yusufcakmak.exoplayersample;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

public class VideoPlayerActivity extends AppCompatActivity implements PlaybackControlView.VisibilityListener {

    private SimpleExoPlayerView simpleExoPlayerView;
    private Handler mainHandler;
    private BandwidthMeter bandwidthMeter;
    private TrackSelector trackSelector;
    private TrackSelection.Factory videoTrackSelectionFactory;
    private LoadControl loadControl;
    private SimpleExoPlayer player;
    private DataSource.Factory dataSourceFactory;
    private ExtractorsFactory extractorsFactory;
    private MediaSource videoSource;

    private String videoUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        mainHandler = new Handler();
        bandwidthMeter = new DefaultBandwidthMeter();
        videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);
        loadControl = new DefaultLoadControl();

        player = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector, loadControl);

        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        simpleExoPlayerView.setControllerVisibilityListener(this);
        simpleExoPlayerView.requestFocus();
        simpleExoPlayerView.setPlayer(player);


        bandwidthMeter = new DefaultBandwidthMeter();
        dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
        extractorsFactory = new DefaultExtractorsFactory();
        videoSource = new ExtractorMediaSource(Uri.parse(videoUrl), dataSourceFactory, extractorsFactory, null, null);
        player.prepare(videoSource);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.setPlayWhenReady(false);
    }

    @Override
    public void onVisibilityChange(int visibility) {

    }
}

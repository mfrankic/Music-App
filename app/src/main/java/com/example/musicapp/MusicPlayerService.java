package com.example.musicapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.media.MediaBrowserServiceCompat;
import androidx.media.session.MediaButtonReceiver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayerService extends MediaBrowserServiceCompat {

    private static final int NOTIFICATION_ID = 101;
    private static final String NOTIFICATION_CHANNEL_ID = "music_player_channel";
    private static final int REQUEST_CODE = 100;
    private MediaPlayer mediaPlayer;
    private ArrayList<String> songList; // list of song file URLs or paths
    private int mCurrentSongIndex; // index of the currently playing song
    private MediaSessionCompat mediaSession;
    private MediaMetadataCompat.Builder metadataBuilder;
    private PlaybackStateCompat.Builder stateBuilder;
    private MediaControllerCompat mediaController;

    private List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();
    private boolean mediaItemsAreReady = false;
    private boolean isBuffering = false;
    private int lastMediaState = PlaybackStateCompat.STATE_NONE;

    @Override
    public void onCreate() {
        super.onCreate();

        mediaSession = new MediaSessionCompat(this, "MyMusicService");
        mediaSession.setMediaButtonReceiver(null);
        setSessionToken(mediaSession.getSessionToken());

        stateBuilder = new PlaybackStateCompat.Builder().setActions(
                PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                        PlaybackStateCompat.ACTION_STOP
        );

        metadataBuilder = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "Artist Name")
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, "Album Name")
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, "Song Name")
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 0);

        mediaSession.setMetadata(metadataBuilder.build());

        mediaSession.setPlaybackState(stateBuilder.build());

        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                play();
            }

            @Override
            public void onPause() {
                pause();
            }

            @Override
            public void onSkipToNext() {
                skipToNext();
            }

            @Override
            public void onSkipToPrevious() {
                skipToPrevious();
            }

            @Override
            public void onStop() {
                stop();
            }

            @Override
            public void onSeekTo(long pos) {
                seekTo(pos);
            }

            @Override
            public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
                if (Intent.ACTION_MEDIA_BUTTON.equals(mediaButtonEvent.getAction())) {
                    KeyEvent event = mediaButtonEvent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                    if (KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE == event.getKeyCode()) {
                        if (mediaPlayer.isPlaying()) {
                            pause();
                        } else {
                            play();
                        }
                        return true;
                    }
                    if (KeyEvent.KEYCODE_MEDIA_NEXT == event.getKeyCode()) {
                        skipToNext();
                        return true;
                    }
                    if (KeyEvent.KEYCODE_MEDIA_PREVIOUS == event.getKeyCode()) {
                        skipToPrevious();
                        return true;
                    }
                }
                return super.onMediaButtonEvent(mediaButtonEvent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaSession != null) {
            mediaSession.release();
            mediaSession = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MediaButtonReceiver.handleIntent(mediaSession, intent);
        // get the song list from intent
        if (intent == null) {
            return START_NOT_STICKY;
        }
        songList = intent.getStringArrayListExtra("songList");
        mCurrentSongIndex = intent.getIntExtra("songIndex", 0);

        mediaController = new MediaControllerCompat(MusicPlayerService.this, mediaSession.getSessionToken());
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(android.media.AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(android.media.AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(songList.get(mCurrentSongIndex)));
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                metadataBuilder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer.getDuration());
                mediaSession.setMetadata(metadataBuilder.build());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnCompletionListener(mp -> {
            // handle completion of a track
            skipToNext();
        });

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        // Implement a method to verify whether the client is authorized to access the media.
        if (isClientAuthorized(clientPackageName, clientUid)) {
            // Returns the root ID that clients can use with onLoadChildren() to retrieve the content hierarchy.
            return new BrowserRoot("root_id", null);
        } else {
            // Return null if the client is not authorized.
            return null;
        }
    }

    // Dummy method for client verification. You should implement actual logic.
    private boolean isClientAuthorized(String clientPackageName, int clientUid) {
        // Logic to check if the client is authorized to access the media.
        return true;
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        // If the media item information is ready, you can call sendResult() immediately.
        if (mediaItemsAreReady) {
            result.sendResult(mediaItems);
        } else {
            result.detach();
            // Load media items and call sendResult() when ready.
            loadMediaItems(result);
        }
    }

    // Dummy method for media items loading. You should implement actual logic.
    private void loadMediaItems(Result<List<MediaBrowserCompat.MediaItem>> result) {
        // Logic to load media items.
        result.sendResult(mediaItems);
    }

    private void play() {
        PlaybackStateCompat pbStateCompat = mediaController.getPlaybackState();
        if (pbStateCompat.getState() == PlaybackStateCompat.STATE_PAUSED || pbStateCompat.getState() == PlaybackStateCompat.STATE_NONE) {
            mediaPlayer.start();
            updatePlaybackState(PlaybackStateCompat.ACTION_PLAY);
        }
    }

    private void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            updatePlaybackState(PlaybackStateCompat.ACTION_PAUSE);
        }
    }

    private void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            updatePlaybackState(PlaybackStateCompat.ACTION_STOP);
        }
    }

    private void skipToNext() {
        if (mediaPlayer != null && songList != null && mCurrentSongIndex < songList.size() - 1) {
            mCurrentSongIndex++;
            stop();
            play();
            updatePlaybackState(PlaybackStateCompat.ACTION_SKIP_TO_NEXT);
        }
    }

    private void skipToPrevious() {
        if (mediaPlayer != null && mCurrentSongIndex > 0) {
            mCurrentSongIndex--;
            stop();
            play();
            updatePlaybackState(PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);
        }
    }

    private void seekTo(long position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo((int) position);
            mediaPlayer.setOnSeekCompleteListener(mp -> updatePlaybackState(PlaybackStateCompat.ACTION_SEEK_TO));
        }
    }

    private Notification getNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // replace with your own icon
                .setContentTitle("My Music Player") // replace with dynamic title based on playing track
                .setContentText("Now playing...") // replace with dynamic text based on playing track
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.playlist_image)) // replace with dynamic album art
                .setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_STOP))
                .setContentIntent(createContentIntent())
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        // Add previous action
        builder.addAction(new NotificationCompat.Action(R.drawable.ic_previous, "Previous",
                MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)));

        // Add play/pause action
        if (mediaPlayer.isPlaying()) {
            builder.addAction(new NotificationCompat.Action(R.drawable.ic_pause, "Pause",
                    MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_PLAY_PAUSE)));
        } else {
            builder.addAction(new NotificationCompat.Action(R.drawable.ic_play, "Play",
                    MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_PLAY_PAUSE)));
        }

        // Add next action
        builder.addAction(new NotificationCompat.Action(R.drawable.ic_next, "Next",
                MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_SKIP_TO_NEXT)));

        // Apply the media style template
        builder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(mediaSession.getSessionToken())
                .setShowActionsInCompactView(0, 1, 2) // indices for "play/pause", "next" and "previous" actions
                .setShowCancelButton(true)
                .setCancelButtonIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_STOP)));

        return builder.build();
    }

    private PendingIntent createContentIntent() {
        Intent openUI = new Intent(this, MainActivity.class);
        openUI.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this, REQUEST_CODE, openUI, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private void updatePlaybackState(long action) {
        if (action == PlaybackStateCompat.ACTION_PLAY) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mediaPlayer.getCurrentPosition(), 1, SystemClock.elapsedRealtime());
        } else if (action == PlaybackStateCompat.ACTION_PAUSE) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mediaPlayer.getCurrentPosition(), 1, SystemClock.elapsedRealtime());
        } else if (action == PlaybackStateCompat.ACTION_STOP) {
            stateBuilder.setState(PlaybackStateCompat.STATE_STOPPED, mediaPlayer.getCurrentPosition(), 1, SystemClock.elapsedRealtime());
        } else if (action == PlaybackStateCompat.ACTION_SKIP_TO_NEXT) {
            stateBuilder.setState(PlaybackStateCompat.STATE_SKIPPING_TO_NEXT, mediaPlayer.getCurrentPosition(), 1, SystemClock.elapsedRealtime());
        } else if (action == PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS) {
            stateBuilder.setState(PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS, mediaPlayer.getCurrentPosition(), 1, SystemClock.elapsedRealtime());
        } else if (action == PlaybackStateCompat.ACTION_SEEK_TO) {
            lastMediaState = mediaController.getPlaybackState().getState();
            // detect when buffering is complete
            if (lastMediaState == PlaybackStateCompat.STATE_PLAYING) {
                stateBuilder.setState(PlaybackStateCompat.STATE_BUFFERING, mediaPlayer.getCurrentPosition(), 1, SystemClock.elapsedRealtime());
                mHandler.post(playbackBuffering);
            } else {
                stateBuilder.setState(lastMediaState, mediaPlayer.getCurrentPosition(), 1, SystemClock.elapsedRealtime());
            }
        } else if (action == PlaybackStateCompat.ACTION_PLAY_PAUSE) {
            if (mediaPlayer.isPlaying()) {
                stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mediaPlayer.getCurrentPosition(), 1, SystemClock.elapsedRealtime());
            } else {
                stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mediaPlayer.getCurrentPosition(), 1, SystemClock.elapsedRealtime());
            }
        }
        mediaSession.setPlaybackState(stateBuilder.build());

        if (action != PlaybackStateCompat.ACTION_STOP) {
            // Now that the state is updated, notify the service to become active
            startForeground(NOTIFICATION_ID, getNotification());
        } else {
            stopForeground(true);
        }
    }

    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Runnable playbackBuffering = new Runnable() {
        @Override
        public void run() {
            PlaybackStateCompat playbackState = mediaController.getPlaybackState();
            isBuffering = playbackState != null && playbackState.getState() == PlaybackStateCompat.STATE_BUFFERING;
            if (isBuffering && mediaPlayer.isPlaying()) {
                updatePlaybackState(PlaybackStateCompat.ACTION_PLAY);
                mHandler.removeCallbacks(playbackBuffering);
            }
        }
    };

}
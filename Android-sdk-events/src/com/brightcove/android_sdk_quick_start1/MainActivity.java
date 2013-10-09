package com.brightcove.android_sdk_quick_start1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.brightcove.player.event.Event;
import com.brightcove.player.event.EventEmitter;
import com.brightcove.player.event.EventListener;
import com.brightcove.player.event.EventType;
import com.brightcove.player.media.Catalog;
import com.brightcove.player.media.PlaylistListener;
import com.brightcove.player.model.Playlist;
import com.brightcove.player.view.BrightcoveVideoView;

public class MainActivity extends Activity {
  public static final String TAG = "**VIDEO INFO**";
  EventEmitter eventEmitter;
  boolean isPlaying;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final BrightcoveVideoView bcVideoView = (BrightcoveVideoView) findViewById(R.id.bc_video_view);
    eventEmitter =  bcVideoView.getEventEmitter();
    isPlaying = true;

    final Catalog catalog = new Catalog("XGuquNMCweRY0D3tt_VUotzuyIASMAzhUS4F8ZIWa_e0cYlKpA4WtQ..");

    eventEmitter.on(EventType.DID_PLAY, new EventListener() {

      @Override
      public void processEvent(Event event) {
        Log.d(TAG,"*** Event Info***" + event);
      }
    });
    eventEmitter.on(EventType.COMPLETED, new EventListener() {

      @Override
      public void processEvent(Event event) {
        Log.d(TAG,"*** Event Info***" + event);
      }
    });

    eventEmitter.on(EventType.DID_PAUSE, new EventListener() {

      @Override
      public void processEvent(Event arg0) {
        isPlaying = false;
        invalidateOptionsMenu();
      }
    });

    eventEmitter.on(EventType.DID_PLAY, new EventListener() {

      @Override
      public void processEvent(Event arg0) {
        isPlaying = true;
        invalidateOptionsMenu();
      }
    });

    catalog.findPlaylistByID("1752604519001", new PlaylistListener() {
      @Override
      public void onPlaylist(Playlist playlist) {
        bcVideoView.addAll(playlist.getVideos());
        bcVideoView.start();
      }

      @Override
      public void onError(String error) {
        //Insert error handling here
        Log.e(TAG, error);
      }
    });
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    if(isPlaying){
      menu.findItem(R.id.play).setEnabled(false);
    }else{
      menu.findItem(R.id.pause).setEnabled(false);
    }
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onMenuItemSelected(int featureId, MenuItem item) {
    switch (item.getItemId()) {
    case R.id.pause:
      eventEmitter.emit(EventType.PAUSE);
      return true;
    case R.id.play:
      eventEmitter.emit(EventType.PLAY);
      return true;
    default:
      return super.onMenuItemSelected(featureId, item);
    }
  }  
}
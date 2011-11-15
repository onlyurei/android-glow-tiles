package com.intofan.android.glowtiles;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class GlowTilesWallpaper extends WallpaperService {

	private final Handler handler = new Handler();

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public Engine onCreateEngine() {
		return new TileEngine();
	}

	private class TileEngine extends Engine {

		private final Paint stroke = new Paint();
		private final Paint fill = new Paint();
		private long startTime;
		private int minR = Math.min(Color.red(Setting.color1), Color.red(Setting.color2));
		private int minG = Math.min(Color.green(Setting.color1), Color.green(Setting.color2));
		private int minB = Math.min(Color.blue(Setting.color1), Color.blue(Setting.color2));
		private int maxR = Math.max(Color.red(Setting.color1), Color.red(Setting.color2));
		private int maxG = Math.max(Color.green(Setting.color1), Color.green(Setting.color2));
		private int maxB = Math.max(Color.blue(Setting.color1), Color.blue(Setting.color2));

		private final Runnable drawFrame = new Runnable() {
			public void run() {
				drawFrame();
			}
		};

		TileEngine() {
			Setting.loadPreferences(getApplicationContext());
			stroke.setColor(0xff000000);
			stroke.setAntiAlias(true);
			stroke.setStrokeWidth(Setting.strokeWidth);
			stroke.setStrokeCap(Paint.Cap.BUTT);
			stroke.setStyle(Paint.Style.STROKE);
			fill.setColor(0xff333333);
			fill.setAntiAlias(true);
			fill.setStyle(Paint.Style.FILL);
			startTime = SystemClock.elapsedRealtime();
		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			setTouchEventsEnabled(true);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			handler.removeCallbacks(drawFrame);
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			minR = Math.min(Color.red(Setting.color1), Color.red(Setting.color2));
			minG = Math.min(Color.green(Setting.color1), Color.green(Setting.color2));
			minB = Math.min(Color.blue(Setting.color1), Color.blue(Setting.color2));
			maxR = Math.max(Color.red(Setting.color1), Color.red(Setting.color2));
			maxG = Math.max(Color.green(Setting.color1), Color.green(Setting.color2));
			maxB = Math.max(Color.blue(Setting.color1), Color.blue(Setting.color2));
			if (visible) {
				drawFrame();
			} else {
				handler.removeCallbacks(drawFrame);
			}
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			super.onSurfaceChanged(holder, format, width, height);
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			super.onSurfaceCreated(holder);
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			handler.removeCallbacks(drawFrame);
		}

		@Override
		public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep,
				int xPixels, int yPixels) {
		}

		/*
		 * Draw one frame of the animation.
		 */
		void drawFrame() {
			final SurfaceHolder holder = getSurfaceHolder();
			Canvas c = null;
			try {
				c = holder.lockCanvas();
				if (c != null) {
					c.drawColor(0xff000000);
					drawTiles(c, Setting.tileSize);
				}
			} finally {
				if (c != null)
					holder.unlockCanvasAndPost(c);
			}
			handler.postDelayed(drawFrame, 10000 / Setting.speed);
		}

		void drawTiles(Canvas c, int size) {
			for (int x = 0; x < c.getWidth(); x += size) {
				for (int y = 0; y < c.getHeight(); y += size) {
					drawSquare(c, x, y, size);
				}
			}
		}

		void drawSquare(Canvas c, int x, int y, int size) {
			if (Setting.mixColors) {
				long now = SystemClock.elapsedRealtime();
				float seed = (float) (now - startTime)
						/ (1000 / (Setting.randomPattern ? (float) Math.random() : Setting.pattern));
				int r = (int) (Math.abs(Math.sin(seed)) * maxR);
				int g = (int) (Math.abs(Math.sin(seed)) * maxG);
				int b = (int) (Math.abs(Math.sin(seed)) * maxB);
				if (r < minR) {
					r = minR;
				}
				if (g < minG) {
					g = minG;
				}
				if (b < minB) {
					b = minB;
				}
				fill.setColor(Color.argb(0xff, r, g, b));
			} else {
				fill.setColor(Math.random() > 0.5 ? Setting.color1 : Setting.color2);
			}
			c.drawRect(x, y, x + size, y + size, stroke);
			c.drawRect(x + Setting.strokeWidth - 1, y + Setting.strokeWidth - 1, x + size
					- Setting.strokeWidth + 1, y + size - Setting.strokeWidth + 1, fill);
		}
	}
}

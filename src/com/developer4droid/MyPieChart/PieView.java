package com.developer4droid.MyPieChart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created with IntelliJ IDEA.
 * User: roger sent2roger@gmail.com
 * Date: 07.02.13
 * Time: 19:15
 */
public class PieView extends View {

	private ShapeDrawable[] mDrawables;
	private Games games;

	public PieView(Context context) {
		super(context);
		init(context);
	}

	PieView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	PieView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PieView(Context context, Games games) {
		super(context);
		this.games = games;
		init(context);
	}

	private void init(Context context) {
		setFocusable(true);

		mDrawables = new ShapeDrawable[4];

		int totalDegree = 360;
		// set wins
		// calc percent
		int startDegree = -90;

		float winsPercent = ((float) games.getWins() / games.getTotal());
		int winsDegree = (int) (totalDegree * winsPercent);

		float losePercent = ((float) games.getLosses() / games.getTotal());
		int lossesDegree = (int) (totalDegree * losePercent);

		int lossesStartDegree = startDegree - winsDegree;


		float drawPercent = ((float) games.getDraws() / games.getTotal());
		int drawDegree = (int) (totalDegree * drawPercent);

		int drawStartDegree = lossesStartDegree - lossesDegree;

		mDrawables[0] = new MyShapeDrawable(new ArcShape(startDegree, -winsDegree));
		mDrawables[1] = new MyShapeDrawable(new ArcShape(lossesStartDegree, -lossesDegree));
		mDrawables[2] = new MyShapeDrawable(new ArcShape(drawStartDegree, -drawDegree));
		mDrawables[3] = new ShapeDrawable(new OvalShape());

		mDrawables[3].getPaint().setColor(0xFFFFFFFF);

		int greenColor1 = context.getResources().getColor(R.color.green_1);
		int greenColor2 = context.getResources().getColor(R.color.green_2);
		mDrawables[0].getPaint().setShader(makeRadial(greenColor1, greenColor2));

		int orangeColor1 = context.getResources().getColor(R.color.orange_1);
		int orangeColor2 = context.getResources().getColor(R.color.orange_2);
		mDrawables[1].getPaint().setShader(makeRadial(orangeColor1, orangeColor2));

		int greyColor1 = context.getResources().getColor(R.color.grey_1);
		int greyColor2 = context.getResources().getColor(R.color.grey_2);
		mDrawables[2].getPaint().setShader(makeRadial(greyColor1, greyColor2));

		{
			MyShapeDrawable msd = (MyShapeDrawable) mDrawables[0];
			msd.getStrokePaint().setStrokeWidth(1);
		}
		{
			MyShapeDrawable msd = (MyShapeDrawable) mDrawables[1];
			msd.getStrokePaint().setStrokeWidth(1);
		}
		{
			MyShapeDrawable msd = (MyShapeDrawable) mDrawables[2];
			msd.getStrokePaint().setStrokeWidth(1);
		}
	}

	private static Shader makeRadial(int color1, int color2) {
//			float x, float y, float radius, int color0, int color1, Shader.TileMode tile
		return new RadialGradient(150, 150, 150, color1, color2, Shader.TileMode.CLAMP);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		canvas.drawColor(0xFFFFFFFF);
		drawMyShapes(canvas);
	}


	private void drawMyShapes(Canvas canvas) {
		int x = 10;
		int y = 10;
		int width = 300;
		int height = 300;

		int overlaySize = width / 2 + 20;

		// pie 1
		mDrawables[0].setBounds(x, y, x + width, y + height);
		mDrawables[0].draw(canvas);

		// pie 2
		mDrawables[1].setBounds(x, y, x + width, y + height);
		mDrawables[1].draw(canvas);

		// pie 3
		mDrawables[2].setBounds(x, y, x + width, y + height);
		mDrawables[2].draw(canvas);

		// white overlay
		canvas.save();
		int xOffset = width / 2 - overlaySize / 2;
		canvas.translate(xOffset, xOffset);
		mDrawables[3].setBounds(x, y, x + overlaySize, y + overlaySize);
		mDrawables[3].draw(canvas);
		canvas.restore();
	}

	public void setGames(Games games) {
		this.games = games;
	}


	private static class MyShapeDrawable extends ShapeDrawable {
		private Paint mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

		public MyShapeDrawable(Shape s) {
			super(s);
			mStrokePaint.setStyle(Paint.Style.STROKE);
			mStrokePaint.setColor(0x26FFFFFF);
		}

		public Paint getStrokePaint() {
			return mStrokePaint;
		}

		@Override
		protected void onDraw(Shape s, Canvas c, Paint p) {
			s.draw(c, p);
			s.draw(c, mStrokePaint);
		}
	}
}
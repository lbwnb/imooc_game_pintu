package com.imooc.game.pintu.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.imooc.game.utils.ImagePiece;

import java.util.ArrayList;
import java.util.List;

public class GamePintuLayout extends RelativeLayout {

    private int mColum = 3;
    /**
     * 容器的内边距
     */
    private int mPadding;

    /**
     *每张小图之间的距离（横、纵） dp
     */
    private int mMargin = 3;

    private ImageView[] mGamePintuItems;

    private int mItemWidth;

    /**
     * 游戏的图片
     */
    private Bitmap mBitmap;

    private int mWidth;

    private List<ImagePiece>mItemBitmaps;

    private boolean once;



    public GamePintuLayout(Context context) {
        this(context,null);
    }
    public GamePintuLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public GamePintuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,3,getResources().getDisplayMetrics());

        mPadding = min(getPaddingLeft(),getPaddingRight(),getPaddingTop(),getPaddingBottom());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
       //取宽和高中的小值
        mWidth = Math.min(getMeasuredHeight(),getMeasuredWidth());

        if (!once){

            //进行切图，以及排序
            initBitmap();

            //设置ImageView(Item)的宽高属性
            initItem();

            once = true;

        }
        setMeasuredDimension(mWidth,mWidth);
    }

    /**
     *
     */
    private void initBitmap() {

        if (mBitmap == null){
            mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable)
        }
    }

    /**
     * 获取多个参数的最小值
     */
    private int min(int... params) {
int min = params[0];

for (int param:params){
    if (param<min)
        min = param;
}
        return min;
    }


}

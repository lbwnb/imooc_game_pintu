package com.imooc.game.pintu.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.qiux.imooc_game_pintu.R;
import com.imooc.game.utils.ImagePiece;
import com.imooc.game.utils.ImageSplitterUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GamePintuLayout extends RelativeLayout implements View.OnClickListener {

    private int mColumn = 3;
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
     * 设置ImageView(Item)的宽度等属性
     */
    private void initItem() {
        mItemWidth = (mWidth = mPadding*2-mMargin*(mColumn-1))/mColumn;
        mGamePintuItems = new ImageView[mColumn*mColumn];
        //生成我们的Item，设置Rule
        for (int i=0;i<mGamePintuItems.length;i++){
            ImageView item = new ImageView(getContext());
            item.setOnClickListener(this);
            item.setImageBitmap(mItemBitmaps.get(i).getBitmap());

            mGamePintuItems[i]=item;
            item.setId(i+1);

            //在Item中的tag中存储index
            item.setTag(i+"_"+mItemBitmaps.get(i).getIndex());

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mItemWidth,mItemWidth);

            //设置Item间横向间隙，通过rightMargin
            // 不是最后一列，
            if ((i+1)%mColumn!=0 ){
                lp.rightMargin = mMargin;
            }
            //不是第一列
            if (i%mColumn!=0){
                lp.addRule(RelativeLayout.RIGHT_OF,mGamePintuItems[i-1].getId());
            }

            //如果不是第一行,设置topMargin和rule
            if ((i+1)>mColumn){
                lp.topMargin = mMargin;
                        lp.addRule(RelativeLayout.BELOW,mGamePintuItems[i-mColumn].getId());
            }
            addView(item,lp);
        }

    }

    /**
     *
     */
    private void initBitmap() {

        if (mBitmap == null){
            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image);
        }

        mItemBitmaps = ImageSplitterUtil.splitImage(mBitmap,mColumn);

        //使用sort完成我们的乱序
        Collections.sort(mItemBitmaps, new Comparator<ImagePiece>() {
            @Override
            public int compare(ImagePiece a, ImagePiece b) {

            return Math.random() > 0.5 ? 1:-1;
            }
        });
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

    private ImageView mFirst;
    private ImageView mSecond;

    @Override
    public void onClick(View v) {
        //两次点击同一
        if (mFirst == v){
            mFirst.setColorFilter(null);
            mFirst = null;
            return;
        }

        if (mFirst == null) {
            mFirst = (ImageView) v;
            mFirst.setColorFilter(Color.parseColor("#55FF0000"));

        }else{
            mSecond = (ImageView) v;

            exchangeView();
            
        }
    }

    /**
     * 交换我们的item
     */
    private void exchangeView() {
        mFirst.setColorFilter(null);

       String firstTag = (String) mFirst.getTag();
       String secondTag = (String) mSecond.getTag();

      String[] firstParams = firstTag.split("_");
      String [] secondParams = secondTag.split("_");

      Bitmap firstBitmap = mItemBitmaps.get(Integer.parseInt(firstParams[0])).getBitmap();
      mSecond.setImageBitmap(firstBitmap);
        Bitmap secondBitmap = mItemBitmaps.get(Integer.parseInt(secondParams[0])).getBitmap();
        mFirst.setImageBitmap(secondBitmap);

        mFirst = mSecond = null;


    }
}

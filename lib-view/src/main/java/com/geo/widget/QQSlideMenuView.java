package com.geo.widget;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.geo.util.ScreenUtil;

/**
 * Created by liwei on 2018/4/14.
 * QQ侧滑菜单
 */
public class QQSlideMenuView extends FrameLayout {
    private VelocityTracker mVelocityTracker;//速度检测，速度超过一定值就让View直接滚动到头
    int mTouchSlop;//滑动的最小距离
    private ViewGroup mLeftView;//左边的菜单
    private ViewGroup mRightView;//右边View
    private int mMaxOffset;//菜单最大偏移量
    private int mLeftOffset = 150;//菜单滑到左边最大的负距离
    private Toolbar mToolbar;

    public QQSlideMenuView(Context context) {
        super(context);
        init();
    }
    public QQSlideMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public QQSlideMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public void setToolbar(Toolbar toolbar) {
        this.mToolbar = toolbar;
    }
    private void init() {
        mVelocityTracker = VelocityTracker.obtain();
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledPagingTouchSlop();
        mMaxOffset = (int) (ScreenUtil.getScreenWidth(getContext()) * 0.8);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLeftView = (ViewGroup) getChildAt(0);
        mRightView = (ViewGroup) getChildAt(1);
        mLeftView.setTranslationX(-mLeftOffset);
    }
    private float mLastX;
    private int mLastDistance;
    private boolean iSLeft;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        mVelocityTracker.addMovement(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float distance = event.getX() - mLastX;
                if (Math.abs(distance) < mTouchSlop) {//如果滑动的距离小于最小距离就不算是滚动了
                    return false;
                }
                if (distance < 0) {
                    iSLeft = true;
                } else {
                    iSLeft = false;
                }
                smoothScroll((int) distance);
                mLastDistance = (int) Math.abs(distance);
                break;
            case MotionEvent.ACTION_UP:
                mLastDistance = 0;
                mVelocityTracker.computeCurrentVelocity(1000);
                animaTranslate();
                break;
        }
        return true;
    }

    private void animaTranslate() {
        ValueAnimator animator = null;
        ValueAnimator animator1 = null;
        if (iSLeft) {  //左滑动
            if (isAnima()) {
                animator = ValueAnimator.ofInt((int) mRightView.getTranslationX(), 0);
                animator1=ValueAnimator.ofInt((int) mLeftView.getTranslationX(), -mLeftOffset);
            } else {
                animator = ValueAnimator.ofInt((int) mRightView.getTranslationX(), mMaxOffset);
                animator1=ValueAnimator.ofInt((int) mLeftView.getTranslationX(),0);
            }
        } else {  //右滑动
            if (isAnima()) {
                animator = ValueAnimator.ofInt((int) mRightView.getTranslationX(), 0);
                animator1=ValueAnimator.ofInt((int) mLeftView.getTranslationX(), -mLeftOffset);
            } else {
                animator1=ValueAnimator.ofInt((int) mLeftView.getTranslationX(),0);
                animator = ValueAnimator.ofInt((int) mRightView.getTranslationX(), mMaxOffset);
            }
        }
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mRightView.setTranslationX(value);
                handleToolbar();
            }
        });
        animator.setDuration(300);
        animator.start();
        //左边菜单动画
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mLeftView.setTranslationX(value);
            }
        });
        animator1.setDuration(300);
        animator1.start();
    }
    /**
     * 判断是是需要自动滚到左边还是右边
     *
     * @return
     */
    private boolean isAnima() {
        int transX = (int) mRightView.getTranslationX();
        mVelocityTracker.computeCurrentVelocity(1000);
        int speed = (int) mVelocityTracker.getYVelocity();
        if (Math.abs(speed) > 400) {
            return iSLeft;
        }
        if (Math.abs(transX) <= mMaxOffset / 2) {
            return true;
        }
        return false;
    }

    /**
     * 菜单和主界面内容随着手指慢慢滚动
     * @param dx
     */
    private void smoothScroll(int dx) {
        int rScrollX = (int) mRightView.getTranslationX();
        if (rScrollX <= 0 && iSLeft) {  //如果右边主页已经全屏显示就不再滚动了
            return;
        }
        if (rScrollX >= mMaxOffset && !iSLeft) {//如果已经滑到最右边不能再滑了
            return;
        }
        handleToolbar();
        if (iSLeft) {//向左滑动
            //右边主题内容
            mRightView.setTranslationX(mMaxOffset + dx);
            //左边菜单
            mLeftView.setTranslationX((float) (dx*0.16));
        } else {  //向右滑动
            //右边主题内容
            mRightView.setTranslationX(dx);
            //左边菜单
            if(mLeftView.getTranslationX()>=0){
             return;
            }
            mLeftView.setTranslationX((float) (-mLeftOffset+dx*0.16));
        }
    }
    private void handleToolbar() {
        if (mToolbar == null) {
            return;
        }
        int rScrollX = (int) mRightView.getTranslationX();
        if (iSLeft && Math.abs(rScrollX) < mMaxOffset / 2) {//向左滑动
            float factor = (float)(mMaxOffset/2-Math.abs(rScrollX))/(float)(mMaxOffset/2);
            Log.e("LEFT",factor+"  ");
            int alpha = (int) (factor*255);
            alpha=alpha>255?255:alpha;
            //修改Toolbar颜色
            mToolbar.getBackground().setAlpha(alpha);
            //设置背景色
            if(factor>=0.6){
                View view = mRightView.getChildAt(mRightView.getChildCount()-1);
                view.setAlpha(1-factor);
            }
        } else if (!iSLeft && Math.abs(rScrollX) >= mMaxOffset / 2) {  //如果向右滑动就直接
            float factor = (float)(mMaxOffset-Math.abs(rScrollX))/(float)mMaxOffset;
            int alpha = (int) (factor*255);
            alpha=alpha<0?0:alpha;
            mToolbar.getBackground().setAlpha(alpha);
            //设置背景色
            if(factor<=0.5){
                View view = mRightView.getChildAt(mRightView.getChildCount()-1);
                view.setAlpha((float) (0.4-factor));
            }

        }
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

}

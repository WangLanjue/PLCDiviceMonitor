package com.example.divicemonitor2.tools;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import com.example.divicemonitor2.R;


//下拉选择框
public class DropEditText extends androidx.appcompat.widget.AppCompatEditText implements AdapterView.OnItemClickListener, PopupWindow.OnDismissListener {

    private Drawable mDrawable; // 显示的图
    private PopupWindow mPopupWindow; // 点击图片弹出的popWindow对象
    private ListView mPopListView; // popWindow的布局
    private int mDropDrawableResId; // 下拉图标
    private int mRiseDrawableResID; // 上拉图标
    public DropEditText(Context context) {
        this(context, null);
    }

    public DropEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DropEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mPopListView = new ListView(context);
        mDropDrawableResId = R.drawable.angledown; // 设置下拉图标
        mRiseDrawableResID = R.drawable.angleup; // 设置上拉图标
        showDropDrawable(); // 默认显示下拉图标
        mPopListView.setOnItemClickListener(this);
    }

    /**
     * 我们无法直接给EditText设置点击事件，只能通过按下的位置来模拟点击事件
     * 当我们按下的位置在图标包括图标到控件右边的间距范围内均算有效
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                int start = getWidth() - getTotalPaddingRight() + getPaddingRight(); // 起始位置
                int end = getWidth(); // 结束位置
                boolean available = (event.getX() > start) && (event.getX() < end);
                if (available) {
                    closeSoftInput();
                    showPopWindow();
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mPopupWindow = new PopupWindow(mPopListView, getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE)); // 设置popWindow背景颜色
            mPopupWindow.setFocusable(true); // 让popWindow获取焦点
            mPopupWindow.setOnDismissListener(this);
        }
    }

    private void showPopWindow() {
        mPopupWindow.showAsDropDown(this, 0, 5);
        showRiseDrawable();
    }

    private void showDropDrawable() {
        mDrawable = getResources().getDrawable(mDropDrawableResId);
        mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], mDrawable, getCompoundDrawables()[3]);
    }

    private void showRiseDrawable() {
        mDrawable = getResources().getDrawable(mRiseDrawableResID);
        mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], mDrawable, getCompoundDrawables()[3]);
    }

    public void setAdapter(BaseAdapter adapter) {
        mPopListView.setAdapter(adapter);
    }

    private void closeSoftInput() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.setText(mPopListView.getAdapter().getItem(position).toString());


        // 需要一个条件激活diviceinformactivity中的ID匹配填写各text中数据
        mPopupWindow.dismiss();
    }

    @Override
    public void onDismiss() {
        showDropDrawable(); // 当popWindow消失时显示下拉图标
    }
}

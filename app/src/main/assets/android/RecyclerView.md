### 去掉边界阴影

    android:fadingEdge="none"
    android:overScrollMode="never"
    
### 解决RecyclerView和ScrollView的冲突
    
    1.recyclerView需要一个父布局，并添加属性android:descendantFocusability="blocksDescendants"
     <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        //务必添加此属性
        android:descendantFocusability="blocksDescendants">

        <android.support.v7.widget.RecyclerView
            android:id="@+id/recyler_view"
            android:fadingEdge="none"
            android:overScrollMode="never"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />
    </RelativeLayout>

    2.mRecyclerView.setLayoutManager(new FullyLinearLayoutManager(mContext));
    mRecyclerView.setNestedScrollingEnabled(false);//限制自己的滑动，紧依靠ScrollView滑动
    同样：
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
        @Override
        public boolean canScrollVertically() {
            //返回false限制自身滑动
            return false;
        }
    };
    
### 滚动到底部判断

    RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
    RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
    
    public static boolean isSlideToBottom(RecyclerView recyclerView) {    
       if (recyclerView == null) return false; 
       if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() 
            >= recyclerView.computeVerticalScrollRange())   
         return true;  
       return false;
    }



### TODO 横向ViewPager和横向RecyclerView直接的滑动冲突

    
### 纵向的RecyclerView(ListView)与横向RecyclerView之间的滑动冲突

### 优化体验和入坑

    https://www.jianshu.com/p/90c31e97cc55

#### 多布局

    最好不要和ScrollView嵌套，可以使用ViewType区别类型，或者AddHeaderView或
    AddFooterView
    
#### List布局和Grid布局切换

    1.删除原先的分割线，mRecyclerView.removeItemDecoration(divider1);
        否则item会多次offset偏移
    2.重新addItemDecoration和setLayoutManager
    3.mAdapter.notifyDataSetChanged();
    
### 解决与下拉刷新冲突

     mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
              LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
            mIsCanRefresh = firstCompletelyVisibleItemPosition <= 0;
        }
    
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }
    });
    
    如果RecycleView上方还有其他ViewGroup，需要在其Activity事件分发中设置触摸是否在TopLayout中:
     @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //手指按下：判断是否再HeaderLayout
                if (mCurrentFragment != null && mCurrentFragment instanceof HomeFragment) {
                    HomeFragment fragment = ((HomeFragment) mCurrentFragment);
                    View headerLayout = fragment.getHeaderLayout();
                    if (isTouchPointInView(headerLayout, (int) ev.getRawX(), (int) ev.getRawY())) {
                        fragment.setCanRefresh(true);
                    } else {
                        fragment.setCanRefresh(fragment.initIsCanRefresh());
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
    
    Fragment中initIsCanRefresh:
    public boolean initIsCanRefresh() {
        int topRowVerticalPosition =
                (mRecyclerView == null || mRecyclerView.getChildCount() == 0) ? 0 : mRecyclerView.getChildAt(0).getTop();
        return topRowVerticalPosition >= 0;
    }
    
    /**
     * (x,y)是否在view的区域内
     */
    private boolean isTouchPointInView(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        //view.isClickable() &&
        if (y >= top && y <= bottom && x >= left
                && x <= right) {
            return true;
        }
        return false;
    }

    


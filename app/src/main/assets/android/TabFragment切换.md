### tab切换找回Fragment实例

    public class MainActivity extends BaseActivity {
    
        private static final int INDEX_HOME = 0;
        private static final int INDEX_EXAM = 1;
        private static final int INDEX_MINE = 2;
    
        /**
         * 当前被选中
         */
        private int mCurrentIndex = INDEX_HOME;
        /**
         * Fragment集合
         */
        private Fragment[] mFragments = new Fragment[3];
    
    
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            initFragment(savedInstanceState);
        }
    
        @Override
        protected void onSaveInstanceState(@NonNull Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putInt(TAB_INDEX, mCurrentIndex);
        }
    
        protected void initListeners() {
            mBottomBarLayout.setOnBottomItemSelectedListener((item, position) -> {
                switch (position) {
                    case 1:
                        selectFragment(INDEX_EXAM);
                        break;
                    case 2:
                        selectFragment(INDEX_MINE);
                        break;
                    default:
                        selectFragment(INDEX_HOME);
                        break;
                }
            });
        }
    
        /**
         * 初始化Fragment
         */
        private void initFragment(Bundle savedInstanceState) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment homeFragment = fragmentManager.findFragmentByTag("HomeFragment");
            Fragment examFragment = fragmentManager.findFragmentByTag("IndexFragment");
            Fragment mineFragment = fragmentManager.findFragmentByTag("MineFragment");
    
            if (homeFragment == null) {
                homeFragment = HomeFragment.newInstance();
            }
            if (examFragment == null) {
                examFragment = HiRouter.getInstance().getExamRouter().providerIndexFragment();
            }
            if (mineFragment == null) {
                mineFragment = HiRouter.getInstance().getAccountRouter().providerMineFragment();
            }
    
            mFragments[INDEX_HOME] = homeFragment;
            mFragments[INDEX_EXAM] = examFragment;
            mFragments[INDEX_MINE] = mineFragment;
    
            if (savedInstanceState == null) {
                fragmentManager.beginTransaction()
                        .add(R.id.fp_fl_main_container, homeFragment, "HomeFragment")
                        .add(R.id.fp_fl_main_container, examFragment, "IndexFragment")
                        .add(R.id.fp_fl_main_container, mineFragment, "MineFragment")
                        .hide(examFragment)
                        .hide(mineFragment)
                        .show(homeFragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            } else {
                mCurrentIndex = savedInstanceState.getInt(TAB_INDEX, INDEX_HOME);
            }
            selectFragment(mCurrentIndex);
        }
    
        /**
         * Fragment选择
         */
        private void selectFragment(int selectedIndex) {
            if (selectedIndex != mCurrentIndex) {
                getSupportFragmentManager().beginTransaction()
                        .hide(mFragments[mCurrentIndex])
                        .show(mFragments[selectedIndex])
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
                mCurrentIndex = selectedIndex;
            }
        }
    }

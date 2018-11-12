package com.wind.photopreview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wind.photopreview.event.PhotoRemoveEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wind on 2017/3/2.
 */

public class PhotoPreviewActivity extends FragmentActivity {

    public static final String EXTRA_KEY_PHOTOS = "extra_key_photos";
    public static final String EXTRA_KEY_FRAGMENT_NAME = "extra_key_fragment_name";
    public static final String EXTRA_SHOW_DELETE_BTN = "extra_show_delete_btn";
    public static final String EXTRA_KEY_START_POSITION = "extra_key_position";
    public static final String EXTRA_KEY_CURRENT_POSITION = "extra_key_current_position";
    private boolean mIsReturning;

    private int mCurrentPosition;
    private int mStartingPosition;
    private static final String STATE_CURRENT_PAGE_POSITION = "state_current_page_position";
    private final SharedElementCallback mCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (mIsReturning) {
                ImageView sharedElement = mCurrentFragment.getPreviewView();
                if (sharedElement == null) {
                    // If shared element is null, then it has been scrolled off screen and
                    // no longer visible. In this case we cancel the shared element transition by
                    // removing the shared element from the shared elements map.
                    names.clear();
                    sharedElements.clear();
                } else if (mStartingPosition != mCurrentPosition) {
                    // If the user has swiped to a different ViewPager page, then we need to
                    // remove the old shared element and replace it with the new shared element
                    // that should be transitioned instead.
                    names.clear();
                    names.add(ViewCompat.getTransitionName(sharedElement));
                    sharedElements.clear();
                    sharedElements.put(ViewCompat.getTransitionName(sharedElement), sharedElement);
                }
            }
        }
    };

    private PhotoPreviewFragment mCurrentFragment;
    private ArrayList<PhotoPreview> mPhotos;
    private PhotoFragmentPagerAdapter mPageAdapter;
    private ViewPager mViewPager;

   // TextView tv_fraction;
    View toolbar_iv_back;
    private String mFragmentName;

    protected int getLayoutRes(){
        return R.layout.activity_photo_preview;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(getLayoutRes());
        ActivityCompat.postponeEnterTransition(this);
        EventBus.getDefault().register(this);
        setEnterSharedElementCallback(mCallback);
        mFragmentName=getIntent().getStringExtra(EXTRA_KEY_FRAGMENT_NAME);
        if (TextUtils.isEmpty(mFragmentName)){
            mFragmentName=PhotoPreviewFragment.class.getCanonicalName();
        }
        mPhotos= (ArrayList<PhotoPreview>) getIntent().getSerializableExtra(EXTRA_KEY_PHOTOS);
        mStartingPosition=getIntent().getIntExtra(EXTRA_KEY_START_POSITION,0);
        if (savedInstanceState == null) {
            mCurrentPosition = mStartingPosition;
        } else {
            mCurrentPosition = savedInstanceState.getInt(STATE_CURRENT_PAGE_POSITION);
        }

        mViewPager= findViewById(R.id.pager);

        mPageAdapter =  new PhotoFragmentPagerAdapter(getSupportFragmentManager(),new ArrayList<Fragment>());
        for(int i=0;i<mPhotos.size();i++){
            mPageAdapter.addInfo(i);
        }
        mPageAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setCurrentItem(mCurrentPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
            }
        });
        //mFragment=PhotoPreviewFragment.getInstance(photos,position);
        //replaceFragment(mFragment);

       // tv_fraction = (TextView) findViewById(R.id.tv_fraction);
        toolbar_iv_back = findViewById(R.id.toolbar_iv_back);
        toolbar_iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });




    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_CURRENT_PAGE_POSITION, mCurrentPosition);
    }


    @Override
    public void onBackPressed() {
        mIsReturning = true;
        Intent data = new Intent();

        data.putExtra(EXTRA_KEY_START_POSITION, mStartingPosition);
        data.putExtra(EXTRA_KEY_CURRENT_POSITION, mCurrentPosition);
        setResult(RESULT_OK, data);
        ActivityCompat.finishAfterTransition(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPhotoRemoveEvent(PhotoRemoveEvent event){
       /* try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        if(mPageAdapter.fragments.size()>0)
            mPageAdapter.fragments.remove(mCurrentPosition);
        if( mPageAdapter.fragments.size()>1&&mCurrentPosition== mPageAdapter.fragments.size() - 1) {
            //最后一个元素
            mCurrentPosition--;

        }
        mPageAdapter.notifyDataSetChanged();
        //如果照片都删除完了，就结束
        if(mPageAdapter.fragments.size()==0){
            finish();
            return;
        }


        mViewPager.setCurrentItem(mCurrentPosition);
    }

    private class PhotoFragmentPagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<Fragment> fragments;

        public PhotoFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mCurrentFragment = (PhotoPreviewFragment) object;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public  void  addInfo(int position){
            Class paramsTypes[]={int.class,int.class,PhotoPreview.class};
            try {
                Method method= Class.forName(mFragmentName).getMethod("newInstance",paramsTypes);
                Object [] params={position, mStartingPosition,mPhotos.get(position)};
                Fragment fragment= (Fragment) method.invoke(null,params);
                fragments.add(fragment);
            } catch (Exception e) {
                e.printStackTrace();
            }
           /* Fragment fragment=PhotoPreviewFragment.newInstance(position, mStartingPosition,mPhotos.get(position));
            fragments.add(fragment);*/
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        private  void  remove(String id){
            for (int i=0;i<fragments.size();i++){
                PhotoPreview mPhoto = (PhotoPreview) fragments.get(i).getArguments().getSerializable(PhotoPreviewFragment.ARGS_KEY_PHOTO);
                if(String.valueOf(mPhoto.getResId()).equals(id)){
                    fragments.get(i).onDestroyView();
                    fragments.remove(i);
//                    fragments.get(i).onDestroyView();
//                    ft.remove(fm.getFragments().get(i));
//                    ft.commit();
//                    fm.getFragments().get(i).onDestroyView();


                }
            }

           notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

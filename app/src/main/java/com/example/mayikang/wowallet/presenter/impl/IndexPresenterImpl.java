package com.example.mayikang.wowallet.presenter.impl;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.presenter.IIndexPresenter;
import com.example.mayikang.wowallet.ui.frg.FriendsFragment;
import com.example.mayikang.wowallet.ui.frg.HomeFragment;
import com.example.mayikang.wowallet.ui.frg.OwnFragment;
import com.example.mayikang.wowallet.ui.frg.OwnsFragment;
import com.example.mayikang.wowallet.ui.frg.StoreFragment;
import com.example.mayikang.wowallet.ui.frg.StoresFragment;

/**
 * Created by mayikang on 17/5/8.
 */

public class IndexPresenterImpl implements IIndexPresenter {

    private HomeFragment homeFragment;
    private StoresFragment storeFragment;
    private FriendsFragment friendsFragment;
    private OwnsFragment ownFragment;

    private Context context;

    private FragmentManager fragmentManager;
    public IndexPresenterImpl(Context context,FragmentManager fragmentManager){
        this.context=context;
        this.fragmentManager=fragmentManager;


        if(homeFragment==null){
            homeFragment=new HomeFragment();
            fragmentManager.beginTransaction().add(R.id.act_index_frg_container,homeFragment).show(homeFragment).commit();
        }


    }

    @Override
    public void replaceFragment(String frgName) {

        if(frgName==null || frgName.isEmpty()){
            return;
        }


        switch (frgName){
            case "HOME":
                hideAllFgIfNotNull();
                if(homeFragment==null){
                    homeFragment=new HomeFragment();
                    fragmentManager.beginTransaction().add(R.id.act_index_frg_container,homeFragment).show(homeFragment).commit();
                }else {
                    fragmentManager.beginTransaction().show(homeFragment).commit();

                }
                break;

            case "STORE":
                hideAllFgIfNotNull();
                if(storeFragment==null){
                    storeFragment=new StoresFragment();
                    fragmentManager.beginTransaction().add(R.id.act_index_frg_container,storeFragment).show(storeFragment).commitAllowingStateLoss();
                }else {
                    fragmentManager.beginTransaction().show(storeFragment).commitAllowingStateLoss();
                }
                break;

            case "FRIENDS":
                hideAllFgIfNotNull();
                if(friendsFragment==null){
                    friendsFragment=new FriendsFragment();
                    fragmentManager.beginTransaction().add(R.id.act_index_frg_container,friendsFragment).show(friendsFragment).commit();
                }else {
                    fragmentManager.beginTransaction().show(friendsFragment).commit();
                }
                break;

            case "OWN":
                hideAllFgIfNotNull();
                if(ownFragment==null){
                    ownFragment=new OwnsFragment();
                    fragmentManager.beginTransaction().add(R.id.act_index_frg_container,ownFragment).show(ownFragment).commit();
                }else {
                    fragmentManager.beginTransaction().show(ownFragment).commit();
                }
                break;
        }



    }

    @Override
    public void pullData() {
        if(null!=homeFragment){
            homeFragment.pullData();
        }
    }


    /**
     * 隐藏所有fragment
     */
    private void hideAllFgIfNotNull(){
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        if(homeFragment!=null){
            transaction.hide(homeFragment);
        }



        
        if(storeFragment!=null){
            transaction.hide(storeFragment);
        }

        if(friendsFragment!=null){
            transaction.hide(friendsFragment);
        }

        if(ownFragment!=null){
            transaction.hide(ownFragment);
        }

        //提交
        transaction.commitAllowingStateLoss();
    }




}

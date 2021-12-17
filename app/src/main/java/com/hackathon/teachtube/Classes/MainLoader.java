package com.hackathon.teachtube.Classes;

import android.view.View;
import android.widget.RelativeLayout;

public class MainLoader
{

    public static void Loader(boolean start , RelativeLayout LL_loader)
    {
        RelativeLayout rootView = (RelativeLayout) LL_loader.getParent();
        if (start) {
            LL_loader.setVisibility(View.VISIBLE);
            //LL_loader.findViewById(R.id.imageBG).setClickable(false);

            rootView.getChildAt(0).setVisibility(View.GONE);
            rootView.getChildAt(0).setEnabled(false);

        }else
        {
            LL_loader.setVisibility(View.GONE);
            //LL_loader.setClickable(true);
            rootView.getChildAt(0).setVisibility(View.VISIBLE);
            rootView.getChildAt(0).setEnabled(true);
        }
    }

}

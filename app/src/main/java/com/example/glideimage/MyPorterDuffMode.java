package com.example.glideimage;

import android.graphics.Bitmap;

public class MyPorterDuffMode
{

    public Bitmap applyOverlayMode(Bitmap srcBmp, Bitmap destBmp)
    {
        int width = srcBmp.getWidth();
        int height = srcBmp.getHeight();
        int srcPixels[] = new int[width * height];;
        int destPixels[] = new int[width * height];
        int resultPixels[] = new int[width * height];
        int aS = 0, rS = 0, gS = 0, bS = 0;
        int rgbS = 0;
        int aD = 0, rD = 0, gD = 0, bD = 0;
        int rgbD = 0;

        try
        {
            srcBmp.getPixels(srcPixels, 0, width, 0, 0, width, height);
            destBmp.getPixels(destPixels, 0, width, 0, 0, width, height);
            srcBmp.recycle();
            destBmp.recycle();
        }
        catch(IllegalArgumentException e)
        {
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
        }

        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                rgbS = srcPixels[y*width + x];
                aS = (rgbS >> 24) & 0xff;
                rS = (rgbS >> 16) & 0xff;
                gS = (rgbS >>  8) & 0xff;
                bS = (rgbS      ) & 0xff;

                rgbD = destPixels[y*width + x];
                aD = ((rgbD >> 24) & 0xff);
                rD = (rgbD >> 16) & 0xff;
                gD = (rgbD >>  8) & 0xff;
                bD = (rgbD      )  & 0xff;

                //overlay-mode
                rS = overlay_byte(rD, rS, aS, aD);
                gS = overlay_byte(gD, gS, aS, aD);
                bS = overlay_byte(bD, bS, aS, aD);
                aS = aS + aD - Math.round((aS * aD)/255f);

                resultPixels[y*width + x] = ((int)aS << 24) | ((int)rS << 16) | ((int)gS << 8) | (int)bS;
            }
        }

        return Bitmap.createBitmap(resultPixels, width, height, srcBmp.getConfig());
    }

    // kOverlay_Mode
    int overlay_byte(int sc, int dc, int sa, int da) {
        int tmp = sc * (255 - da) + dc * (255 - sa);
        int rc;
        if (2 * dc <= da) {
            rc = 2 * sc * dc;
        } else {
            rc = sa * da - 2 * (da - dc) * (sa - sc);
        }
        return clamp_div255round(rc + tmp);
    }


    int clamp_div255round(int prod) {
        if (prod <= 0) {
            return 0;
        } else if (prod >= 255*255) {
            return 255;
        } else {
            return Math.round((float)prod/255);
        }
    }

}

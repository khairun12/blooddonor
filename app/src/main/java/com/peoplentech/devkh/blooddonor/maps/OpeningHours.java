package com.peoplentech.devkh.blooddonor.maps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 7/23/2018.
 */

public class OpeningHours {

   /* @SerializedName("open_now")
    @Expose
    private Boolean openNow;
    @SerializedName("weekday_text")
    @Expose
    private List<Object> weekdayText = new ArrayList<Object>();*/

    /**
     *
     * @return
     * The openNow
     */
    /*public Boolean getOpenNow() {
        return openNow;
    }*/

    /**
     *
     * @param openNow
     * The open_now
     */
    /*public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }*/

    /**
     *
     * @return
     * The weekdayText
     */
    /*public List<Object> getWeekdayText() {
        return weekdayText;
    }*/

    /**
     *
     * @param weekdayText
     * The weekday_text
     */
   /* public void setWeekdayText(List<Object> weekdayText) {
        this.weekdayText = weekdayText;
    }

}*/
/**
 * new code
 */

@SerializedName("open_now")

@Expose

private Boolean openNow;

    @SerializedName("weekday_text")

    @Expose

    private List<Object> weekdayText = new ArrayList<Object>();



    /**

     *

     * @return

     * The openNow

     */

    public Boolean getOpenNow() {

        return openNow;

    }



    /**

     *

     * @param openNow

     * The open_now

     */

    public void setOpenNow(Boolean openNow) {

        this.openNow = openNow;

    }



    /**

     *

     * @return

     * The weekdayText

     */

    public List<Object> getWeekdayText() {

        return weekdayText;

    }



    /**

     *

     * @param weekdayText

     * The weekday_text

     */

    public void setWeekdayText(List<Object> weekdayText) {

        this.weekdayText = weekdayText;

    }



}

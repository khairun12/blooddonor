package com.peoplentech.devkh.blooddonor.maps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 7/23/2018.
 */

public class Geometry {

   /*@SerializedName("location")
    @Expose
    private Location location;*/

    /**
     *
     * @return
     * The location
     */
    /*public Location getLocation() {
        return location;
    }*/

    /**
     *
     * @param location
     * The location
     */
  /*  public void setLocation(Location location) {
        this.location = location;
    }

}*/
/**
 * new code
 */

@SerializedName("location")

@Expose

private Location location;



    /**

     *

     * @return

     * The location

     */

    public Location getLocation() {

        return location;

    }



    /**

     *

     * @param location

     * The location

     */

    public void setLocation(Location location) {

        this.location = location;

    }



}

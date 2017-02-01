package com.reactivemobile.ukpoliceapp.objects;

import org.parceler.Parcel;

/**
 * Class represending a street-level crime report
 */
@Parcel
public class StreetLevelCrime {

    String category;

    String persistent_id;

    String location_type;

    String location_subtype;

    int id;

    Location location;

    String context;

    String month;

    OutcomeStatus outcome_status;

    public String getCategory() {
        return category;
    }

    public String getPersistent_id() {
        return persistent_id;
    }

    public String getLocation_type() {
        return location_type;
    }

    public String getLocation_subtype() {
        return location_subtype;
    }

    public int getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public String getContext() {
        return context;
    }

    public String getMonth() {
        return month;
    }

    public OutcomeStatus getOutcome_status() {
        return outcome_status;
    }

    @Parcel
    public static class Location {

        String latitude;

        String longitude;

        Street street;

        public Street getStreet() {
            return street;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        @Parcel
        public static class Street {
            int id;

            String name;

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }
        }
    }

    @Parcel
    public static class OutcomeStatus {

        String date;

        String category;

        public String getCategory() {
            return category;
        }

        public String getDate() {
            return date;
        }
    }

    @Override
    public String toString() {
        return "" + category + " [" + id + "] " + location.getStreet().getName();
    }
}

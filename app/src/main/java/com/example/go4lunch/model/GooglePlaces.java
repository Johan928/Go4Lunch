package com.example.go4lunch.model;

import java.util.List;
import java.util.Objects;

public class GooglePlaces {

    private String status;


    private List<Results> results;

    private List<String> html_attributions;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    public List<String> getHtml_attributions() {
        return html_attributions;
    }

    public void setHtml_attributions(List<String> html_attributions) {
        this.html_attributions = html_attributions;
    }

    public static class Results {
        private Opening_hours opening_hours;
       // @SerializedName("vicinity")
        private String vicinity;
        // @SerializedName("user_ratings_total")
        private int user_ratings_total;

        private List<String> types;

        private String scope;

        private String reference;

        private Double rating;

        private int price_level;

        private Plus_code plus_code;

        private String place_id;

        private List<Photos> photos;

        private String name;

        private String icon_mask_base_uri;

        private String icon_background_color;

        private String icon;

        private Geometry geometry;

        private String business_status;
        //  @SerializedName("website")
        private String website;
        // @SerializedName("formatted_phone_number")
        private String formatted_phone_number;
        // @SerializedName("formatted_address")
        private String formatted_address;


        public String getVicinity() {
            return vicinity;
        }

        public void setVicinity(String vicinity) {
            this.vicinity = vicinity;
        }

        public int getUser_ratings_total() {
            return user_ratings_total;
        }

        public void setUser_ratings_total(int user_ratings_total) {
            this.user_ratings_total = user_ratings_total;
        }

        public Opening_hours getOpening_hours() {
            return opening_hours;
        }
        public void setOpening_hours(Opening_hours opening_hours){
            this.opening_hours = opening_hours;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public Double getRating() {
            return rating;
        }

        public void setRating(Double rating) {
            this.rating = rating;
        }

        public int getPrice_level() {
            return price_level;
        }

        public void setPrice_level(int price_level) {
            this.price_level = price_level;
        }

        public Plus_code getPlus_code() {
            return plus_code;
        }

        public void setPlus_code(Plus_code plus_code) {
            this.plus_code = plus_code;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public List<Photos> getPhotos() {
            return photos;
        }

        public void setPhotos(List<Photos> photos) {
            this.photos = photos;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon_mask_base_uri() {
            return icon_mask_base_uri;
        }

        public void setIcon_mask_base_uri(String icon_mask_base_uri) {
            this.icon_mask_base_uri = icon_mask_base_uri;
        }

        public String getIcon_background_color() {
            return icon_background_color;
        }

        public void setIcon_background_color(String icon_background_color) {
            this.icon_background_color = icon_background_color;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        public String getBusiness_status() {
            return business_status;
        }

        public void setBusiness_status(String business_status) {
            this.business_status = business_status;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getFormatted_phone_number() {
            return formatted_phone_number;
        }

        public void setFormatted_phone_number(String formatted_phone_number) {
            this.formatted_phone_number = formatted_phone_number;
        }

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Results results = (Results) o;
            return user_ratings_total == results.user_ratings_total && price_level == results.price_level && Objects.equals(opening_hours, results.opening_hours) && Objects.equals(vicinity, results.vicinity) && Objects.equals(types, results.types) && Objects.equals(scope, results.scope) && Objects.equals(reference, results.reference) && Objects.equals(rating, results.rating) && Objects.equals(plus_code, results.plus_code) && Objects.equals(place_id, results.place_id) && Objects.equals(photos, results.photos) && Objects.equals(name, results.name) && Objects.equals(icon_mask_base_uri, results.icon_mask_base_uri) && Objects.equals(icon_background_color, results.icon_background_color) && Objects.equals(icon, results.icon) && Objects.equals(geometry, results.geometry) && Objects.equals(business_status, results.business_status) && Objects.equals(website, results.website) && Objects.equals(formatted_phone_number, results.formatted_phone_number) && Objects.equals(formatted_address, results.formatted_address);
        }

        @Override
        public int hashCode() {
            return Objects.hash(opening_hours, vicinity, user_ratings_total, types, scope, reference, rating, price_level, plus_code, place_id, photos, name, icon_mask_base_uri, icon_background_color, icon, geometry, business_status, website, formatted_phone_number, formatted_address);
        }

        @Override
        public String toString() {
            return "Results{" +
                    "opening_hours=" + opening_hours +
                    ", vicinity='" + vicinity + '\'' +
                    ", user_ratings_total=" + user_ratings_total +
                    ", types=" + types +
                    ", scope='" + scope + '\'' +
                    ", reference='" + reference + '\'' +
                    ", rating=" + rating +
                    ", price_level=" + price_level +
                    ", plus_code=" + plus_code +
                    ", place_id='" + place_id + '\'' +
                    ", photos=" + photos +
                    ", name='" + name + '\'' +
                    ", icon_mask_base_uri='" + icon_mask_base_uri + '\'' +
                    ", icon_background_color='" + icon_background_color + '\'' +
                    ", icon='" + icon + '\'' +
                    ", geometry=" + geometry +
                    ", business_status='" + business_status + '\'' +
                    ", website='" + website + '\'' +
                    ", formatted_phone_number='" + formatted_phone_number + '\'' +
                    ", formatted_address='" + formatted_address + '\'' +
                    '}';
        }
    }

    public static class Plus_code {

        private String global_code;

        private String compound_code;

        public String getGlobal_code() {
            return global_code;
        }

        public void setGlobal_code(String global_code) {
            this.global_code = global_code;
        }

        public String getCompound_code() {
            return compound_code;
        }

        public void setCompound_code(String compound_code) {
            this.compound_code = compound_code;
        }
    }

    public static class Photos {
        private int width;

        private String photo_reference;

        private List<String> html_attributions;

        private int height;

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getPhoto_reference() {
            return photo_reference;
        }

        public void setPhoto_reference(String photo_reference) {
            this.photo_reference = photo_reference;
        }

        public List<String> getHtml_attributions() {
            return html_attributions;
        }

        public void setHtml_attributions(List<String> html_attributions) {
            this.html_attributions = html_attributions;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

    public static class Opening_hours {
                private boolean open_now;

                public boolean getOpen_now() {
                    return open_now;
                }

                public void setOpen_now(boolean open_now) {
                    this.open_now = open_now;
                }

    }


    public static class Geometry {

        private Viewport viewport;

        private Location location;

        public Viewport getViewport() {
            return viewport;
        }

        public void setViewport(Viewport viewport) {
            this.viewport = viewport;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }

    public static class Viewport {

        private Southwest southwest;

        private Northeast northeast;

        public Southwest getSouthwest() {
            return southwest;
        }

        public void setSouthwest(Southwest southwest) {
            this.southwest = southwest;
        }

        public Northeast getNortheast() {
            return northeast;
        }

        public void setNortheast(Northeast northeast) {
            this.northeast = northeast;
        }
    }

    public static class Southwest {

        private double lng;

        private double lat;

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }

    public static class Northeast {

        private double lng;

        private double lat;

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }

    public static class Location {

        private double lng;

        private double lat;

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }
}

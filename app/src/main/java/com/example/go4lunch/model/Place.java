package com.example.go4lunch.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class Place {
    @SerializedName("status")
    private String status;
    @SerializedName("result")
    private Result result;
    @SerializedName("html_attributions")
    private List<String> html_attributions;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<String> getHtml_attributions() {
        return html_attributions;
    }

    public void setHtml_attributions(List<String> html_attributions) {
        this.html_attributions = html_attributions;
    }

    public static class Result {
        @SerializedName("website")
        private String website;
        @SerializedName("vicinity")
        private String vicinity;
        @SerializedName("utc_offset")
        private int utc_offset;
        @SerializedName("user_ratings_total")
        private int user_ratings_total;
        @SerializedName("url")
        private String url;
        @SerializedName("types")
        private List<String> types;
        @SerializedName("reviews")
        private List<Reviews> reviews;
        @SerializedName("reference")
        private String reference;
        @SerializedName("rating")
        private double rating;
        @SerializedName("plus_code")
        private Plus_code plus_code;
        @SerializedName("place_id")
        private String place_id;
        @SerializedName("photos")
        private List<Photos> photos;
        @SerializedName("opening_hours")
        private Opening_hours opening_hours;
        @SerializedName("name")
        private String name;
        @SerializedName("international_phone_number")
        private String international_phone_number;
        @SerializedName("icon_mask_base_uri")
        private String icon_mask_base_uri;
        @SerializedName("icon_background_color")
        private String icon_background_color;
        @SerializedName("icon")
        private String icon;
        @SerializedName("geometry")
        private Geometry geometry;
        @SerializedName("formatted_phone_number")
        private String formatted_phone_number;
        @SerializedName("formatted_address")
        private String formatted_address;
        @SerializedName("business_status")
        private String business_status;
        @SerializedName("adr_address")
        private String adr_address;
        @SerializedName("address_components")
        private List<Address_components> address_components;

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getVicinity() {
            return vicinity;
        }

        public void setVicinity(String vicinity) {
            this.vicinity = vicinity;
        }

        public int getUtc_offset() {
            return utc_offset;
        }

        public void setUtc_offset(int utc_offset) {
            this.utc_offset = utc_offset;
        }

        public int getUser_ratings_total() {
            return user_ratings_total;
        }

        public void setUser_ratings_total(int user_ratings_total) {
            this.user_ratings_total = user_ratings_total;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public List<Reviews> getReviews() {
            return reviews;
        }

        public void setReviews(List<Reviews> reviews) {
            this.reviews = reviews;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
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

        public Opening_hours getOpening_hours() {
            return opening_hours;
        }

        public void setOpening_hours(Opening_hours opening_hours) {
            this.opening_hours = opening_hours;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getInternational_phone_number() {
            return international_phone_number;
        }

        public void setInternational_phone_number(String international_phone_number) {
            this.international_phone_number = international_phone_number;
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

        public String getBusiness_status() {
            return business_status;
        }

        public void setBusiness_status(String business_status) {
            this.business_status = business_status;
        }

        public String getAdr_address() {
            return adr_address;
        }

        public void setAdr_address(String adr_address) {
            this.adr_address = adr_address;
        }

        public List<Address_components> getAddress_components() {
            return address_components;
        }

        public void setAddress_components(List<Address_components> address_components) {
            this.address_components = address_components;
        }
    }

    public static class Reviews {
        @SerializedName("time")
        private int time;
        @SerializedName("text")
        private String text;
        @SerializedName("relative_time_description")
        private String relative_time_description;
        @SerializedName("rating")
        private int rating;
        @SerializedName("profile_photo_url")
        private String profile_photo_url;
        @SerializedName("language")
        private String language;
        @SerializedName("author_url")
        private String author_url;
        @SerializedName("author_name")
        private String author_name;

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getRelative_time_description() {
            return relative_time_description;
        }

        public void setRelative_time_description(String relative_time_description) {
            this.relative_time_description = relative_time_description;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public String getProfile_photo_url() {
            return profile_photo_url;
        }

        public void setProfile_photo_url(String profile_photo_url) {
            this.profile_photo_url = profile_photo_url;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getAuthor_url() {
            return author_url;
        }

        public void setAuthor_url(String author_url) {
            this.author_url = author_url;
        }

        public String getAuthor_name() {
            return author_name;
        }

        public void setAuthor_name(String author_name) {
            this.author_name = author_name;
        }
    }

    public static class Plus_code {
        @SerializedName("global_code")
        private String global_code;
        @SerializedName("compound_code")
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
        @SerializedName("width")
        private int width;
        @SerializedName("photo_reference")
        private String photo_reference;
        @SerializedName("html_attributions")
        private List<String> html_attributions;
        @SerializedName("height")
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
        @SerializedName("weekday_text")
        private List<String> weekday_text;
        @SerializedName("periods")
        private List<Periods> periods;
        @SerializedName("open_now")
        private boolean open_now;

        public List<String> getWeekday_text() {
            return weekday_text;
        }

        public void setWeekday_text(List<String> weekday_text) {
            this.weekday_text = weekday_text;
        }

        public List<Periods> getPeriods() {
            return periods;
        }

        public void setPeriods(List<Periods> periods) {
            this.periods = periods;
        }

        public boolean getOpen_now() {
            return open_now;
        }

        public void setOpen_now(boolean open_now) {
            this.open_now = open_now;
        }
    }

    public static class Periods {
        @SerializedName("open")
        private Open open;
        @SerializedName("close")
        private Close close;

        public Open getOpen() {
            return open;
        }

        public void setOpen(Open open) {
            this.open = open;
        }

        public Close getClose() {
            return close;
        }

        public void setClose(Close close) {
            this.close = close;
        }
    }

    public static class Open {
        @SerializedName("time")
        private String time;
        @SerializedName("day")
        private int day;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }
    }

    public static class Close {
        @SerializedName("time")
        private String time;
        @SerializedName("day")
        private int day;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }
    }

    public static class Geometry {
        @SerializedName("viewport")
        private Viewport viewport;
        @SerializedName("location")
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
        @SerializedName("southwest")
        private Southwest southwest;
        @SerializedName("northeast")
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
        @SerializedName("lng")
        private double lng;
        @SerializedName("lat")
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
        @SerializedName("lng")
        private double lng;
        @SerializedName("lat")
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
        @SerializedName("lng")
        private double lng;
        @SerializedName("lat")
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

    public static class Address_components {
        @SerializedName("types")
        private List<String> types;
        @SerializedName("short_name")
        private String short_name;
        @SerializedName("long_name")
        private String long_name;

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public String getShort_name() {
            return short_name;
        }

        public void setShort_name(String short_name) {
            this.short_name = short_name;
        }

        public String getLong_name() {
            return long_name;
        }

        public void setLong_name(String long_name) {
            this.long_name = long_name;
        }
    }
}

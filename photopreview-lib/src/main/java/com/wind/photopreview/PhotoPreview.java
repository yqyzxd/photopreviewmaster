package com.wind.photopreview;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wind on 2017/3/2.
 */
public class PhotoPreview implements Parcelable{


    private String path;//url或本地文件路径
    private int resId;//drawable资源

    private Map<String,String> extra;

    public PhotoPreview() {
    }

    protected PhotoPreview(Parcel in) {
        path = in.readString();
        resId = in.readInt();
        extra=in.readHashMap(HashMap.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeInt(resId);
        dest.writeMap(extra);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhotoPreview> CREATOR = new Creator<PhotoPreview>() {
        @Override
        public PhotoPreview createFromParcel(Parcel in) {
            return new PhotoPreview(in);
        }

        @Override
        public PhotoPreview[] newArray(int size) {
            return new PhotoPreview[size];
        }
    };

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public void put(String key,String value){
        if (extra==null){
            extra=new HashMap<>();
        }
        extra.put(key,value);
    }
    public String get(String key){
        return extra.get(key);
    }

    public void setExtra(Map<String, String> extra) {
        this.extra = extra;
    }


    @Override
    public boolean equals(Object obj) {

        if (obj==this){
            return true;
        }

        if (obj instanceof PhotoPreview){
            PhotoPreview that= (PhotoPreview) obj;
            if (!that.getPath().equals(getPath())){
                return false;
            }
            if (that.getResId()!=getResId()){
                return false;
            }
            return true;
        }

        return false;

    }
}

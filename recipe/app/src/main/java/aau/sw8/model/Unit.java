package aau.sw8.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jacob on 3/27/14.
 */
public class Unit implements Parcelable{
    private long unitId;
    private String metric;
    private String imperial;
    private float conversion;

    public Unit(long unitId, String metric, String imperial, float conversion) {
        this.unitId = unitId;
        this.metric = metric;
        this.imperial = imperial;
        this.conversion = conversion;
    }

    public long getUnitId() {
        return unitId;
    }

    public String getMetric() {
        return metric;
    }

    public String getImperial() {
        return imperial;
    }

    public float getConversion() {
        return conversion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.unitId);
        out.writeString(this.metric);
        out.writeString(this.imperial);
        out.writeFloat(this.conversion);
    }

    public static final Parcelable.Creator<Unit> CREATOR = new Parcelable.Creator<Unit>() {
        @Override
        public Unit createFromParcel(Parcel in) {
            return new Unit(in);
        }

        @Override
        public Unit[] newArray(int size) {
            return new Unit[size];
        }
    };

    private Unit(Parcel in) {
        this.unitId = in.readLong();
        this.metric = in.readString();
        this.imperial = in.readString();
        this.conversion = in.readFloat();
    }
}

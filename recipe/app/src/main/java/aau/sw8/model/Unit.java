package aau.sw8.model;

/**
 * Created by jacob on 3/27/14.
 */
public class Unit {
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
}

package aau.sw8.recipe;

/**
 * Created by jacob on 3/27/14.
 */
public class Unit {
    private int unitId;
    private int metric;
    private int imperial;
    private float conversion;

    public Unit(int unitId, int metric, int imperial, float conversion) {
        this.unitId = unitId;
        this.metric = metric;
        this.imperial = imperial;
        this.conversion = conversion;
    }

    public int getUnitId() {
        return unitId;
    }

    public int getMetric() {
        return metric;
    }

    public int getImperial() {
        return imperial;
    }

    public float getConversion() {
        return conversion;
    }
}

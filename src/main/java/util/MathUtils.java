package util;

public class MathUtils {

    public static boolean allLessThan(float[] values, int bound) {
        for (float value: values)
            if (value >= bound)
                return false;
        return true;
    }

    public static int argMin(float[] values) {
        int argMin = -1;
        float minValue = Float.POSITIVE_INFINITY;
        for (int i = 0; i < values.length; i++) {
            if(values[i] < minValue) {
                argMin = i;
                minValue = values[i];
            }
        }
        return argMin;
    }

    public static int argMax(float[] values) {
        int argMax = -1;
        float maxValue = Float.NEGATIVE_INFINITY;
        for (int i = 0; i < values.length; i++) {
            if(values[i] > maxValue) {
                argMax = i;
                maxValue = values[i];
            }
        }
        return argMax;
    }

    public static float[] abs(float[] values) {
        float[] result = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = Math.abs(values[i]);
        }
        return result;
    }

}

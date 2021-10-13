package sinanakkoyun.minecraftinventorylog.math;

public class MathUtil {
    public static int clamp(int a, int min, int max) {
        a = (a < min ? min : a);
        return (a > max ? max : a);
    }
}

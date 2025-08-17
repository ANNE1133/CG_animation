package finalVer;

public class AnimationTime {
    private static float globalTime = 0;
    
    public static void update() {
        globalTime += 0.1f;
    }
    
    public static float getTime() {
        return globalTime;
    }
    
    public static void reset() {
        globalTime = 0;
    }
}
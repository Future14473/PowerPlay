package org.firstinspires.ftc.teamcode.Hardware.util;

import java.util.*;

public class MathUtil {
  public static double EPSILON = 0.01;

  public static double clamp(double x, double minVal, double maxVal) {
    return Math.min(Math.max(x, minVal), maxVal);
  }
  
  public static double applyWeight(double edge0, double edge1, double weight) {
    weight = MathUtil.clamp(weight, 0.0, 1.0);
    return edge0 * (1.0 - weight) + edge1 * weight;
  }
  
  public static int applyWeight(int edge0, int edge1, double weight) {
    return (int) MathUtil.applyWeight((double) edge0, (double) edge1, weight);
  }

  public static double getWeight(double edge0, double edge1, double value) {
    return (value - edge0) / (edge1 - edge0);
  }
}
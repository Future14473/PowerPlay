package org.firstinspires.ftc.teamcode.Hardware.util;

public class SumUtil {
  private double[] prefixes;
  private int count;

  public SumUtil(double[] array) {
    this.prefixes = new double[array.length + 1];
    for (int i = 0; i < array.length; i++) prefixes[i + 1] = array[i];
    for (int i = 0; i < array.length; i++) prefixes[i + 1] += prefixes[i];
    this.count = array.length;
  }

  public double rangeSum(int l, int r) {
    return this.prefixes[r] - this.prefixes[l];
  }

  public int lowerBound(double x) {
    int l = 0, r = this.count - 1;
    while (l < r) {
      int m = l + (r - l + 1) / 2;
      if (this.rangeSum(0, m) <= x) l = m;
      else r = m - 1;
    }
    return l;
  }
}
public final class Widget {
  private final int value;
  private final String label;

  public Widget(int value, String label) {
    this.value = value;
    this.label = label;
  }

  public int getValue() {
    return this.value;
  }

  public String toString() {
    return this.label;
  }
}
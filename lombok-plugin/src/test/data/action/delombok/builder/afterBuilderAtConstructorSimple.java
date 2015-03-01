public class BuilderAtConstructorSimple {
  private int myInt;
  private String myString;

  public BuilderAtConstructorSimple(int myInt, String myString) {
    this.myInt = myInt;
    this.myString = myString;
  }

  public static void main(String[] args) {
    BuilderAtConstructorSimple builderSimple = BuilderAtConstructorSimple.builder().myInt(123).myString("string").build();
    System.out.println(builderSimple);
  }

  public static BuilderAtConstructorSimple.BuilderAtConstructorSimpleBuilder builder() {
    return new BuilderAtConstructorSimple.BuilderAtConstructorSimpleBuilder();
  }

  public static class BuilderAtConstructorSimpleBuilder {
    private int myInt;
    private String myString;

    BuilderAtConstructorSimpleBuilder() {
    }

    public BuilderAtConstructorSimple.BuilderAtConstructorSimpleBuilder myInt(int myInt) {
      this.myInt = myInt;
      return this;
    }

    public BuilderAtConstructorSimple.BuilderAtConstructorSimpleBuilder myString(String myString) {
      this.myString = myString;
      return this;
    }

    public BuilderAtConstructorSimple build() {
      return new BuilderAtConstructorSimple(myInt, myString);
    }

    public String toString() {
      return "BuilderAtConstructorSimple.BuilderAtConstructorSimpleBuilder(myInt=" + this.myInt + ", myString=" + this.myString + ")";
    }
  }
}
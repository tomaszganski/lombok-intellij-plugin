package de.plushnikov.builder.simple;

public class BuilderAtConstructorSimplePredefined {
    private int myInt;
    private String myString2;

    @lombok.Builder
    public BuilderAtConstructorSimplePredefined(int myInt, String myString2) {
        this.myInt = myInt + 1;
        this.myString = myString2;
    }

    static class BuilderAtConstructorSimplePredefinedBuilder {
        private int myInt;

        public BuilderAtConstructorSimplePredefined.BuilderAtConstructorSimplePredefinedBuilder myString2(String myString) {
            this.myString2 = myString + "something";
            return this;
        }
    }

    public static void main(String[] args) {
        BuilderAtConstructorSimplePredefined builderSimple = BuilderAtConstructorSimplePredefined.builder().myInt(123).myString("string").build();
        System.out.println(builderSimple);
    }
}

package it.finanze.sanita.fse2.ms.edssrvdictionary.changeset.base;

public enum ResourceTypeTest {
    CODESYSTEM("codesystem"),
    VALUESET("valueset");

    private final String value;

    private ResourceTypeTest(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}

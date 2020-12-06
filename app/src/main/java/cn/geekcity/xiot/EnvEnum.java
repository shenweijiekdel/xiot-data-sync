package cn.geekcity.xiot;

public enum EnvEnum {

    Dev("dev-"),
    Stage("st-"),
    Preview("pv-"),
    Prod("");

    private final String prefix;

    EnvEnum(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}

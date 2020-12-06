package cn.geekcity.xiot.view;

import cn.geekcity.xiot.EnvEnum;
import javafx.util.StringConverter;

public class EnvConverter extends StringConverter<EnvEnum> {

    @Override
    public String toString(EnvEnum object) {
        return object.name();
    }

    @Override
    public EnvEnum fromString(String string) {
        return EnvEnum.valueOf(string);
    }
}

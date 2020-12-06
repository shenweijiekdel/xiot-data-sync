package cn.geekcity.xiot.domain;

import cn.geekcity.xiot.spec.constant.Spec;
import cn.geekcity.xiot.spec.template.DeviceTemplate;
import javafx.beans.property.SimpleStringProperty;

public class VTemplate extends EnvData<DeviceTemplate> {

    private final SimpleStringProperty type = new SimpleStringProperty("");
    private final SimpleStringProperty name = new SimpleStringProperty("");

    public VTemplate(DeviceTemplate sourceTemplate, DataDiffer<DeviceTemplate> differ) {
        super(differ, sourceTemplate);
        this.type.set(sourceTemplate.type().toString());
        this.name.set(sourceTemplate.description().get(Spec.DESCRIPTION_ZH_CN));
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }
}

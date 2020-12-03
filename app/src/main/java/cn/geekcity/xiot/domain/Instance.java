package cn.geekcity.xiot.domain;

import cn.geekcity.xiot.spec.definition.urn.DeviceType;
import cn.geekcity.xiot.spec.instance.Device;
import cn.geekcity.xiot.spec.lifecycle.Lifecycle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Instance {

    private int version;
    private Lifecycle lifecycle;
    private DeviceType type;
    private Device content;

    public Instance(int version, Lifecycle lifecycle, DeviceType type, Device content) {
        this.version = version;
        this.lifecycle = lifecycle;
        this.type = type;
        this.content = content;
    }

    public int getVersion() {
        return version;
    }

    public Instance setVersion(int version) {
        this.version = version;
        return this;
    }

    public Lifecycle getLifecycle() {
        return lifecycle;
    }

    public Instance setLifecycle(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
        return this;
    }

    public DeviceType getType() {
        return type;
    }

    public Instance setType(DeviceType type) {
        this.type = type;
        return this;
    }

    public Device getContent() {
        return content;
    }

    public Instance setContent(Device content) {
        this.content = content;
        return this;
    }
}

package cn.geekcity.xiot.domain;

import cn.geekcity.xiot.spec.instance.Device;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {

    private final SimpleIntegerProperty id = new SimpleIntegerProperty(0);
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty model = new SimpleStringProperty("");
    private final SimpleStringProperty spec = new SimpleStringProperty("");
    private final SimpleStringProperty group = new SimpleStringProperty("");
    private final SimpleStringProperty template = new SimpleStringProperty("");
    private final SimpleStringProperty diff = new SimpleStringProperty("");
    private final Map<Integer, Instance> instances = new HashMap<>();

    public Product(Integer id, String name, String model, String spec, String template, String group) {
        this.id.set(id);
        this.name.set(name);
        this.group.set(group);
        this.model.set(model);
        this.spec.set(spec);
        this.template.set(template);
    }

    public int getId() {
        return id.get();
    }

    public Product addInstance(Instance instance) {
        instances.put(instance.getVersion(), instance);
        return this;
    }

    public Product setInstances(List<Instance> instance) {
        for (Instance i : instance) {
            instances.put(i.getVersion(), i);
        }

        return this;
    }

    public Product addDiff(String diff) {
        setDiff(this.diff.get() + "\n" + diff);
        return this;
    }

    public Map<Integer, Instance> getInstances() {
        return instances;
    }

    public String getGroup() {
        return group.get();
    }

    public SimpleStringProperty groupProperty() {
        return group;
    }

    public void setGroup(String group) {
        this.group.set(group);
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getDiff() {
        return diff.get();
    }

    public SimpleStringProperty diffProperty() {
        return diff;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getModel() {
        return model.get();
    }

    public SimpleStringProperty modelProperty() {
        return model;
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public void setDiff(String diff) {
        this.diff.set(diff);
    }

    public String getSpec() {
        return spec.get();
    }

    public SimpleStringProperty specProperty() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec.set(spec);
    }

    public String getTemplate() {
        return template.get();
    }

    public SimpleStringProperty templateProperty() {
        return template;
    }

    public void setTemplate(String template) {
        this.template.set(template);
    }
}

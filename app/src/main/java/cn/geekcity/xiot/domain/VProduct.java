package cn.geekcity.xiot.domain;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class VProduct extends EnvData<Product> {

    private final SimpleIntegerProperty id = new SimpleIntegerProperty(0);
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty model = new SimpleStringProperty("");
    private final SimpleStringProperty spec = new SimpleStringProperty("");
    private final SimpleStringProperty group = new SimpleStringProperty("");
    private final SimpleStringProperty template = new SimpleStringProperty("");

    public VProduct(Product source, DataDiffer<Product> differ) {
        super(differ, source);
        this.id.setValue(source.getId());
        this.name.setValue(source.getName());
        this.model.setValue(source.getModel());
        this.spec.setValue(source.getSpec());
        this.group.setValue(source.getGroup());
        this.template.setValue(source.getTemplate());
    }

    public int getId() {
        return id.get();
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

    public String getSpec() {
        return spec.get();
    }

    public SimpleStringProperty specProperty() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec.set(spec);
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

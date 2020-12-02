package cn.geekcity.xiot.domain;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Product {

    private final SimpleIntegerProperty id = new SimpleIntegerProperty(0);
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty model = new SimpleStringProperty("");
    private final SimpleStringProperty spec = new SimpleStringProperty("");
    private final SimpleStringProperty template = new SimpleStringProperty("");

    public Product(Integer id, String name, String model, String spec, String template) {
        this.id.set(id);
        this.name.set(name);
        this.model.set(model);
        this.spec.set(spec);
        this.template.set(template);
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

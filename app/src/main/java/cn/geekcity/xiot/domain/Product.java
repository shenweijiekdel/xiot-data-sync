package cn.geekcity.xiot.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {

    private Integer id;
    private String name;
    private String model;
    private String spec;
    private String group;
    private String template;
    private final Map<Integer, Instance> instances = new HashMap<>();

    public Product(Integer id, String name, String model, String ns, String template, String code) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.spec = ns;
        this.template = template;
        this.group = code;
    }

    public Integer getId() {
        return id;
    }

    public Product setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public String getModel() {
        return model;
    }

    public Product setModel(String model) {
        this.model = model;
        return this;
    }

    public String getSpec() {
        return spec;
    }

    public Product setSpec(String spec) {
        this.spec = spec;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public Product setGroup(String group) {
        this.group = group;
        return this;
    }

    public String getTemplate() {
        return template;
    }

    public Product setTemplate(String template) {
        this.template = template;
        return this;
    }

    public Map<Integer, Instance> getInstances() {
        return instances;
    }

    public Product setInstances(List<Instance> instances) {
        this.instances.clear();

        for (Instance i : instances) {
            this.instances.put(i.getVersion(), i);
        }

        return this;
    }
}

package cn.geekcity.xiot.domain;

import java.util.List;

public class ProductDiffer {

    private List<Product> source;
    private List<Product> target;

    public List<Product> source() {
        return source;
    }

    public ProductDiffer source(List<Product> source) {
        this.source = source;
        return this;
    }

    public List<Product> target() {
        return target;
    }

    public ProductDiffer target(List<Product> target) {
        this.target = target;
        return this;
    }
}

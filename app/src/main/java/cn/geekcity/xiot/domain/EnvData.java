package cn.geekcity.xiot.domain;

import javafx.beans.property.SimpleStringProperty;

public class EnvData<T> {

    private final DataDiffer<T> differ;
    private final SimpleStringProperty diff = new SimpleStringProperty("未比较");
    private T source;
    private T current;

    public EnvData(DataDiffer<T> differ, T source) {
        this.differ = differ;
        this.source = source;
    }

    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
        this.diff.setValue(this.differ.diff(source, current));
    }

    public T getCurrent() {
        return current;
    }

    public void setCurrent(T current) {
        this.current = current;
        this.diff.setValue(this.differ.diff(source, current));
    }

    public DataDiffer<T> getDiffer() {
        return differ;
    }

    public String getDiff() {
        return diff.get();
    }

    public SimpleStringProperty diffProperty() {
        return diff;
    }
}

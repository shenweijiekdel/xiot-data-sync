package cn.geekcity.xiot.domain;

public interface DataDiffer<T> {

    /**
     *  数据比较
     * @param source 源环境数据
     * @param current 当前环境数据
     * @return 差异消息
     */
    String diff (T source, T current) ;

}

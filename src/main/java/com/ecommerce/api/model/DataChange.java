package com.ecommerce.api.model;

public class DataChange<T> {
    private ChangeType  changetype;

    private  T data;
    public  DataChange(){}
    public DataChange(ChangeType changetype, T data){
        this.changetype = changetype;
        this.data = data;

    }

    public ChangeType getChangetype() {
        return changetype;
    }

    public T getData() {
        return data;
    }

    public void setChangetype(ChangeType changetype) {
        this.changetype = changetype;
    }

    public void setData(T data) {
        this.data = data;
    }

    public enum ChangeType {
        INSERT,
        UPDATE,
        DELETE,
    }
}

package com.example.demo.messageDatabase;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table

public class Value {
    
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    public long id ;

    private long value;
    public Long parentId;
    
    public Value(){}

    public Value(long id, int value, Long parentId){
        this.id = id;
        this.value = value;
        this.parentId = parentId;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    
}

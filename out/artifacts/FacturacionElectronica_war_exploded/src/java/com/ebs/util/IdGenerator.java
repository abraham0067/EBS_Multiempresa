/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class IdGenerator implements Serializable {
    private AtomicInteger value;
    private AtomicLong lngId;

    public IdGenerator(int value,long lngValue) {
        this.value = new AtomicInteger(value);
        this.lngId = new AtomicLong(lngValue);
    }

    public synchronized int incrementAndGet() {
        int result = value.incrementAndGet();
        //in case of integer overflow
        if (result < 0) {
            value.set(0);
            return 0;
        }
        return result;
    }

    public synchronized long incrementAndGetAsLong() {
        long result = lngId.incrementAndGet();
        //in case of integer overflow
        if (result < 0) {
            lngId.set(0);
            return 0;
        }
        return result;
    }

    public synchronized  long getIdFromDateTime(){
        return Calendar.getInstance().getTime().getTime();
    }
    public synchronized UUID getUuid() {
        UUID idOne = UUID.randomUUID();
        return idOne;
    }

    public synchronized String  getUuidAsString(UUID id){
        if(id== null)
            throw new IllegalArgumentException("UUID argument should not be null");
        return id.toString();
    }

    public UUID getStringAsUuid(String uuid){
        if(uuid== null)
            throw new IllegalArgumentException("String UUID argument should not be null");
        return UUID.fromString(uuid);
    }
}

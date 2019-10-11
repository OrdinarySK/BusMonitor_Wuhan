package com.zyc.busmonitoritem;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class BusLine  implements Serializable {
    String lineName;
    String lineNo;
    int direction;
    String startStopName="起始站";
    String endStopName="终点站";
    String firstTime="XX:XX";
    String lastTime="XX:XX";
    int stopsNum=0;

    public BusLine(String lineName, String LineNo, int direction) {
        this.lineName = lineName;
        this.lineNo = LineNo;
        this.direction = direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public void setEndStopName(String endStopName) {
        this.endStopName = endStopName;
    }

    public void setFirstTime(String firstTime) {
        this.firstTime = firstTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public void setStartStopName(String startStopName) {
        this.startStopName = startStopName;
    }

    public void setStopsNum(int stopsNum) {
        this.stopsNum = stopsNum;
    }

    public int getDirection() {
        return direction;
    }

    public String getLineName() {
        return lineName;
    }

    public String getLineNo() {
        return lineNo;
    }

    public int getStopsNum() {
        return stopsNum;
    }

    public String getEndStopName() {
        return endStopName;
    }

    public String getFirstTime() {
        return firstTime;
    }

    public String getStartStopName() {
        return startStopName;
    }

    public String getLastTime() {
        return lastTime;
    }
}

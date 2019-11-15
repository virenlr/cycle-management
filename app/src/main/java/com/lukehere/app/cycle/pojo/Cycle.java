package com.lukehere.app.cycle.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.util.Date;

public class Cycle implements Parcelable {
    private String cycleId;
    private int cycleNumber;

    private int status;
    private String location;

    private int tyreCondition;
    private int chainCondition;

    private int cableCondition;
    private int brakeCondition;

    private int lubricationCondition;
    private int miscellaneousCondition;

    private String creationDate;

    private int decommissionedStatus;
    private String decommissionedDate;

    public Cycle() {
    }

    public Cycle(String cycleId, int cycleNumber, int status, String location, int tyreCondition, int chainCondition, int cableCondition, int brakeCondition, int lubricationCondition, int miscellaneousCondition, int decommissionedStatus) {
        this.cycleId = cycleId;
        this.cycleNumber = cycleNumber;
        this.status = status;
        this.location = location;
        this.tyreCondition = tyreCondition;
        this.chainCondition = chainCondition;
        this.cableCondition = cableCondition;
        this.brakeCondition = brakeCondition;
        this.lubricationCondition = lubricationCondition;
        this.miscellaneousCondition = miscellaneousCondition;

        long creationTime = new Date().getTime();
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        creationDate = dateFormat.format(creationTime);

        this.decommissionedStatus = decommissionedStatus;
    }

    public String getCycleId() {
        return cycleId;
    }

    public void setCycleId(String cycleId) {
        this.cycleId = cycleId;
    }

    public int getCycleNumber() {
        return cycleNumber;
    }

    public void setCycleNumber(int cycleNumber) {
        this.cycleNumber = cycleNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTyreCondition() {
        return tyreCondition;
    }

    public void setTyreCondition(int tyreCondition) {
        this.tyreCondition = tyreCondition;
    }

    public int getChainCondition() {
        return chainCondition;
    }

    public void setChainCondition(int chainCondition) {
        this.chainCondition = chainCondition;
    }

    public int getCableCondition() {
        return cableCondition;
    }

    public void setCableCondition(int cableCondition) {
        this.cableCondition = cableCondition;
    }

    public int getBrakeCondition() {
        return brakeCondition;
    }

    public void setBrakeCondition(int brakeCondition) {
        this.brakeCondition = brakeCondition;
    }

    public int getMiscellaneousCondition() {
        return lubricationCondition;
    }

    public void setMiscellaneousCondition(int lubricationCondition) {
        this.lubricationCondition = lubricationCondition;
    }

    public int getLubricationCondition() {
        return miscellaneousCondition;
    }

    public void setLubricationCondition(int miscellaneousCondition) {
        this.miscellaneousCondition = miscellaneousCondition;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public int getDecommissionedStatus() {
        return decommissionedStatus;
    }

    public void setDecommissionedStatus(int decommissionedStatus) {
        this.decommissionedStatus = decommissionedStatus;
    }

    public String getDecommissionedDate() {
        return decommissionedDate;
    }

    public void setDecommissionedDate(String decommissionedDate) {
        this.decommissionedDate = decommissionedDate;
    }

    protected Cycle(Parcel in) {
        cycleId = in.readString();
        cycleNumber = in.readInt();
        status = in.readInt();
        location = in.readString();
        tyreCondition = in.readInt();
        chainCondition = in.readInt();
        cableCondition = in.readInt();
        brakeCondition = in.readInt();
        lubricationCondition = in.readInt();
        miscellaneousCondition = in.readInt();
        creationDate = in.readString();
        decommissionedStatus = in.readInt();
        decommissionedDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cycleId);
        dest.writeInt(cycleNumber);
        dest.writeInt(status);
        dest.writeString(location);
        dest.writeInt(tyreCondition);
        dest.writeInt(chainCondition);
        dest.writeInt(cableCondition);
        dest.writeInt(brakeCondition);
        dest.writeInt(lubricationCondition);
        dest.writeInt(miscellaneousCondition);
        dest.writeString(creationDate);
        dest.writeInt(decommissionedStatus);
        dest.writeString(decommissionedDate);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Cycle> CREATOR = new Parcelable.Creator<Cycle>() {
        @Override
        public Cycle createFromParcel(Parcel in) {
            return new Cycle(in);
        }

        @Override
        public Cycle[] newArray(int size) {
            return new Cycle[size];
        }
    };
}
package com.example.calculatenow.model;

import java.util.List;

public class EquitationCard {

    private String result;
    private String amountItem;
    private List<EquitationCard> list;

    private int tag;

    public EquitationCard() {
    }


    public EquitationCard(String result, String amountItem) {
        this.result = result;
        this.amountItem = amountItem;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAmountItem() {
        return amountItem;
    }

    public void setAmountItem(String amountItem) {
        this.amountItem = amountItem;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public List<EquitationCard> getList() {
        return list;
    }

}

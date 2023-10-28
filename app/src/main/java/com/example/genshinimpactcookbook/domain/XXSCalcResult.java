package com.example.genshinimpactcookbook.domain;

public class XXSCalcResult {
    private int result;
    private int exceed;

    public XXSCalcResult(int result, int exceed) {
        this.result = result;
        this.exceed = exceed;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getExceed() {
        return exceed;
    }

    public void setExceed(int exceed) {
        this.exceed = exceed;
    }

    @Override
    public String toString() {
        return "XXSCalcResult{" +
                "result=" + result +
                ", exceed=" + exceed +
                '}';
    }
}

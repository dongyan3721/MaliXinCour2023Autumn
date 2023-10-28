package com.example.genshinimpactcookbook.domain;

public class MultiplyDivideTasks {
    private int id;
    private int num1;
    private int num2;
    private int operator;
    private int result;
    private int exceed;

    public MultiplyDivideTasks(int id, int num1, int num2, int operator, int result, int exceed) {
        this.id = id;
        this.num1 = num1;
        this.num2 = num2;
        this.operator = operator;
        this.result = result;
        this.exceed = exceed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum1() {
        return num1;
    }

    public void setNum1(int num1) {
        this.num1 = num1;
    }

    public int getNum2() {
        return num2;
    }

    public void setNum2(int num2) {
        this.num2 = num2;
    }

    public int getOperator() {
        return operator;
    }

    public void setOperator(int operator) {
        this.operator = operator;
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
        return "MultiplyDivideTasks{" +
                "id=" + id +
                ", num1=" + num1 +
                ", num2=" + num2 +
                ", operator=" + operator +
                ", result=" + result +
                ", exceed=" + exceed +
                '}';
    }
}

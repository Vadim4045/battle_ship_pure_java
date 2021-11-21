package com.gmail.focusdigit;

public class Pair<T,Y>
{
    public T first;
    public Y second;

    public Pair(T f, Y s)
    {
        first = f;
        second = s;
    }

    public Pair(Pair<T, Y> pair){
        this.setFirst(pair.getFirst());
        this.setSecond(pair.getSecond());
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public Y getSecond() {
        return second;
    }

    public void setSecond(Y second) {
        this.second = second;
    }
}

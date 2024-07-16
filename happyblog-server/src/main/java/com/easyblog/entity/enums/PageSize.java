package com.happyblog.entity.enums;


public enum PageSize {
    SIZE5(5), SIZE15(15), SIZE20(20), SIZE30(30), SIZE40(40), SIZE50(50), SIZE100(100);
    int size;

    private PageSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }
}

package com.example.mauriciogodinez.splashtest.datarecyclerview;

/*
 * Created by mauriciogodinez on 19/04/17.
 */

public class PromocionesItem {
    private Integer imagenItem;
    private String titleItem;
    private String contentItem;

    public PromocionesItem() {
        this.imagenItem = -1;
        this.titleItem = "";
        this.contentItem = "";
    }

    public PromocionesItem(Integer imagenItem, String titleItem, String contentItem) {
        this.imagenItem = imagenItem;
        this.titleItem = titleItem;
        this.contentItem = contentItem;
    }

    public Integer getImagenItem() {
        return imagenItem;
    }

    public void setImagenItem(Integer imagenItem) {
        this.imagenItem = imagenItem;
    }

    public String getTitleItem() {
        return titleItem;
    }

    public void setTitleItem(String titleItem) {
        this.titleItem = titleItem;
    }

    public String getContentItem() {
        return contentItem;
    }

    public void setContentItem(String contentItem) {
        this.contentItem = contentItem;
    }
}

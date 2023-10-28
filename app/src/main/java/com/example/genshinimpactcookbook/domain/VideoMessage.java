package com.example.genshinimpactcookbook.domain;

public class VideoMessage {
    private String alias;
    private String pictureUrl;
    private String previewPictureUrl;
    private String contentUrl;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getPreviewPictureUrl() {
        return previewPictureUrl;
    }

    public void setPreviewPictureUrl(String previewPictureUrl) {
        this.previewPictureUrl = previewPictureUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
}

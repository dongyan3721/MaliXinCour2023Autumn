package com.example.genshinimpactcookbook.domain;

public class VideoMessage {
    private String alias;//视频上传者名
    private String pictureUrl;//头像
    private String previewPictureUrl;//封面
    private String contentUrl;//视频内容

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

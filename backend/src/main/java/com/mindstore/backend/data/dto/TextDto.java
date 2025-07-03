package com.mindstore.backend.data.dto;

import com.mindstore.backend.data.Category;

import java.util.List;

public class TextDto {
    private String title;
    private String content_raw;
    private String content_html;
    private List<Category> tags;

    public List<String> getCommandList() {
        return commandList;
    }

    public void setCommandList(List<String> commandList) {
        this.commandList = commandList;
    }

    public List<Category> getTags() {
        return tags;
    }

    public void setTags(List<Category> tags) {
        this.tags = tags;
    }

    public String getContent_html() {
        return content_html;
    }

    public void setContent_html(String content_html) {
        this.content_html = content_html;
    }

    public String getContent_raw() {
        return content_raw;
    }

    public void setContent_raw(String content_raw) {
        this.content_raw = content_raw;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private List<String> commandList;

}

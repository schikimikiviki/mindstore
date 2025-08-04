package com.mindstore.backend.data.dto;

import com.mindstore.backend.data.Category;

import java.util.List;

/**
 * TextDto class used as counterpart to text document
 */
public class TextDto {
    private String title;
    private String content_raw;
    private String content_html;
    private List<Category> tags;
    private List<String> commandList;

    /**
     * default constructor
     */
    public TextDto(){}

    /**
     * default getter
     * @return List of strings commands
     */
    public List<String> getCommandList() {
        return commandList;
    }

    /**
     * default setter
     * @param commandList List of String commands
     */
    public void setCommandList(List<String> commandList) {
        this.commandList = commandList;
    }

    /**
     * default getter
     * @return List of categories
     */
    public List<Category> getTags() {
        return tags;
    }

    /**
     * default setter
     * @param tags List of categories
     */
    public void setTags(List<Category> tags) {
        this.tags = tags;
    }

    /**
     * default getter
     * @return List of strings content html
     */
    public String getContent_html() {
        return content_html;
    }

    /**
     * default setter
     * @param content_html String
     */
    public void setContent_html(String content_html) {
        this.content_html = content_html;
    }

    /**
     * default getter
     * @return List of strings content raw
     */
    public String getContent_raw() {
        return content_raw;
    }

    /**
     * default setter
     * @param content_raw String
     */
    public void setContent_raw(String content_raw) {
        this.content_raw = content_raw;
    }

    /**
     * default getter
     * @return title String
     */
    public String getTitle() {
        return title;
    }

    /**
     * default setter
     * @param title String
     */
    public void setTitle(String title) {
        this.title = title;
    }



}

package com.mindstore.backend.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

/**
 * Text document class, used as main data object
 */
@Document(indexName = "text_index")
public class TextDocument {

    @Id
    private Integer id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String content_raw;

    @Field(type = FieldType.Text)
    private String content_html;

    @Field(type = FieldType.Date)
    private Date createdAt;

    @Field(type = FieldType.Date)
    private Date updatedAt;

    @Field(type = FieldType.Nested)
    private List<Category> tags;

    @Field(type = FieldType.Keyword)
    private List<String> commandList;

    /**
     * default constructor
     */
    public TextDocument(){}

    /**
     * default getter
     * @return Integer id
     */
    public Integer getId() {
        return id;
    }

    /**
     * default setter
     * @param id integer
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * default getter
     * @return String title
     */
    public String getTitle() {
        return title;
    }

    /**
     * default setter
     * @param title string
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * default getter
     * @return String content raw
     */
    public String getContent_raw() {
        return content_raw;
    }

    /**
     * default setter
     * @param content_raw string
     */
    public void setContent_raw(String content_raw) {
        this.content_raw = content_raw;
    }

    /**
     * default getter
     * @return String content html
     */
    public String getContent_html() {
        return content_html;
    }

    /**
     * default setter
     * @param content_html string
     */
    public void setContent_html(String content_html) {
        this.content_html = content_html;
    }

    /**
     * default getter
     * @return Date updated at
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * default setter
     * @param updatedAt Date
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * default getter
     * @return Date created at
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * default setter
     * @param createdAt Date
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * default getter
     * @return List of Categories
     */
    public List<Category> getTags() {
        return tags;
    }

    /**
     * default setter
     * @param tags list of categories
     */
    public void setTags(List<Category> tags) {
        this.tags = tags;
    }

    /**
     * default getter
     * @return List of command Strings
     */
    public List<String> getCommandList() {
        return commandList;
    }

    /**
     * default setter
     * @param commandList List of strings
     */
    public void setCommandList(List<String> commandList) {
        this.commandList = commandList;
    }
}
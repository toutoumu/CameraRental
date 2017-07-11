package com.dsfy.entity;

import com.google.gson.annotations.Expose;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_Category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    @Column(name = "categoryId")
    private int categoryId;
    /** 名字 */
    @Expose
    @Column(length = 120)
    private String name;
    /** 父节点ID */
    @Expose
    @Column
    private int parentId;
    /** 节点属于第几级 ,从1开始 */
    @Expose
    @Column
    private int level;
    /** 备注 */
    @Expose
    @Column(length = 1000)
    private String mark;

    /**
     * @return the categoryId
     */
    public int getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId the id to set
     */
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 地区名称(省份,城市,区)
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * 地区名称(省份,城市,区)
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 父节点主键
     *
     * @return the parentId
     */
    public int getParentId() {
        return parentId;
    }

    /**
     * 父节点主键
     *
     * @param parentId the parentId to set
     */
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    /** 节点属于第几级 */
    public int getLevel() {
        return level;
    }

    /** 节点属于第几级 */
    public void setLevel(int regionLevel) {
        this.level = regionLevel;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    /**
     * 树形数据
     *
     * @author toutoumu
     */
    public static class TreeData {
        private int id;
        private int parentId;
        private String text;
        private String state = "open";
        private boolean checked;
        private List<TreeData> children;

        public TreeData() {

        }

        public TreeData(Category category) {
            if (category != null) {
                this.id = category.categoryId;
                this.text = category.name;
                this.parentId = category.parentId;
            }
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public List<TreeData> getChildren() {
            return children;
        }

        public void setChildren(List<TreeData> children) {
            this.children = children;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }
}

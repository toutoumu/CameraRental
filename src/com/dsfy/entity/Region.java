package com.dsfy.entity;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "t_Region")
public class Region {
    public static class Category {
        /** 0:未知 */
        public static final int nullVal = 0;
        /** 省 */
        public static final int province = 1;
        /** 城市 */
        public static final int city = 2;
        /** 区 */
        public static final int district = 3;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    @Column(name = "regionId")
    private int regionId;
    /** 名字 */
    @Expose
    @Column
    private String name;
    /** 父节点ID */
    @Expose
    @Column
    private int parentId;
    /** 节点类型 1.省 2.市 3.区 */
    @Expose
    @Column
    private int regionType;

    /**
     * @return the regionId
     */
    public int getRegionId() {
        return regionId;
    }

    /**
     * @param regionId the id to set
     */
    public void setRegionId(int regionId) {
        this.regionId = regionId;
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

    /**
     * 区域类型(1.省,2.城市,3.区(乡镇,县)
     *
     * @return the regionType
     */
    public int getRegionType() {
        return regionType;
    }

    /**
     * 区域类型(1.省,2.城市,3.区(乡镇,县)
     *
     * @param regionType the regionType to set
     */
    public void setRegionType(int regionType) {
        this.regionType = regionType;
    }

    /**
     * 树形数据
     *
     * @author toutoumu
     */
    public static class TreeData {
        private int id;
        private int parentId;
        private int regionType;
        private String text;
        private String state = "open";
        private boolean checked;
        private List<TreeData> children;

        public TreeData() {

        }

        public TreeData(Region region) {
            if (region != null) {
                this.id = region.regionId;
                this.text = region.name;
                this.parentId = region.parentId;
                this.regionType = region.regionType;
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

        public int getRegionType() {
            return regionType;
        }

        public void setRegionType(int regionType) {
            this.regionType = regionType;
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

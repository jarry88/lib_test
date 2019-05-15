package com.ftofs.twant.domain.complain;

public class ComplainSubject {
    /**
     * 主键
     */
    private int subjectId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ComplainSubject{" +
                "subjectId=" + subjectId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

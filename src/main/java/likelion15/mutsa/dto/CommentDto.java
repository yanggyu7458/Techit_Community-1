package likelion15.mutsa.dto;

public class CommentDto {
    private Long comId;
    private Long notId;

    //    private String writer;
//    private String regDate;
    private String content;

    public CommentDto(Long comId, Long notId, String content) {
        this.comId = comId;
        this.notId = notId;
        this.content = content;
    }

    public Long getComId() {
        return comId;
    }

    public void setComId(Long comId) {
        this.comId = comId;
    }

    public Long getNotId() {
        return notId;
    }

    public void setNotId(Long notId) {
        this.notId = notId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

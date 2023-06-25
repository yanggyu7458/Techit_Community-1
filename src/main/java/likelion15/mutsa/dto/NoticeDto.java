package likelion15.mutsa.dto;

public class NoticeDto {
    private Long id;
    private String title;
//    private String writer;
//    private String regDate;
    private String content;

    public NoticeDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public String getWriter() {
//        return writer;
//    }
//
//    public void setWriter(String writer) {
//        this.writer = writer;
//    }
//
//    public String getRegDate() {
//        return regDate;
//    }
//
//    public void setRegDate(String regDate) {
//        this.regDate = regDate;
//    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

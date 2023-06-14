package likelion15.mutsa.dto;

public class BoardDTO {
    private Long id;
    private String title;
    private String content;



    private Integer count;

    public BoardDTO(Long id, String title, String content, Integer count) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.count = count;


    }
    public Long getId() {
        return id;
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
    public Integer getCount() {
        return count;
    }

}

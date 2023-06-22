package likelion15.mutsa.entity;

import jakarta.persistence.*;
import likelion15.mutsa.entity.base.BaseEntity;
import likelion15.mutsa.entity.enums.UserAuth;
import likelion15.mutsa.entity.enums.UserStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter //setter추가했음..
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String realName;

    @Column(nullable = false,
            unique = true
    )
    private String name;

    @Column(nullable = false,
            unique = true
    )
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserAuth auth;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "user"
    )
    private List<Board> boards = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY,
            mappedBy = "user",
            cascade = CascadeType.ALL)
    private Profile profile;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "user"
    )
    private List<UserBoardTag> userBoardTags = new ArrayList<>();

//    public User(Long id,String name,String email,String password,String phoneNumber){
//        this.id = id;
//        this.name = name;
//        this.email = email;
//        this.password = password;
//        this.phoneNumber = phoneNumber;
//    }

    //콘솔에 println(user)을 찍을 때 객체 정보가 들어가서 찍히게 함.

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", realName='" + realName + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", status=" + status +
                ", auth=" + auth +
                ", boards=" + boards +
                ", profile=" + profile +
                ", userBoardTags=" + userBoardTags +
                '}';
    }
}

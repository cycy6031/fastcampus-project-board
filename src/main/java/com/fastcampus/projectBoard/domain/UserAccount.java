package com.fastcampus.projectBoard.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Table(indexes = {
    @Index(columnList = "userId", unique = true),
    @Index(columnList = "email", unique = true),
    @Index(columnList = "createdAt"),
    @Index(columnList = "createdBy")
})
@Entity
public class UserAccount extends AuditingField {
    @Id
    @Column(length = 50)
    private String userId;

    @Setter @Column(nullable = false) private String userPassword;

    @Setter @Column(length = 100) private String email;
    @Setter @Column(length = 100) private String nickname;
    @Setter private String memo;

    public UserAccount() {}

    private UserAccount(String userId, String userPassword, String email, String nickname, String memo) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
    }

    public static UserAccount of(String userId, String userPassword, String email, String nickname, String memo) {
        return new UserAccount(userId, userPassword, email, nickname, memo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount userAccount)) return false;
        return userId != null && userId.equals(userAccount.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}

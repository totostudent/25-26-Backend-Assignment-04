package com.gdg.jwt.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ToDo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "work_content", nullable = false) //DB 제약
    private String work;

    @Column(name = "work_daysLeft", nullable = false)
    private String daysLeft;

    @ManyToOne(fetch = FetchType.LAZY) //여러 '할 일'이 하나의 User(사용자)에 포함될 수 있음
    @JoinColumn(name = "user_id") //참조할 외래키 이름
    private User user;

    @Builder
    public ToDo(String work, String daysLeft, User user) {
        this.work = work;
        this.daysLeft = daysLeft;
        this.user = user;
    }

    public void update(String work, String daysLeft) {
        this.work = work;
        this.daysLeft = daysLeft;
    }
}

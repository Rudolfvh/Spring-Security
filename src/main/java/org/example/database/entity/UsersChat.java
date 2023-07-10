package org.example.database.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users_chat", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "chat_id"}))
public class UsersChat implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", referencedColumnName = "id", nullable = false)
    private Chat chat;

    public void setUser(User user) {
        this.user = user;
        this.user.getUsersChats().add(this);
    }

    public void setChat(Chat chat) {
        this.chat = chat;
        this.chat.getUsersChat().add(this);
    }

}

package com.chatop.back.rental.entity;

import lombok.*;
import java.util.List;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.chatop.back.user.entity.User;
import com.chatop.back.message.entity.Message;

@Entity
@Table(name = "RENTALS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private Double surface;

    @Column(nullable=false)
    private Double price;

    @Column(nullable=false)
    private String picture;

    @Column(nullable=false, length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    @Column(nullable=false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

package gymmi.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "creator", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private WorkspaceStatus status;

    @Column(nullable = false)
    private Integer goalScore;

    @Column(nullable = false)
    private int headCount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private String tag;
}

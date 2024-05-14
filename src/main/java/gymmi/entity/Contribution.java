package gymmi.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Contribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "worker_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Worker worker;

    @JoinColumn(name = "mission_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Mission mission;

    @Column(nullable = false)
    private int count;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

package gymmi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "workspace_id"})})
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "workspace_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Workspace workspace;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User register;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean isPicked;

    @Builder
    public Task(Workspace workspace, User register, String name) {
        this.workspace = workspace;
        this.register = register;
        this.name = name;
        this.isPicked = false;
    }

    public boolean isPicked() {
        return isPicked;
    }

    public void changeToPicked() {
        this.isPicked = true;
    }

    public boolean isRegisteredBy(User user) {
        return this.register.equals(user);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getRegister() {
        return register;
    }
}



package gymmi.workspace.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean isPicked;

    @Builder
    public Task(String name) {
        this.name = name;
        this.isPicked = false;
    }

    public boolean isPicked() {
        return isPicked;
    }

    public void changeToPicked() {
        this.isPicked = true;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}



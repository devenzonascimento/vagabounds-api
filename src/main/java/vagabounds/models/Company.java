package vagabounds.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="account_id", unique=true)
    private Account account;

    @Column(name = "cnpj", nullable = false, unique = true, length = 18)
    private String cnpj;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    // TODO: COLOCAR O RESTO DOS ATRIBUTOS E RELACIONAMENTOS
}

package br.com.franca.ShirtVirtual.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "acesso")
@SequenceGenerator(name = "seq_acesso", sequenceName = "seq_acesso", allocationSize = 1, initialValue = 1)
public class Acesso implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_acesso")
    private Long id;

    @Column(name = "descricao_acesso", nullable = false)
    private String descAcesso;

    @JsonIgnore
    @Override
    public String getAuthority() {

        return this.descAcesso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescAcesso() {
        return descAcesso;
    }

    public void setDescAcesso(String descAcesso) {
        this.descAcesso = descAcesso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Acesso)) return false;
        Acesso acesso = (Acesso) o;
        return getId().equals(acesso.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
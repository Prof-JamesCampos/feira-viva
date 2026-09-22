package br.com.feiraviva.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nome;

    private String descricao;

    // Adicione/Atualize o relacionamento com o pai:
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_pai_id")
    private Categoria categoriaPai;

    // Adicione o lado inverso (filhos):
    @OneToMany(mappedBy = "categoriaPai")
    private List<Categoria> subcategorias = new ArrayList<>();

    // Adicione o método do Composite uniforme:
    public boolean ehFolha() {
        return subcategorias == null || subcategorias.isEmpty();
    }

    protected Categoria() {
    }

    public Categoria(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    // getters e setters (gerar pelo IDE (Alt + Insert))

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getNome() {

        return nome;
    }

    public void setNome(String nome) {

        this.nome = nome;
    }

    public String getDescricao() {

        return descricao;
    }

    public void setDescricao(String descricao) {

        this.descricao = descricao;
    }

    public Categoria getCategoriaPai() {
        return categoriaPai;
    }

    public void setCategoriaPai(Categoria categoriaPai) {

        this.categoriaPai = categoriaPai;
    }

    public List<Categoria> getSubcategorias() {

        return subcategorias;
    }

    public void setSubcategorias(List<Categoria> subcategorias) {

        this.subcategorias = subcategorias;
    }
}

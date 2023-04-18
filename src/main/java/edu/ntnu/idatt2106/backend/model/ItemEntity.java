package edu.ntnu.idatt2106.backend.model;

/**
 * Entity for when saving in database(?)
 */
import edu.ntnu.idatt2106.backend.model.enums.Category;
import jakarta.persistence.*;


@Entity
@Table(name = "item")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "short_desc")
    private String shortDesc;

    @Column
    @Enumerated(EnumType.STRING)
    private Category category;
    @Column
    private double price;

    @Column
    private double weight;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image;


    public ItemEntity() {

    }
}

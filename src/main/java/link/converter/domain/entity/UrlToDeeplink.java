package link.converter.domain.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Entity representing url to deeplink pair used in conversion
 */
@Entity
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@Builder(setterPrefix = "set")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UrlToDeeplink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String deeplink;

}

package ang.neggaw.models;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * author by: ANG
 * since: 14/04/2022 16:20
 */

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable{

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;


    private Long idBook;

    @NonNull
    private long isbn;

    @NonNull
    private String title;

    @NonNull
    private String theme;

    @NonNull
    private int nbr_pages;

    @NonNull
    private String author;

    @NonNull
    private String editor;

    @NonNull
    private double price;

    @Override
    public String toString() {
        return "Book{" +
                "idBook=" + idBook +
                ", isbn=" + isbn +
                ", title='" + title + '\'' +
                ", theme='" + theme + '\'' +
                ", nbr_pages=" + nbr_pages +
                ", author='" + author + '\'' +
                ", editor='" + editor + '\'' +
                ", price=" + price +
                '}';
    }
}

package ang.neggaw.models;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * author by: ANG
 * since: 14/04/2022 16:21
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@ToString
public class Ordered implements Serializable {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;

    private Long idOrdered;

    @NonNull
    private long idBook;

    @NonNull
    private long idSupplier;

    private String datePurchase;

    @NonNull
    private double price;

    @NonNull
    private int nbr_copies;

    private Book book;
    private Supplier supplier;
}

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
public class Supplier implements Serializable {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;


    private Long idSupplier;

    @NonNull
    private String social_reason;

    @NonNull
    private String city;

    @NonNull
    private String phone;

    @NonNull
    private String email;
}
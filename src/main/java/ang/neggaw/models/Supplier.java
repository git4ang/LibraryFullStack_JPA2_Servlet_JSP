package ang.neggaw.models;

import lombok.*;

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
public class Supplier implements Serializable {

    /**
     *
     */
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

    @Override
    public String toString() {
        return "Supplier{" +
                "idSupplier=" + idSupplier +
                ", social_reason='" + social_reason + '\'' +
                ", city='" + city + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
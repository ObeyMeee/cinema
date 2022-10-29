package ua.com.andromeda.cinemaspringbootapp.mapper;

import org.springframework.stereotype.Component;
import ua.com.andromeda.cinemaspringbootapp.dto.Purchase;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class TupleMapper {

    public Purchase mapTupleToPurchase(Tuple tuple) {
        String sessionName = tuple.get("session_name", String.class);
        LocalDateTime sessionDate = tuple.get("session_date", Timestamp.class).toLocalDateTime();
        int totalPrice = tuple.get("total_price", BigInteger.class).intValue();
        int quantity = tuple.get("quantity", BigInteger.class).intValue();
        return new Purchase(sessionName, sessionDate, totalPrice, quantity);
    }
}

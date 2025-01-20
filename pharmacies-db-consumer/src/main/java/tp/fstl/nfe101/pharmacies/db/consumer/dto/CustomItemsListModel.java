package tp.fstl.nfe101.pharmacies.db.consumer.dto;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;

public class CustomItemsListModel<T> extends CollectionModel<EntityModel<T>> {
    public CustomItemsListModel(List<EntityModel<T>> content) {
        super(content);
    }
}

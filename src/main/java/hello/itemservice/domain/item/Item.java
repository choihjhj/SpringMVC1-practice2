package hello.itemservice.domain.item;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
//@Data 이것보다 게터세터쓰기
@Getter @Setter
public class Item {
    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item() {
    }

    public Item( String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}

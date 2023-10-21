package hello.itemservice.web.basic;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;
@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
@Slf4j
public class BasicItemController {
    private final ItemRepository itemRepository;
    @GetMapping
    public String items(Model model) { //상품 목록 페이지
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }
    @GetMapping("/{itemId}") //상품 상세 페이지
    public String item(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add") //상품 등록 폼 페이지
    public String addForm() {
        return "basic/addForm";
    }
    //@PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                            @RequestParam int price,
                            @RequestParam Integer quantity,
                            Model model) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);
        itemRepository.save(item);
        model.addAttribute("item", item);
        return "basic/item";
    }
    /**
     * @ModelAttribute("item") Item item
     * model.addAttribute("item", item); 자동 추가
     */
    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item) {
        itemRepository.save(item);
        //model.addAttribute("item", item); //자동 추가, 생략 가능
        return "basic/item";
    }
    /**
     * @ModelAttribute name 생략 가능
     * model.addAttribute(item); 자동 추가, 생략 가능
     * 생략시 model에 저장되는 name은 클래스명 첫글자만 소문자로 등록 Item -> item
     */
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        //만약 @ModelAttribute HelloData item 이렇게 파라미터로 받으면,
        //모델에는 HelloData->helloData 앞글자만 소문자로 바뀌고이렇게 들어감
        //즉, @ModelAttribute Item item 라고 하면 모델에는 Item->item 이렇게 바뀌어서 들어감
        itemRepository.save(item);
        return "basic/item";
    }
    /**
     * @ModelAttribute 자체 생략 가능
     * model.addAttribute(item) 자동 추가
     */
    //@PostMapping("/add") //등록처리 후 상품 상세 페이지로 이동
    public String addItemV4(Item item) {
        itemRepository.save(item);
        //return "basic/item"; //redirect:안해서 상세페이지로 이동했어도 url http://localhost:9090/basic/items/add 으로 변경없음
        return "redirect:/basic/items/"+item.getId(); //이런 방식을 PRG Post/Redirect/Get
        //근데 이렇게 하면 url 인코딩은 안됨 그래서 아래 addItemV6()처럼 RedirectAttributes redirectAttributes으로 addAttribute해줘야함
    }
    /**
     * RedirectAttributes
     */
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        //${param.status} : 타임리프에서 쿼리 파라미터를 편리하게 조회하는 기능
        //원래는 컨트롤러에서 모델에 직접 담고 값을 꺼내야 한다. 그런데 쿼리 파라미터는 자주 사용해서 타임리프에서 직접 지원한다.
        return "redirect:/basic/items/{itemId}";
    }
    @GetMapping("/{itemId}/edit") //상품 수정 폼 페이지
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }
    @PostMapping("/{itemId}/edit") //수정처리 후 상품 상세 페이지로 이동
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}"; //수정처리 후 url http://localhost:9090/basic/items/3 로 바뀌고 상품상세페이지로 이동
    }
    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("testA", 10000, 10));
        itemRepository.save(new Item("testB", 20000, 20));
    }
}
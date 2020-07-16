package icecreamPickup;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Sales_table")
public class Sales {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long orderId;
    private Long storeId;
    private String status;

    @PostPersist
    public void onPostPersist(){
        if(this.getStatus().equals("WAITING")) {
            StoreOrderReceived storeOrderReceived = new StoreOrderReceived();

            storeOrderReceived.setId(this.getId());
            storeOrderReceived.setOrderId(this.getOrderId());
            storeOrderReceived.setStoreId(this.getStoreId());

            BeanUtils.copyProperties(this, storeOrderReceived);
            storeOrderReceived.publishAfterCommit();
        } else if(this.getStatus().equals("ACCEPT")) {
            StoreOrderReceived storeOrderReceived = new StoreOrderReceived();

            storeOrderReceived.setId(this.getId());
            storeOrderReceived.setOrderId(this.getOrderId());
            storeOrderReceived.setStoreId(this.getStoreId());
            storeOrderReceived.setStatus("ACCEPT");

            BeanUtils.copyProperties(this, storeOrderReceived);
            storeOrderReceived.publishAfterCommit();
        }

    }

    @PostUpdate
    public void onPostUpdate(){
        if(this.getStatus().equals("COMPLETE")) {
            PickedUp pickedUp = new PickedUp();
            BeanUtils.copyProperties(this, pickedUp);
            pickedUp.publishAfterCommit();
        }
        if(this.getStatus().equals("READY")) {
            Packed packed = new Packed();
            BeanUtils.copyProperties(this, packed);
            packed.publishAfterCommit();
            // 여기 메시지 발송 부분
            System.out.println("## 주문한 아이스크림 포장이 완료되었습니다.");
        }
        if(this.getStatus().equals("CANCEL")) {
            OrderCanceled orderCanceled = new OrderCanceled();
            BeanUtils.copyProperties(this, orderCanceled);
            orderCanceled.publishAfterCommit();
        }
        if(this.getStatus().equals("ACCEPT")) {
            StoreOrderReceived storeOrderReceived = new StoreOrderReceived();

            storeOrderReceived.setId(this.getId());
            storeOrderReceived.setOrderId(this.getOrderId());
            storeOrderReceived.setStoreId(this.getStoreId());
            storeOrderReceived.setStatus("ACCEPT");

            BeanUtils.copyProperties(this, storeOrderReceived);
            storeOrderReceived.publishAfterCommit();
            // 여기 메시지 발송 부분
            System.out.println("## 매장에 주문이 접수되었습니다.");
        }


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }




}

package icecreamPickup;

import icecreamPickup.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @Autowired
    private SalesRepository salesRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverIceCreamOrderCanceled_OrderCancelReq(@Payload IceCreamOrderCanceled iceCreamOrderCanceled){

        if(iceCreamOrderCanceled.isMe()){
            System.out.println("##### listener OrderCancelReq : " + iceCreamOrderCanceled.toJson());
            Sales storesales = new Sales();
            storesales = salesRepository.findByOrderId(iceCreamOrderCanceled.getId());
            storesales.setStatus("CANCEL");
            // 레파지 토리에 save
            salesRepository.save(storesales);
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaymentApproved_StoreOrderAcceptReq(@Payload PaymentApproved paymentApproved){

        if(paymentApproved.isMe()){
            System.out.println("##### listener StoreOrderAcceptReq : " + paymentApproved.toJson());
            Sales storesales = new Sales();
            //storesales = salesRepository.findByOrderId(paymentApproved.getOrderId());
            storesales.setOrderId(paymentApproved.getOrderId());
            storesales.setStoreId(Long.valueOf(1));
            storesales.setStatus("WAITING");
            // 레파지 토리에 save
            salesRepository.save(storesales);

        }
    }
    /*
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaymentApproved_StoreOrderReceived(@Payload StoreOrderReceived storeOrderReceived){

        if(storeOrderReceived.isMe()){
            System.out.println("##### listener OrderCancelReq : " + storeOrderReceived.toJson());
            Sales storesales = new Sales();
            storesales = salesRepository.findByOrderId(storeOrderReceived.getId());
            storesales.setStatus("ACCEPT");
            storesales.setStoreId(storeOrderReceived.getStoreId());
            // 레파지 토리에 save
            salesRepository.save(storesales);
        }
    }
    */

}

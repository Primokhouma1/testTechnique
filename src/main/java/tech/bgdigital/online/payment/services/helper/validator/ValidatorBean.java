package tech.bgdigital.online.payment.services.helper.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.bgdigital.online.payment.models.entity.Partner;
import tech.bgdigital.online.payment.models.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Component
@Slf4j
public class ValidatorBean {
    @Autowired
    TransactionRepository transactionRepository;
    public boolean isAmount(BigDecimal amount){
        log.info("amount=>"+amount +"=>"+ amount.compareTo(new BigDecimal("1")));
        return amount.compareTo(new BigDecimal("1")) > 0;
    }
    public boolean isUrl(String url){
        String regex = "\\b(https|http)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern pattern = Pattern.compile(regex);
        return  pattern.matcher(url).find();
    }
    public boolean isSecureUrl(String url){
        String regex = "\\b(https)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern pattern = Pattern.compile(regex);
        return  pattern.matcher(url).find();
    }
    public boolean isFullName(String name){
        return Arrays.stream(name.split(" ")).toArray().length > 1;
    }
    public boolean isAddress(String address){
        return address.length() > 1;
    }
    public boolean isNotEmpty(String text){
        return !text.isEmpty() ;
    }
    public boolean isPan(String pan){
        String regex= "^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$";
        Pattern pattern = Pattern.compile(regex);
        return  pattern.matcher(pan).find();
    }
    public boolean isCvv(String cvv){
        List<String> cvvs = List.of(cvv.split(""));
        if(cvvs.size() != 3){
            return false;
        }
        return valideListNumberString(cvvs);
    }
    public boolean isExpireDate(String expiredDate){
        List<String> expiredDates = List.of(expiredDate.split("-"));
        if(expiredDates.size() != 2){
            return false;
        }
        if(expiredDates.get(0).length() != 4){
            return false;
        }
        if(expiredDates.get(1).length() != 2){
            return false;
        }
        return
                valideListNumberString(List.of(expiredDates.get(0).split("")))
                        &&
                        valideListNumberString( List.of(expiredDates.get(1).split("")) )
                     && valideMonthNumber(expiredDates.get(1))
                //&& dateNoExpired(expiredDates.get(0),expiredDates.get(1))
                      ;
    }

    private boolean valideListNumberString(List<String> listNumberString) {
        try{
            for (String itemDate:
                    listNumberString) {
                Integer number = Integer.valueOf(itemDate);
                log.info(String.valueOf(number));
            }
        }
        catch (NumberFormatException ex){
            return false;
        }
        return true ;
    }
    private boolean valideNumberString(String numberString) {
        try{
                Integer number = Integer.valueOf(numberString);
                log.info(String.valueOf(number));
        }
        catch (NumberFormatException ex){
            return false;
        }
        return true ;
    }
    private boolean valideMonthNumber(String numberString) {
        try{
            log.info("NUME"+ numberString);
                Integer number = Integer.valueOf(numberString);
                log.info(String.valueOf(number));
                if(number < 1 || number > 12){
                    return false ;
                }
        }
        catch (NumberFormatException ex){
            return false;
        }
        return true ;
    }
    private boolean dateNoExpired(String year, String month){
        Date date1 = new Date();
        Date date2 = new Date();
        date1.setMonth(Integer.parseInt(month) -1);
        date1.setYear(Integer.parseInt(year) - 1900);

        log.info(String.valueOf(date1));
        log.info(String.valueOf(date2));
        log.info(year);
        log.info(month);
        return date1.compareTo(date2) >= 0;
    }
    public boolean isExisteTransactionNumber(String trxNumer, Partner partner){
        return transactionRepository.findTransactionByPartenerTrxRefAndPartners(trxNumer,partner) != null;
    }
    public boolean isValidPhone(String phone){
        return phone != null && phone.length() >= 9 && this.valideListNumberString(List.of(phone.split("")));
    }
}

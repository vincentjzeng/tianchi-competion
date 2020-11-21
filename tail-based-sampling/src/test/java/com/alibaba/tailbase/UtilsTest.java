package com.alibaba.tailbase;

import com.alibaba.tailbase.clientprocess.ClientProcessData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientProcessData.class.getName());

    @org.junit.Test
    public void testIndexofTime() {
        String line = "19a1fff895394c5e|1589285985704700|13d80aeb8def8292|4ad8fb2612aa09e0|377|PromotionCenter|getAppConfig|192.168.107.250|http.status_code=200&http.url=http://localhost:9001/getItem&component=java-web-servlet&span.kind=server&http.method=GET&error=1";
        String tradeid = line.substring(0,line.indexOf("|"));
        String tags = line.substring(line.lastIndexOf("|"));
        LOGGER.info("tradeid:" + tradeid);
        LOGGER.info("tags:" + tags);
    }

    @org.junit.Test
    public void testSplitTime() {
        long before = System.currentTimeMillis();
        indexString();
        long after = System.currentTimeMillis();
        LOGGER.info("time spend:" + (after - before));
    }

    public boolean arrayContain(String[] tagList, String tag){
        for(String item : tagList){
            if (item.equals(tag)){
                return true;
            }
        }
        return false;
    }

    public void splitString(){
        String line = "19a1fff895394c5e|1589285985704700|13d80aeb8def8292|4ad8fb2612aa09e0|377|PromotionCenter|getAppConfig|192.168.107.250|http.status_code=200&http.url=http://localhost:9001/getItem&component=java-web-servlet&span.kind=server&http.method=GET&error=1";
        String[] cols = line.split("\\|");
        LOGGER.info("tradeid:" + cols[0]);
        LOGGER.info("tags:" + cols[8]);
//        if (cols != null && cols.length > 1 ) {
//            String traceId = cols[0];
//            if (cols.length > 8) {
//                String tags = cols[8];
//                if (tags != null) {
//                    if (tags.contains("error=1")) {
//                        LOGGER.info("error:" + cols[0]);
//                    } else if (tags.contains("http.status_code=") && tags.indexOf("http.status_code=200") < 0) {
//                        LOGGER.info("http.status_code:" + cols[0]);
//                    }
//                }
//            }
//        }
    }

    public void indexString(){
        String line = "19a1fff895394c5e|1589285985704700|13d80aeb8def8292|4ad8fb2612aa09e0|377|PromotionCenter|getAppConfig|192.168.107.250|http.status_code=200&http.url=http://localhost:9001/getItem&component=java-web-servlet&span.kind=server&http.method=GET&error=1";
        String tradeid = line.substring(0,line.indexOf("|"));
        String tags = line.substring(line.lastIndexOf("|"));
        LOGGER.info("tradeid:" + tradeid);
        LOGGER.info("tags:" + tags);

    }
}